package original

class StudentPathChecks(private val emplid: String, private val institution: String, private val container: Container) {
    fun canAddPath(userId: String, acadCareer: String, pathType: String): Boolean {
        val careerSettings = try {
            container.appSettings.getCareerSettings(acadCareer)
        } catch (e: Exception) {
            return false
        }

        var maxCount = if (!container.isStaffRequest) {
            careerSettings.maxPathsCount
        } else {
            careerSettings.maxAdvisorPathsCount
        }

        if (pathType == PathTypes.WhatIf.value) {
            if (!container.isStaffRequest) {
                maxCount = careerSettings.maxWhatIfPathsCount
            } else {
                return true
            }
        }

        if (maxCount == 0) return false

        val sql = """
            select count(*)
            from PS_H_DP_SP_PATH
            where EMPLID = :1
                and INSTITUTION = :2
                and ACAD_CAREER = :3
                and OWNER_ID = :4
                and H_DP_SP_TYPE = :5
        """.trimIndent()

        val count = runQuery(sql, emplid, institution, acadCareer, userId, pathType)
        return (count < maxCount)
    }
}
