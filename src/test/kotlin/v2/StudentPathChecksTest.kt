package v2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class StudentPathChecksTest {

    @Test
    fun `get student max paths`() {
        val maxPathsCount = getMaxPathsCount(false, false)
        assertEquals(1, maxPathsCount())
    }

    @Test
    fun `get student what-if max paths`() {
        val maxPathsCount = getMaxPathsCount(true, false)
        assertEquals(3, maxPathsCount())
    }

    @Test
    fun `get staff max paths`() {
        val maxPathsCount = getMaxPathsCount(false, true)
        assertEquals(2, maxPathsCount())
    }

    @Test
    fun `get staff max what-if path`() {
        val maxPathsCount = getMaxPathsCount(true, true)
        assertEquals(2, maxPathsCount())
    }

    @Test
    fun `max paths is zero`() {
        val maxPathsCount = MaxPathsCount { 0 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 1 }
        assertFalse(canAddPath)
    }

    @Test
    fun `path count is less than max`() {
        val maxPathsCount = MaxPathsCount { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 2 }
        assert(canAddPath)
    }

    @Test
    fun `path count is greater than max`() {
        val maxPathsCount = MaxPathsCount { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 4 }
        assertFalse(canAddPath)
    }

    @Test
    fun `path count is equal to max`() {
        val maxPathsCount = MaxPathsCount { 3 }
        val canAddPath = StudentPathChecks.canAddPath(maxPathsCount) { 3 }
        assertFalse(canAddPath)
    }
}
