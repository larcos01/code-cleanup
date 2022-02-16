package designation

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class DesignationKtTest {
    @Test
    fun `assistant sales associate`() {
        val employee = Employee(Department.SALES, 4, Degree.HIGH_SCHOOL)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.AssistantSalesAssociate, designation)
    }

    @Test
    fun `sales associate less than 5 years experience`() {
        val employee = Employee(Department.SALES, 4, Degree.BBA)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.SalesAssociate, designation)
    }

    @Test
    fun `sales manager less than 5 years experience`() {
        val employee = Employee(Department.SALES, 4, Degree.MBA)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.SalesManager, designation)
    }

    @Test
    fun `in sales with less than 5 years experience and various degrees`() {
        val salesDegrees = listOf(Degree.HIGH_SCHOOL, Degree.BBA, Degree.MBA)
        val degrees = Degree.values().toList() - salesDegrees
        degrees.forEach { degree ->
            val employee = Employee(Department.SALES, 4, degree)
            val designation = getDesignation(employee).getOrThrow()
            assertEquals(Designation.Unknown, designation)
        }
    }

    @Test
    fun `in sales with 5 to 10 years experience and various degrees`() {
        val salesDegrees = listOf(Degree.HIGH_SCHOOL, Degree.BBA, Degree.MBA)
        val degrees = Degree.values().toList() - salesDegrees
        degrees.forEach { degree ->
            val employee = Employee(Department.SALES, 5, degree)
            val designation = getDesignation(employee).getOrThrow()
            assertEquals(Designation.Unknown, designation)
        }
    }

    @Test
    fun `sales associate with 5 years experience`() {
        val employee = Employee(Department.SALES, 5, Degree.HIGH_SCHOOL)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.SalesAssociate, designation)
    }

    @Test
    fun `account executive with 5 years experience`() {
        val employee = Employee(Department.SALES, 5, Degree.BBA)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.AccountExecutive, designation)
    }

    @Test
    fun `sales manager with 5 years experience`() {
        val employee = Employee(Department.SALES, 5, Degree.MBA)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.SeniorSalesManager, designation)
    }

    @Test
    fun `regional head`() {
        val employee = Employee(Department.SALES, 10, Degree.HIGH_SCHOOL)
        val designation = getDesignation(employee).getOrThrow()
        assertEquals(Designation.RegionalHead, designation)
    }

    @Test
    fun `assistant engineer with less than 5 years experience`() {
        val employee1 = Employee(Department.ENGINEERING, 4, Degree.Btech)
        val designation1 = getDesignation(employee1).getOrThrow()
        assertEquals(Designation.AssistantEngineer, designation1)

        val employee2 = Employee(Department.ENGINEERING, 4, Degree.BE)
        val designation2 = getDesignation(employee2).getOrThrow()
        assertEquals(Designation.AssistantEngineer, designation2)
    }

    @Test
    fun `associate engineer with less than 5 years experience`() {
        val employee1 = Employee(Department.ENGINEERING, 4, Degree.Mtech)
        val designation1 = getDesignation(employee1).getOrThrow()
        assertEquals(Designation.AssociateEngineer, designation1)

        val employee2 = Employee(Department.ENGINEERING, 4, Degree.Phd)
        val designation2 = getDesignation(employee2).getOrThrow()
        assertEquals(Designation.AssociateEngineer, designation2)
    }

    @Test
    fun `associate engineer with 5 or more years experience`() {
        val employee1 = Employee(Department.ENGINEERING, 5, Degree.Btech)
        val designation1 = getDesignation(employee1).getOrThrow()
        assertEquals(Designation.AssociateEngineer, designation1)

        val employee2 = Employee(Department.ENGINEERING, 5, Degree.BE)
        val designation2 = getDesignation(employee2).getOrThrow()
        assertEquals(Designation.AssociateEngineer, designation2)
    }

    @Test
    fun `engineer lead`() {
        val employee1 = Employee(Department.ENGINEERING, 5, Degree.Mtech)
        val designation1 = getDesignation(employee1).getOrThrow()
        assertEquals(Designation.EngineerLead, designation1)

        val employee2 = Employee(Department.ENGINEERING, 5, Degree.Phd)
        val designation2 = getDesignation(employee2).getOrThrow()
        assertEquals(Designation.EngineerLead, designation2)
    }

    @Test
    fun `in engineering with less than 5 years experience and various degrees`() {
        val engineeringDegrees = listOf(Degree.Btech, Degree.BE, Degree.Mtech, Degree.Phd)
        val degrees = Degree.values().toList() - engineeringDegrees
        degrees.forEach { degree ->
            val employee = Employee(Department.ENGINEERING, 4, degree)
            val designation = getDesignation(employee).getOrThrow()
            assertEquals(Designation.Unknown, designation)
        }
    }

    @Test
    fun `in engineering with 5 to 10 years experience and various degrees`() {
        val engineeringDegrees = listOf(Degree.Btech, Degree.BE, Degree.Mtech, Degree.Phd)
        val degrees = Degree.values().toList() - engineeringDegrees
        degrees.forEach { degree ->
            val employee = Employee(Department.ENGINEERING, 5, degree)
            val designation = getDesignation(employee).getOrThrow()
            assertEquals(Designation.Unknown, designation)
        }
    }
}