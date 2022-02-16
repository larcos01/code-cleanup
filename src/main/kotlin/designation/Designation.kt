package designation

data class Employee(val department: Department, val yearsOfExperience: Int, val degree: Degree)

enum class Department {
    SALES,
    ENGINEERING,
}

enum class Degree {
    HIGH_SCHOOL,
    BBA,
    MBA,
    BE,
    Phd,
    Mtech,
    Btech,
}

enum class Designation(val checker: DesignationCheck) {
    AccountExecutive(isAccountExecutive()),
    AssistantEngineer(isAssistantEngineer()),
    AssociateEngineer(isAssociateEngineer()),
    AssistantSalesAssociate(isAssistantSalesAssociate()),
    EngineerLead(isEngineerLead()),
    RegionalHead(isRegionalHead()),
    SalesAssociate(isSalesAssociate()),
    SalesManager(isSalesManager()),
    SeniorSalesManager(isSeniorSalesManager()),
    Unknown(isUnknown())
}

typealias DesignationCheck = (Employee) -> Boolean

fun isAccountExecutive(): DesignationCheck = { employee ->
    with(employee) {
        department == Department.SALES &&
                yearsOfExperience in 5 until 10
                && degree == Degree.BBA
    }
}

fun isAssistantEngineer(): DesignationCheck = { employee ->
    with(employee) {
        department == Department.ENGINEERING &&
                yearsOfExperience < 5
                && (degree == Degree.Btech || degree == Degree.BE)
    }
}

fun isAssociateEngineer(): DesignationCheck = { employee ->
    if (employee.department != Department.ENGINEERING) false
    else {
        with(employee) {
            (yearsOfExperience < 5 && (degree == Degree.Mtech || degree == Degree.Phd)) ||
                    (yearsOfExperience >= 5 && (degree == Degree.Btech || degree == Degree.BE))
        }
    }
}

fun isEngineerLead(): DesignationCheck = {
    with(it) {
        department == Department.ENGINEERING &&
                yearsOfExperience >= 5 &&
                (degree == Degree.Mtech || degree == Degree.Phd)
    }
}

fun isAssistantSalesAssociate(): DesignationCheck = {
    with(it) {
        department == Department.SALES &&
                yearsOfExperience < 5 &&
                degree == Degree.HIGH_SCHOOL
    }
}

fun isRegionalHead(): DesignationCheck = {
    with(it) {
        department == Department.SALES &&
                yearsOfExperience >= 10
    }
}

fun isSalesAssociate(): DesignationCheck = {
    if (it.department != Department.SALES) false
    else {
        with(it) {
            (yearsOfExperience < 5 && degree == Degree.BBA) ||
                    (yearsOfExperience in 5 until 10 && degree == Degree.HIGH_SCHOOL)
        }
    }
}

fun isSalesManager(): DesignationCheck = {
    with(it) {
        department == Department.SALES &&
                yearsOfExperience < 5 &&
                degree == Degree.MBA
    }
}

fun isSeniorSalesManager(): DesignationCheck = {
    with(it) {
        department == Department.SALES
                && yearsOfExperience in 5 until 10
                && degree == Degree.MBA
    }
}

fun isUnknown(): DesignationCheck = { employee ->
    val isUnknownSales: DesignationCheck = {
        val isSales = it.department == Department.SALES
        val isLessThanFive = it.yearsOfExperience < 5
        val isFiveToTen = it.yearsOfExperience in 5 until 10
        val degrees = listOf(Degree.HIGH_SCHOOL, Degree.BBA, Degree.MBA)
        isSales && (isLessThanFive || isFiveToTen) && !degrees.contains(it.degree)
    }

    val isUnknownEngineering: DesignationCheck = {
        with(it) {
            val degrees = listOf(Degree.Btech, Degree.BE, Degree.Mtech, Degree.Phd)
            department == Department.ENGINEERING && !degrees.contains(degree)
        }
    }
    isUnknownSales(employee) || isUnknownEngineering(employee)
}

fun getDesignation(
    employee: Employee,
    designations: Collection<Designation> = Designation.values().toList()
): Result<Designation> =
    runCatching {
        designations.find { designation ->
            designation.checker(employee)
        } ?: error("No designation found.")
    }
