package v4

import arrow.core.*
import arrow.core.computations.either

typealias InstitutionId = String
typealias PersonId = String
typealias UserId = String
typealias AcademicCareerId = String

sealed class CanAddPathError {
    object CareerSettingsNotFound: CanAddPathError()
    object NewPathNotAllowed: CanAddPathError()
    object MaxPathCountReached: CanAddPathError()
}

data class CareerSettings(
    val maxPathsCount: Int,
    val maxAdvisorPathsCount: Int,
    val maxWhatIfPathsCount: Int,
    val maxAdvisorWhatIfPathsCount: Int,
)

enum class PathType {
    Standard,
    WhatIf,
    PublicWhatIf,
}

sealed class Role {
    object Advisor : Role()
}

data class User(
    val userId: UserId,
    val roles: List<Role>,
)

data class Person(
    val personId: PersonId,
)

fun User.hasRole(role: Role) = roles.contains(role)

fun interface GetCareerSettings {
    suspend operator fun invoke(
        institutionId: InstitutionId,
        academicCareerId: AcademicCareerId
    ): Option<CareerSettings>
}

fun interface GetStudentPathCount {
    suspend operator fun invoke(
        personId: PersonId,
        institutionId: InstitutionId,
        academicCareerId: AcademicCareerId,
        userId: UserId,
        pathType: PathType,
    ): Int
}

fun interface MaxCount {
    suspend operator fun invoke(careerSettings: CareerSettings, user: User): Int
}

class StandardMaxCount : MaxCount {
    override suspend fun invoke(careerSettings: CareerSettings, user: User): Int {
        if (user.hasRole(Role.Advisor)) {
            return careerSettings.maxAdvisorPathsCount
        }

        return careerSettings.maxPathsCount
    }
}

class WhatIfMaxCount : MaxCount {
    override suspend fun invoke(careerSettings: CareerSettings, user: User): Int {
        if (user.hasRole(Role.Advisor)) {
            return careerSettings.maxAdvisorWhatIfPathsCount
        }

        return careerSettings.maxWhatIfPathsCount
    }
}

fun PathType.makeGetMaxCount(): MaxCount = when (this) {
    PathType.Standard -> StandardMaxCount()
    PathType.WhatIf -> WhatIfMaxCount()
    PathType.PublicWhatIf -> StandardMaxCount()
}

class CanAddPath(
    val getCareerSettings: GetCareerSettings,
    val getStudentPathCount: GetStudentPathCount,
) {
    suspend operator fun invoke(
        institutionId: InstitutionId,
        academicCareerId: AcademicCareerId,
        person: Person,
        user: User,
        pathType: PathType,
    ): Either<CanAddPathError, Unit> = either {
        val maxCount = getCareerSettings(institutionId, academicCareerId)
            .fold({
                  CanAddPathError.CareerSettingsNotFound.left()
            }, {
                val getMaxCount = pathType.makeGetMaxCount()

                getMaxCount(it, user).let { maxCount ->
                    if (maxCount == 0) {
                        CanAddPathError.NewPathNotAllowed.left()
                    } else {
                        maxCount.right()
                    }
                }
            }).bind()

        getStudentPathCount(
            personId = person.personId,
            institutionId = institutionId,
            academicCareerId = academicCareerId,
            userId = user.userId,
            pathType = pathType
        ).right().flatMap {
            if (it == 0 || it >= maxCount) {
                CanAddPathError.MaxPathCountReached.left()
            } else {
                Unit.right()
            }
        }.bind()
    }
}