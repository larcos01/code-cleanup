package v3

import original.PathTypes
import original.runQuery

/**
 * Uses type aliases instead of functional interfaces.
 */
object StudentPathChecks {
    fun canAddPath(getMaxPathsCount: MaxPathsCount, getPathCount: () -> Int): Boolean {
        val maxPathsCount = getMaxPathsCount()
        return if (maxPathsCount == 0) false else getPathCount() < maxPathsCount
    }
}

typealias MaxPathsCount = () -> Int

fun getStudentMaxPathsCount(): MaxPathsCount = { 1 }
fun getAdvisorMaxPathsCount(): MaxPathsCount = { 2 }
fun getWhatIfMaxPathsCount(): MaxPathsCount = { 3 }

fun getMaxPathsCount(pathType: PathTypes, isStaff: Boolean): MaxPathsCount {
    if (isStaff) return getAdvisorMaxPathsCount()
    return if (pathType == PathTypes.WhatIf)
        getWhatIfMaxPathsCount()
    else
        getStudentMaxPathsCount()
}

fun getPathCount(emplid: String, institutionId: String, acadCareer: String, userId: String, pathType: PathTypes): Int {
    val sql = "select count(*) from table where emplid=:1"
    return runQuery(sql, emplid, institutionId, acadCareer, userId, pathType.value)
}