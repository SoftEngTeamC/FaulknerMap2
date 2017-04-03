##README FOR DATABASE

####Any class in our src file can access the database if these steps are taken:

1. Include these imports at the top of the class:
```
        import db.Driver;
        import db.dbHelper.*;
```
        
2. Depending on which database table you want to access, create its 
respective Helper Object like this:
```
        HospitalServicesHelper hs = Driver.getHospitalServiceHelper();
```

3. You now have access to all the helper functions
    Here is an example of all the functions you can call and how they work:

    To print the table (used for testing mostly):
    ```
        hs.printAllServicesRows()
    ```
    To add an element to the table (make sure it is the correct class):
    ```
        HospitalService temp = new HospitalService("My Name", "My location");
        hs.addHospitalService(temp);
    ```
    To get a element from the table:
    ```
        hs.getHospitalService(temp.getId());
    ```
    To get an ArrayList<> of all elements in the table (the parameter
    is the ORDER BY command for SQL. Passing in null will return a list 
    sorted by alphabetical order by name):
    ```
        ArrayList<HospitalService> list = hs.getHospitalServices(null);
    ```    
    To get a specific element in the table:
    ```
        HospitalService currHS = hs.getHospitalService(temp.getId());
    ```    
    To update an edited element (each element in the database has a unique
    UUID so as long as you alter the attributes of the specific element that 
    is already in the table it will work):
    ```
        temp.setName("Other name");
        hs.updateHospitalService(temp); 
    ```
    To delete a element from the table (same as update for UUID needing
        to be the same as the one already in the table):
    ```
            hs.deleteHospitalService(temp);
    ```
           
            