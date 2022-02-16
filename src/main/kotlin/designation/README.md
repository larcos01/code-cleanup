Original source from https://medium.com/@arnab.sen44/how-i-refactored-a-nested-if-else-validation-using-a-design-pattern-ce287c32851d

```java
public Designation getDesignation(Employee emp) {
    if(emp.department == Department.SALES) {
        if (emp.yearsOfExperience < 5) {
            if (emp.qualification == Degree.HIGH_SCHOOL)
                return Designation.ASSISTANT_SALES_ASSOCIATE;
            else if(emp.qualification == Degree.BBA)
                return Designation.SALES_ASSOCIATE;
            else if(emp.qualification == Degree.MBA)
                return Designation.SALES_MANAGER;
            else {
                return Designation.UNKNOWN;
            }
        }
        if(emp.yearsOfExperience >=5 && emp.yearsOfExperience<10) {
            if (emp.qualification == Degree.HIGH_SCHOOL)
                return Designation.SALES_ASSOCIATE;
            else if(emp.qualification == Degree.BBA)
                return Dessignation.Account_Executive;
            else if(emp.qualification == Degree.MBA)
                return Designation.SENIOR_SALES_MANAGER;
            else return Designation.UNKNOWN;
        }
        else {
            return Designation.REGIONAL_HEAD;
        }
    }
    else if(emp.department == Department.ENGINEERING) {
        if(emp.yearsOfExperience < 5) {
            if(emp.qualification == Degree.Btech or emp.qualification == Degree.BE)
                return Designation.ASSISTANT_ENGINEER;
            else if(emp.qualification == Degree.Mtech or emp.qualification == Degree.Phd)
                return Disgnation.ASSOCIATE_ENGINEER;
            else return Designation.UNKNOWN;
        }
        else {
            if(emp.qualification == Degree.Btech or emp.qualification == Degree.BE)
                return Designation.ASSOCIATE_ENGINEER;
            else if(emp.qualification == Degree.Mtech or emp.qualification == Degree.Phd)
                return Disgnation.ENGINEER_LEAD;
            else return Designation.UNKNOWN;
        }
    }
}
```
