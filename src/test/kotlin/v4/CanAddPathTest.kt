package v4

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

class GetCareerSettingsStub(private val settings: Option<CareerSettings> = None) : GetCareerSettings {
    override suspend fun invoke(
        institutionId: InstitutionId,
        academicCareerId: AcademicCareerId,
    ): Option<CareerSettings> = settings
}

class GetStudentPathCountStub(private val count: Int = 0) : GetStudentPathCount {
    override suspend fun invoke(
        personId: PersonId,
        institutionId: InstitutionId,
        academicCareerId: AcademicCareerId,
        userId: UserId,
        pathType: PathType,
    ): Int = count
}

class CanAddPathTest {
    companion object {
        const val INSTITUTION_ID = "INST"
        const val ACADEMIC_CAREER_ID = "ACAD"
        const val PERSON_ID = "PERS"
        const val USER_ID = "USR"
    }

    @Nested
    inner class StandardPath : BaseTest {
        override val pathType: PathType
            get() = PathType.Standard
    }

    @Nested
    inner class WhatIfPath : BaseTest {
        override val pathType: PathType
            get() = PathType.WhatIf
    }

    @Nested
    inner class PublicWhatIfPath : BaseTest {
        override val pathType: PathType
            get() = PathType.PublicWhatIf
    }

    @Test
    fun `given no career settings return career settings not found`() {
        val canAddPath = CanAddPath(
            getCareerSettings = GetCareerSettingsStub(),
            getStudentPathCount = GetStudentPathCountStub()
        )

        val result = runBlocking {
            canAddPath(
                institutionId = INSTITUTION_ID,
                academicCareerId = ACADEMIC_CAREER_ID,
                person = Person(PERSON_ID),
                user = User(USER_ID, listOf()),
                pathType = PathType.Standard,
            )
        }

        result shouldBeLeft CanAddPathError.CareerSettingsNotFound
    }

    interface BaseTest {
        val pathType: PathType

        @Test
        fun `given zero max count return new path not allowed`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 0,
                        maxAdvisorPathsCount = 10,
                        maxWhatIfPathsCount = 0,
                        maxAdvisorWhatIfPathsCount = 10
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub()
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf()),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.NewPathNotAllowed
        }

        @Test
        fun `given zero max count and has advisor role return new path not allowed`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 10,
                        maxAdvisorPathsCount = 0,
                        maxWhatIfPathsCount = 10,
                        maxAdvisorWhatIfPathsCount = 0
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub()
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf(Role.Advisor)),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.NewPathNotAllowed
        }

        @Test
        fun `given zero student path count for student return max path count reached`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 1,
                        maxAdvisorPathsCount = 1,
                        maxWhatIfPathsCount = 1,
                        maxAdvisorWhatIfPathsCount = 1
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(0)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf()),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }

        @Test
        fun `given zero student path count for advisor return max path count reached`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 1,
                        maxAdvisorPathsCount = 1,
                        maxWhatIfPathsCount = 1,
                        maxAdvisorWhatIfPathsCount = 1
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(0)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf(Role.Advisor)),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }

        @Test
        fun `given student path count equal to student max count return max count reached`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 1,
                        maxAdvisorPathsCount = 0,
                        maxWhatIfPathsCount = 1,
                        maxAdvisorWhatIfPathsCount = 0
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(1)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf()),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }

        @Test
        fun `given student path count equal to advisor max count return max count reached`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 0,
                        maxAdvisorPathsCount = 1,
                        maxWhatIfPathsCount = 0,
                        maxAdvisorWhatIfPathsCount = 1
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(1)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf(Role.Advisor)),
                    pathType = pathType,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }

        @Test
        fun `given student path count greater than student max count return success`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 2,
                        maxAdvisorPathsCount = 0,
                        maxWhatIfPathsCount = 2,
                        maxAdvisorWhatIfPathsCount = 0
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(1)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf()),
                    pathType = pathType,
                )
            }

            result shouldBeRight Unit
        }

        @Test
        fun `given student path count greater than advisor max count return success`() {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 0,
                        maxAdvisorPathsCount = 2,
                        maxWhatIfPathsCount = 0,
                        maxAdvisorWhatIfPathsCount = 2
                    ).toOption()
                ),
                getStudentPathCount = GetStudentPathCountStub(1)
            )

            val result = runBlocking {
                canAddPath(
                    institutionId = INSTITUTION_ID,
                    academicCareerId = ACADEMIC_CAREER_ID,
                    person = Person(PERSON_ID),
                    user = User(USER_ID, listOf(Role.Advisor)),
                    pathType = pathType,
                )
            }

            result shouldBeRight Unit
        }
    }
}