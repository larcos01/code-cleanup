package v3

import org.junit.jupiter.api.Test
import original.CareerSettings
import original.PathTypes
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class StudentPathChecksTest {

    private val settings = CareerSettings(1, 2, 3)

    @Test
    fun main() {
        fun getPathCount(): Int = 1
        val isStaff = true
        val maxPathsCount = getMaxPathsCount(PathTypes.WhatIf, isStaff, settings)
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) {
            getPathCount()
        }
        assert(canAddPath)
    }

    @Test
    fun `get student max paths`() {
        val maxPathsCount = getMaxPathsCount(PathTypes.Standard, false, settings)
        assertEquals(1, maxPathsCount())
    }

    @Test
    fun `get student what-if max paths`() {
        val maxPathsCount = getMaxPathsCount(PathTypes.WhatIf, false, settings)
        assertEquals(3, maxPathsCount())
    }

    @Test
    fun `get staff max paths`() {
        val maxPathsCount = getMaxPathsCount(PathTypes.Standard, true, settings)
        assertEquals(2, maxPathsCount())
    }

    @Test
    fun `get staff max what-if path`() {
        val maxPathsCount = getMaxPathsCount(PathTypes.WhatIf, true, settings)
        assertEquals(2, maxPathsCount())
    }

    @Test
    fun `max paths is zero`() {
        val maxPathsCount = { 0 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 1 }
        assertFalse(canAddPath)
    }

    @Test
    fun `path count is less than max`() {
        val maxPathsCount = { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 2 }
        assert(canAddPath)
    }

    @Test
    fun `path count is greater than max`() {
        val maxPathsCount = { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 4 }
        assertFalse(canAddPath)
    }

    @Test
    fun `path count is equal to max`() {
        val maxPathsCount = { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 3 }
        assertFalse(canAddPath)
    }
}