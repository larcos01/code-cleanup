package v1

import original.CareerSettings
import original.Container
import original.PathTypes
import original.runQuery

/**
 * Signature not changed but code is much smaller and modularized.
 */
internal class StudentPathChecks(
    private val emplid: String,
    private val institution: String,
    private val container: Container
) {
    fun canAddPath(userId: String, acadCareer: String, pathType: String): Boolean {
        if (pathType == PathTypes.WhatIf.value && container.isStaffRequest) return true
        val careerSettings = getCareerSettings(container, acadCareer) ?: return false
        val maxCount = getMaxCount(container, careerSettings, pathType)
        if (maxCount == 0) return false
        val count = getPathCount(acadCareer, userId, pathType)
        return (count < maxCount)
    }

    private fun getMaxCount(container: Container, careerSettings: CareerSettings, pathType: String): Int = when {
        container.isStaffRequest -> careerSettings.maxAdvisorPathsCount
        pathType == PathTypes.WhatIf.value -> careerSettings.maxWhatIfPathsCount
        else -> careerSettings.maxPathsCount
    }

    private fun getCareerSettings(container: Container, acadCareer: String): CareerSettings? {
        return try {
            container.appSettings.getCareerSettings(acadCareer)
        } catch (e: Exception) {
            null
        }
    }

    private fun getPathCount(acadCareer: String, userId: String, pathType: String): Int {
        val sql = """
                select count(*)
                from PS_H_DP_SP_PATH
                where EMPLID = :1
                    and INSTITUTION = :2
                    and ACAD_CAREER = :3
                    and OWNER_ID = :4
                    and H_DP_SP_TYPE = :5
            """.trimIndent()
        return runQuery(sql, emplid, institution, acadCareer, userId, pathType)
    }
}
