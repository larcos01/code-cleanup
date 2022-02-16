package v3

import original.CareerSettings
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

fun getStudentMaxPathsCount(settings: CareerSettings): MaxPathsCount = { settings.maxPathsCount }
fun getAdvisorMaxPathsCount(settings: CareerSettings): MaxPathsCount = { settings.maxAdvisorPathsCount }
fun getWhatIfMaxPathsCount(settings: CareerSettings): MaxPathsCount = { settings.maxWhatIfPathsCount }

fun getMaxPathsCount(pathType: PathTypes, isStaff: Boolean, settings: CareerSettings): MaxPathsCount {
    if (isStaff) return getAdvisorMaxPathsCount(settings)
    return if (pathType == PathTypes.WhatIf)
        getWhatIfMaxPathsCount(settings)
    else
        getStudentMaxPathsCount(settings)
}

fun getPathCount(emplid: String, institutionId: String, acadCareer: String, userId: String, pathType: PathTypes): Int {
    val sql = "select count(*) from table where emplid=:1"
    return runQuery(sql, emplid, institutionId, acadCareer, userId, pathType.value)
}