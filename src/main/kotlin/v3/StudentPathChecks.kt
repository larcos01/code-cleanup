package v3

/**
 * Uses type aliases instead of functional interfaces.
 */
object StudentPathChecks {
    fun canAddPath(getMaxPathsCount: MaxPathsCount, getPathCount: GetPathCount): Boolean {
        val maxPathsCount = getMaxPathsCount()
        return if (maxPathsCount == 0) false else getPathCount() < maxPathsCount
    }
}

typealias GetPathCount = () -> Int
typealias MaxPathsCount = () -> Int

val studentMaxPathsCount: MaxPathsCount = { 1 }
val advisorMaxPathsCount: MaxPathsCount = { 2 }
val whatIfMaxPathsCount: MaxPathsCount = { 3 }

fun getMaxPathsCount(isWhatIf: Boolean, isStaff: Boolean): MaxPathsCount {
    if (isStaff) return advisorMaxPathsCount
    return if (isWhatIf)
        whatIfMaxPathsCount
    else
        studentMaxPathsCount
}
