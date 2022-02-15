package original

import org.junit.jupiter.api.Test

internal class StudentPathChecksTest {
    @Test
    fun `student can add path`() {
        val studentPathChecks = StudentPathChecks("AA0001", "PSUNV", Container(AppSettings()))
        val canAddPath = studentPathChecks.canAddPath("AA0001", "UGRD", PathTypes.WhatIf.value)
//        assert(canAddPath)
    }
}