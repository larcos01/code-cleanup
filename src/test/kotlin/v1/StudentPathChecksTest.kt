package v1

import org.junit.jupiter.api.Test
import original.AppSettings
import original.Container
import original.PathTypes
import original.StudentPathChecks

internal class StudentPathChecksTest {

    @Test
    fun `impossible to test`() {
        val studentPathChecks = StudentPathChecks("AA0001", "PSUNV", Container(AppSettings()))
        val canAddPath = studentPathChecks.canAddPath("AA0001", "UGRD", PathTypes.WhatIf.value)
//        assert(canAddPath)
    }
}
