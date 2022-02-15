The code below was taken from a PeopleCode app package to be used as a refactoring exercise.

## Packages
* *original*: Contains the code below translated into Kotlin
* *v1*: refactored without changing the signature of the method
* *v2*: refactored to use functional interfaces (SAM interfaces)
* *v3*: refactored using function types

```text
method CanAddPath
   /+ &psUserId as String, +/
   /+ &psAcadCareer as String, +/
   /+ &psPathType as String +/
   /+ Returns Boolean +/
   try
      
      Local HPT_DP_APP_SETTINGS:AppCareerSettings &oCareerSettings = &moContainer.AppSettings.GetCareerSettings(&psAcadCareer);
      
   catch HPT_DP_APP_SETTINGS:EXCEPTIONS:CareerNotFound &ex
      Return False;
   end-try;
   
   Local integer &iMaxCount;
   
   If Not &moContainer.IsStaffRequest Then
      &iMaxCount = &oCareerSettings.MaxPathsCount;
   Else
      &iMaxCount = &oCareerSettings.MaxAdvisorPathsCount;
   End-If;
   
   If &psPathType = &moPathTypes.WhatIf Then
      
      If Not &moContainer.IsStaffRequest Then
         &iMaxCount = &oCareerSettings.MaxWhatIfPathsCount;
      Else
         Return True;
      End-If;
      
   End-If;
   
   If &iMaxCount = 0 Then
      Return False;
   End-If;
   
   Local integer &iCount;
   Local string &sSQL;
   
   &sSQL = "        select count(*)          ";
   &sSQL = &sSQL | "  from %Table(:1)        ";
   &sSQL = &sSQL | " where EMPLID       = :2 ";
   &sSQL = &sSQL | "   and INSTITUTION  = :3 ";
   &sSQL = &sSQL | "   and ACAD_CAREER  = :4 ";
   &sSQL = &sSQL | "   and OWNER_ID     = :5 ";
   &sSQL = &sSQL | "   and H_DP_SP_TYPE = :6 ";
   
   SQLExec(&sSQL, Record.H_DP_SP_PATH, &msEmplId, &msInstitution, &psAcadCareer, &psUserId, &psPathType, &iCount);
   
   Return (&iCount < &iMaxCount);
   
end-method;
```
