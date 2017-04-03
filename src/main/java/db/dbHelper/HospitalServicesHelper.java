package db.dbHelper;

import db.HospitalSchema.HospitalServiceSchema.*;
import db.HospitalService;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Gina on 3/31/17.
 */
public class HospitalServicesHelper {

    private static HospitalServicesHelper hospitalServicesHelper;

    private Connection connection;
    private Statement statement;
    private ArrayList<HospitalService> originalList;

    /**
     * Initialize HospitalServiceHelper if not already created
     * We only need one instance of it since there is only one table
     *
     * @param connection
     * @return
     */
    public static HospitalServicesHelper get(Connection connection) {

        if (hospitalServicesHelper == null) {
            hospitalServicesHelper = new HospitalServicesHelper(connection);
            System.out.println("Created new HospitalProfessionalsHelper");
        }
        return hospitalServicesHelper;
    }

    /**
     * Constructor for HospitalServiceHelper
     * Populates database if first time calling
     *
     * @param connection
     */
    private HospitalServicesHelper(Connection connection) {
        this.connection = connection;

        try {
            statement = connection.createStatement();

            //check if table is empty
            if (originalList == null) {
                originalList = new ArrayList<>(); //initialize empty array and populate
                //populate table
                populateArray();
            }
        } catch (SQLException e) {
            System.out.println("Constructor error");
            e.printStackTrace();
        }
    }

    /**
     * Add a HospitalService to the database
     *
     * @param service
     * @return success
     */
    public boolean addHospitalService(HospitalService service) {
        //insert HospitalService into table
        String str = "INSERT INTO " + HospitalServiceTable.NAME + " VALUES (" +
                "'" + service.getId().toString() + "', '" + service.getName() + "', '" + service.getLocation() + "')";
        try {
            statement.executeUpdate(str);
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add HospitalService: " + service.getName());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function takes in edited HospitalService and updates it
     *
     * @param service
     * @return success
     */
    public boolean updateHospitalService(HospitalService service) {
        //check table to make sure service is already there
        HospitalService temp = getHospitalService(service.getId());
        if (temp == null) { //could not find oldService to edit
            System.out.println("Could not find HospitalService " + service.getName() + " to update");
            return false;
        } else {
            //updating
            String str = "UPDATE " + HospitalServiceTable.NAME + " SET " + HospitalServiceTable.Cols.NAME +
                    " = '" + service.getName() + "', " + HospitalServiceTable.Cols.LOCATION +
                    " = '" + service.getLocation() + "' WHERE " + HospitalServiceTable.Cols.ID + " = '" +
                    service.getId().toString() + "'";
            try {
                statement.executeUpdate(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not update HospitalService: " + service.getName());
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function that takes in a service and deletes it from the database
     *
     * @param service HospitalService
     * @return success
     */
    public boolean deleteHospitalService(HospitalService service) {
        //check table to make sure oldService is already there
        HospitalService temp = getHospitalService(service.getId());
        if (temp == null) { //could not find oldService to edit
            System.out.println("Could not find HospitalService " + service.getName() + " to delete");
            return false;
        } else {
            String str = "DELETE FROM " + HospitalServiceTable.NAME + " WHERE " +
                    HospitalServiceTable.Cols.ID + " = '" + service.getId().toString() + "'";
            try {
                statement.execute(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not delete HospitalService: " + service.getName());
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function finds a HospitalService by id
     *
     * @param id
     * @return the HospitalService found or null if could not be found
     */
    public HospitalService getHospitalService(UUID id) {
        //query table for specific HospitalService
        String str = "SELECT * FROM " + HospitalServiceTable.NAME + " WHERE " +
                HospitalServiceTable.Cols.ID + " = '" + id.toString() + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            HospitalService tempService = null;
            while (resultSet.next()) {
                tempService = new HospitalService(resultSet.getString(HospitalServiceTable.Cols.NAME),
                        resultSet.getString(HospitalServiceTable.Cols.LOCATION));
            }
            return tempService;
        } catch (SQLException e) {
            System.out.println("Could not select Hospital Service with id: " + id.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function takes in a order by clause and generates list of all HospitalService
     * if no order is needed, order gets set to null when called
     * Default sort if of name alphabetical order
     *
     * @param order
     * @return
     */
    public ArrayList<HospitalService> getHospitalServices(String order) {
        ArrayList<HospitalService> temp = new ArrayList<>();
        try {
            String str;
            if (order == null) {
                //query all HospitalService in table without order
                str = "SELECT * FROM " + HospitalServiceTable.NAME;
            } else {
                //query all HospitalService in table with order
                str = "SELECT * FROM " + HospitalServiceTable.NAME
                        + " ORDER BY " + order + " ASC";
            }
            ResultSet resultSet = statement.executeQuery(str);

            //iterate through result, printing out values of each row
            while (resultSet.next()) {
                //get HospitalService from resultSet
                HospitalService tempService = new HospitalService(resultSet.getString(HospitalServiceTable.Cols.NAME),
                        resultSet.getString(HospitalServiceTable.Cols.LOCATION));
                temp.add(tempService); //add to array
            }
        } catch (Exception e) {
            System.out.println("Could not get all HospitalServices");
            e.printStackTrace();
        }

        return temp;
    }

    /**
     * Print entire table, used for testing
     */
    public void printAllServicesRows() {
        ArrayList<HospitalService> temp = getHospitalServices(null);
        for (HospitalService service : temp) {
            System.out.println("Name: " + service.getName() + " --- Location: " + service.getLocation());
        }
    }

    /**
     * This function populates the originalList array of all services from the Excel sheet on the 4th floor
     */
    private void populateArray() {
        //populate with originalList of services
        System.out.println("\nStoring initial Hospital Services");

        originalList.add(new HospitalService("Arthritis Center", "4D"));
        originalList.add(new HospitalService("Brigham and Women''s Primary Physicians", "4A/4S/5J"));
        originalList.add(new HospitalService("Cardiology", "4G"));
        originalList.add(new HospitalService("Doherty Conference Room", "4th Floor"));
        originalList.add(new HospitalService("Endocrinology", "4G"));
        originalList.add(new HospitalService("Gastroenterology", "4G"));
        originalList.add(new HospitalService("Gastroenterology Associates", "4B"));
        originalList.add(new HospitalService("Geriatrics/Senior Health", "4G"));
        originalList.add(new HospitalService("Headache Center", "4H"));
        originalList.add(new HospitalService("Hematology", "4G"));
        originalList.add(new HospitalService("Infectious Diseases", "4F"));
        originalList.add(new HospitalService("Interpreter Services", "4th Floor"));
        originalList.add(new HospitalService("John R. Graham Headache Center", "4H"));
        originalList.add(new HospitalService("Mary Ann Tynan Conference Rooms", "4th Floor"));
        originalList.add(new HospitalService("Medical Library", "4th Floor"));
        originalList.add(new HospitalService("Medical Records", "4th Floor"));
        originalList.add(new HospitalService("Medical Specialties", "4G"));
        originalList.add(new HospitalService("Men''s Health Center", "4N"));
        originalList.add(new HospitalService("Mohs and Dermatologic Surgery", "4J"));
        originalList.add(new HospitalService("Neurology", "4H"));
        originalList.add(new HospitalService("Neurology/Sleep Division", "4C"));
        originalList.add(new HospitalService("Pulmonary", "4G"));
        originalList.add(new HospitalService("Renal", "4G"));
        originalList.add(new HospitalService("Rheumatology Center", "4D"));
        originalList.add(new HospitalService("Sadowsky Conference Room", "4th Floor"));
        originalList.add(new HospitalService("Saslow Conference Room", "4th Floor"));
        originalList.add(new HospitalService("Social Work", "4th Floor"));
        originalList.add(new HospitalService("Urology", "4N"));

        populateTable(originalList); //put array in database now
    }

    /**
     * This function populates the database table from the array
     */
    public void populateTable(ArrayList<HospitalService> list) {
        dropTable();
        buildTable();

        for (HospitalService service : list) {
            addHospitalService(service);
        }
    }

    /**
     * dropTable checks that the db doesn't already exist, and if so drops all tables
     */
    private void dropTable() {
        System.out.println("Checking for existing tables.");
        try {
            statement = connection.createStatement(); //Statement object
            try {
                // Drop the Hospital Service table.
                String str = "DROP TABLE " + HospitalServiceTable.NAME;
                statement.execute(str); //check HospitalService table
                System.out.println("HospitalService table dropped.");
            } catch (SQLException ex) {
                //Table did not exist
            }
        } catch (SQLException e) {
            System.out.println("Could not drop HospitalService table");
            e.printStackTrace();
        }
    }

    /**
     * This is the function that will create all tables in our database
     */
    private void buildTable() {
        try {
            statement = connection.createStatement(); //Statement object

            // Create HospitalService table.
            String str = "CREATE TABLE " + HospitalServiceTable.NAME + "(" +
                    HospitalServiceTable.Cols.ID + " CHAR(100) NOT NULL PRIMARY KEY, " +
                    HospitalServiceTable.Cols.NAME + " VARCHAR(50) NOT NULL, " +
                    HospitalServiceTable.Cols.LOCATION + " VARCHAR(20) NOT NULL)";
            statement.execute(str);

            System.out.println("HospitalService table created.");
        } catch (SQLException e) {
            System.out.println("Could not build HospitalService table");
            e.printStackTrace();
        }
    }
}