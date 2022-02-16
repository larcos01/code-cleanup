package v4

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

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

    @TestFactory
    fun `given zero max count return new path not allowed`() = PathType.values().map { input ->
        DynamicTest.dynamicTest("for $input") {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 0,
                        maxAdvisorPathsCount = 0,
                        maxWhatIfPathsCount = 0,
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
                    user = User(USER_ID, listOf()),
                    pathType = PathType.Standard,
                )
            }

            result shouldBeLeft CanAddPathError.NewPathNotAllowed
        }
    }

    @TestFactory
    fun `given zero student path count return max path count reached`() = PathType.values().map { input ->
        DynamicTest.dynamicTest("for $input") {
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
                    pathType = PathType.Standard,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }
    }

    @TestFactory
    fun `given student path count equal to max count return max count reached`() = PathType.values().map { input ->
        DynamicTest.dynamicTest("for $input") {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 1,
                        maxAdvisorPathsCount = 1,
                        maxWhatIfPathsCount = 1,
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
                    user = User(USER_ID, listOf()),
                    pathType = PathType.Standard,
                )
            }

            result shouldBeLeft CanAddPathError.MaxPathCountReached
        }
    }

    @TestFactory
    fun `given student path count greater than max count return success`() = PathType.values().map { input ->
        DynamicTest.dynamicTest("for $input") {
            val canAddPath = CanAddPath(
                getCareerSettings = GetCareerSettingsStub(
                    CareerSettings(
                        maxPathsCount = 2,
                        maxAdvisorPathsCount = 2,
                        maxWhatIfPathsCount = 2,
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
                    user = User(USER_ID, listOf()),
                    pathType = PathType.Standard,
                )
            }

            result shouldBeRight Unit
        }
    }
}