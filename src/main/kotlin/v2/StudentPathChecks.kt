package v2

/**
 * Uses functional interfaces.
 */
object StudentPathChecks {
    fun canAddPath(getMaxPathsCount: MaxPathsCount, getPathCount: GetPathCount): Boolean {
        val maxPathsCount = getMaxPathsCount()
        return if (maxPathsCount == 0) false else getPathCount() < maxPathsCount
    }
}

fun interface GetPathCount {
    operator fun invoke(): Int
}

fun interface MaxPathsCount {
    operator fun invoke(): Int
}

val studentMaxPathsCount = MaxPathsCount { 1 }
val advisorMaxPathsCount = MaxPathsCount { 2 }
val whatIfMaxPathsCount = MaxPathsCount { 3 }

fun getMaxPathsCount(isWhatIf: Boolean, isStaff: Boolean): MaxPathsCount {
    if (isStaff) return advisorMaxPathsCount
    return if (isWhatIf)
        whatIfMaxPathsCount
    else
        studentMaxPathsCount
}
