package db.dbHelper;

import db.HospitalProfessional;
import db.HospitalSchema.HospitalProfessionalSchema.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Gina on 3/31/17.
 */
public class HospitalProfessionalsHelper {

    private static HospitalProfessionalsHelper hospitalProfessionalsHelper;

    private Connection connection;
    private Statement statement;
    private ArrayList<HospitalProfessional> originalList;

    /**
     * Initialize HospitalProfessionalsHelper if not already created
     * It is static since there is only one table
     *
     * @param connection
     * @return
     */
    public static HospitalProfessionalsHelper get(Connection connection) {

        if (hospitalProfessionalsHelper == null) {
            hospitalProfessionalsHelper = new HospitalProfessionalsHelper(connection);
            System.out.println("Created new HospitalProfessionalsHelper");
        } else {
            System.out.println("Returning already created HospitalProfessionalsHelper");
        }
        return hospitalProfessionalsHelper;
    }

    /**
     * Constructor for HospitalProfessionalsHelper
     * Populates database if first time calling
     *
     * @param connection
     */
    private HospitalProfessionalsHelper(Connection connection) {
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
     * Add a HospitalProfessional to the database
     *
     * @param professional
     * @return success
     */
    public boolean addHospitalProfessional(HospitalProfessional professional) {
        //insert HospitalProfessional into table
        String str = "INSERT INTO " + HospitalProfessionalTable.NAME + " VALUES (" +
                "'" + professional.getId().toString() + "', '" + professional.getName() + "', '" +
                professional.getTitle() + "', '" + professional.getLocation() + "')";
        try {
            statement.executeUpdate(str);
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add HospitalProfessional: " + professional.getName());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function takes in edited HospitalProfessional and updates it
     * @param professional HospitalProfessional being updated
     * @return success
     */
    public boolean updateHospitalProfessional(HospitalProfessional professional) {
        //check table to make sure professional is already there
        HospitalProfessional temp = getHospitalProfessional(professional.getId());
        if (temp == null) { //could not find HospitalProfessional to edit
            System.out.println("Could not find HospitalProfessional " + professional.getName() + " to update");
            return false;
        } else {
            //updating
            String str = "UPDATE " + HospitalProfessionalTable.NAME + " SET " + HospitalProfessionalTable.Cols.NAME +
                    " = '" + professional.getName() + "', " + HospitalProfessionalTable.Cols.TITLE +
                    " = '" + professional.getTitle() + "', " + HospitalProfessionalTable.Cols.LOCATION +
                    " = '" + professional.getLocation() + "' WHERE " + HospitalProfessionalTable.Cols.ID + " = '" +
                    professional.getId().toString() + "'";
            try {
                //update was successful
                statement.executeUpdate(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not update HospitalProfessional: " + professional.getName());
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function that takes in a professional and deletes it from the database
     * @param professional HospitalProfessional
     * @return success
     */
    public boolean deleteHospitalProfessional(HospitalProfessional professional) {
        //check table to make sure HospitalProfessional is already there
        HospitalProfessional temp = getHospitalProfessional(professional.getId());
        if (temp == null) { //could not find HospitalProfessional to edit
            System.out.println("Could not find HospitalProfessional " + professional.getName() + " to delete");
            return false;
        } else {
            String str = "DELETE FROM " + HospitalProfessionalTable.NAME + " WHERE " +
                    HospitalProfessionalTable.Cols.ID + " = '" + professional.getId().toString() + "'";
            try {
                statement.execute(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not delete HospitalProfessional: " + professional.getName());
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function finds a HospitalProfessional by id
     *
     * @param id ID of HospitalProfessional
     * @return the HospitalProfessional found or null if could not be found
     */
    public HospitalProfessional getHospitalProfessional(UUID id) {
        //query table for specific HospitalProfessional
        String str = "SELECT * FROM " + HospitalProfessionalTable.NAME + " WHERE " +
                HospitalProfessionalTable.Cols.ID + " = '" + id.toString() + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            HospitalProfessional tempProfessional = null;
            while(resultSet.next()){
                tempProfessional = new HospitalProfessional(resultSet.getString(HospitalProfessionalTable.Cols.NAME),
                        resultSet.getString(HospitalProfessionalTable.Cols.TITLE),
                        resultSet.getString(HospitalProfessionalTable.Cols.LOCATION));
            }
            return tempProfessional;
        } catch (SQLException e) {
            System.out.println("Could not select Hospital Professional with id: " + id.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function takes in a order by clause and generates list of all HospitalProfessional
     * if no order is needed, order gets set to null when called
     * Default sort if of name alphabetical order
     *
     * @param order
     * @return list of HospitalProfessionals
     */
    public ArrayList<HospitalProfessional> getHospitalProfessionals(String order) {
        ArrayList<HospitalProfessional> temp = new ArrayList<>();
        try {
            String str;
            if (order == null) {
                //query all HospitalProfessionals in table without order
                str = "SELECT * FROM " + HospitalProfessionalTable.NAME;
            } else {
                //query all HospitalProfessionals in table with order
                str = "SELECT * FROM " + HospitalProfessionalTable.NAME
                        + " ORDER BY " + order + " ASC";
            }
            ResultSet resultSet = statement.executeQuery(str);

            //iterate through result, printing out values of each row
            while (resultSet.next()) {
                //get HospitalProfessional from resultSet
                HospitalProfessional tempProfessional = new HospitalProfessional(resultSet.getString(HospitalProfessionalTable.Cols.NAME),
                        resultSet.getString(HospitalProfessionalTable.Cols.TITLE),
                        resultSet.getString(HospitalProfessionalTable.Cols.LOCATION));
                temp.add(tempProfessional); //add to array
            }
        } catch (Exception e) {
            System.out.println("Could not get all HospitalProfessionals");
            e.printStackTrace();
        }

        return temp;
    }

    /**
     * Print entire table, used for testing
     */
    public void printAllProfessionalRows() {
        ArrayList<HospitalProfessional> temp = getHospitalProfessionals(null);
        for (HospitalProfessional professional : temp) {
            System.out.println("Name: " + professional.getName() +
                    ", " + professional.getTitle() +
                    " --- Location: " + professional.getLocation());
        }
    }

    /**
     * This function populates the originalList array of all professionals from the Excel sheet on the 4th floor
     */
    private void populateArray() {
        //populate with originalList of professionals
        System.out.println("\nStoring initial Hospital Professionals");

        originalList.add(new HospitalProfessional("Bachman, William", "MD", "4G"));
        originalList.add(new HospitalProfessional("Bernstein, Carolyn", "MD", "4H"));
        originalList.add(new HospitalProfessional("Bhasin, Shalender", "MD", "4N"));
        originalList.add(new HospitalProfessional("Bonaca, Marc", "MD", "4G"));
        originalList.add(new HospitalProfessional("Burch, Rebecca", "MD", "4H"));
        originalList.add(new HospitalProfessional("Caplan, Laura", "PA-C", "4A"));
        originalList.add(new HospitalProfessional("Cardet, Juan Carlos", "MD", "4G"));
        originalList.add(new HospitalProfessional("Cardin, Kristin", "NP", "4G"));
        originalList.add(new HospitalProfessional("Chan, Walter", "MD", "4G"));
        originalList.add(new HospitalProfessional("Clark, Roger", "DO", "4F"));
        originalList.add(new HospitalProfessional("Cochrane, Thomas", "MD", "4H"));
        originalList.add(new HospitalProfessional("Conant, Alene", "MD", "4B"));
        originalList.add(new HospitalProfessional("Connell, Nathan", "MD", "4G"));
        originalList.add(new HospitalProfessional("Copello, Maria", "MD", "4A"));
        originalList.add(new HospitalProfessional("Cua, Christopher", "MD", "4I"));
        originalList.add(new HospitalProfessional("D''Ambrosio, Carolyn", "MD", "4G"));
        originalList.add(new HospitalProfessional("Dave, Jatin", "MD", "4G"));
        originalList.add(new HospitalProfessional("Drewniak, Stephen", "MD", "4B"));
        originalList.add(new HospitalProfessional("Fanta, Christopher", "MD", "4G"));
        originalList.add(new HospitalProfessional("Friedman, Pamela", "PsyD, ABPP", "4H"));
        originalList.add(new HospitalProfessional("Goldman, Jill", "MD", "4S"));
        originalList.add(new HospitalProfessional("Healy, Barbara", "RN", "4A"));
        originalList.add(new HospitalProfessional("Hentschel, Dirk", "MD", "4G"));
        originalList.add(new HospitalProfessional("Homenko, Daria", "MD", "4B"));
        originalList.add(new HospitalProfessional("Hoover, Paul", "MD, PhD", "4D"));
        originalList.add(new HospitalProfessional("Hsu, Joyce", "MD", "4G"));
        originalList.add(new HospitalProfessional("Kathrins, Martin", "MD", "4N"));
        originalList.add(new HospitalProfessional("Lahive, Karen", "MD", "4I"));
        originalList.add(new HospitalProfessional("Lauretti, Linda", "MD", "4A"));
        originalList.add(new HospitalProfessional("Lilienfeld, Armin", "MD", "4S"));
        originalList.add(new HospitalProfessional("Lilly, Leonard Stuart", "MD", "4G"));
        originalList.add(new HospitalProfessional("Lo, Amy", "MD", "4B"));
        originalList.add(new HospitalProfessional("Loder, Elizabeth", "MD", "4H"));
        originalList.add(new HospitalProfessional("Malone, Michael", "MD", "4N"));
        originalList.add(new HospitalProfessional("Mathew, Paul", "MD", "4H"));
        originalList.add(new HospitalProfessional("Matloff, Daniel", "MD", "4B"));
        originalList.add(new HospitalProfessional("McDonald, Michael", "MD", "4N"));
        originalList.add(new HospitalProfessional("McGowan, Katherine", "MD", "4F"));
        originalList.add(new HospitalProfessional("McMahon, Gearoid", "MD", "4G"));
        originalList.add(new HospitalProfessional("McNabb-Balter, Julia", "MD", "4B"));
        originalList.add(new HospitalProfessional("Mullally, William", "MD", "4C"));
        originalList.add(new HospitalProfessional("Mutinga, Muthoka", "MD", "4B"));
        originalList.add(new HospitalProfessional("Novak, Peter", "MD", "4C"));
        originalList.add(new HospitalProfessional("O''Leary, Michael", "MD", "4N"));
        originalList.add(new HospitalProfessional("Oliver, Lynn", "RN", "4A"));
        originalList.add(new HospitalProfessional("Owens, Lisa Michelle", "MD", "4S"));
        originalList.add(new HospitalProfessional("Pariser, Kenneth", "MD", "4D"));
        originalList.add(new HospitalProfessional("Parnes, Aric", "MD", "4G"));
        originalList.add(new HospitalProfessional("Pilgrim, David", "MD", "4C"));
        originalList.add(new HospitalProfessional("Preneta, Ewa", "MD", "4B"));
        originalList.add(new HospitalProfessional("Ramirez, Alberto", "MD", "4G"));
        originalList.add(new HospitalProfessional("Rizzoli, Paul", "MD", "4H"));
        originalList.add(new HospitalProfessional("Romano, Keith", "MD", "4G"));
        originalList.add(new HospitalProfessional("Ruff, Christian", "MD", "4G"));
        originalList.add(new HospitalProfessional("Ruiz, Emily", "MD", "4J"));
        originalList.add(new HospitalProfessional("Saldana, Fidencio", "MD", "4G"));
        originalList.add(new HospitalProfessional("Schissel, Scott", "MD", "4G"));
        originalList.add(new HospitalProfessional("Schmults, Chrysalyne", "MD", "4J"));
        originalList.add(new HospitalProfessional("Shah, Amil", "MD", "4G"));
        originalList.add(new HospitalProfessional("Sheth, Samira", "NP", "4G"));
        originalList.add(new HospitalProfessional("Smith, Benjamin", "MD", "4B"));
        originalList.add(new HospitalProfessional("Steele, Graeme", "MD", "4N"));
        originalList.add(new HospitalProfessional("Sweeney, Michael", "MD", "4G"));
        originalList.add(new HospitalProfessional("Tarpy, Robert", "MD", "4I"));
        originalList.add(new HospitalProfessional("Todd, Derrick", "MD, PhD", "4D"));
        originalList.add(new HospitalProfessional("Tucker, Kevin", "MD", "4G"));
        originalList.add(new HospitalProfessional("Vardeh, Daniel", "MD", "4C"));
        originalList.add(new HospitalProfessional("Voiculescu, Adina", "MD", "4G"));
        originalList.add(new HospitalProfessional("Waldman, Abigail", "MD", "4J"));
        originalList.add(new HospitalProfessional("Walsh Samp, Kathy", "LICSW", "4A"));
        originalList.add(new HospitalProfessional("Weisholtz, Daniel", "MD", "4C"));
        originalList.add(new HospitalProfessional("Welker, Roy", "MD", "4A"));
        originalList.add(new HospitalProfessional("Whitman, Gregory", "MD", "4C"));
        originalList.add(new HospitalProfessional("Wickner, Paige", "MD", "4G"));

        populateTable(originalList); //put array in database now
    }

    /**
     * This function populates the database table from the array
     * @param list
     */
    public void populateTable(ArrayList<HospitalProfessional> list) {
        dropTable();
        buildTable();

        for (HospitalProfessional professional : list) {
            addHospitalProfessional(professional);
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
                // Drop the HospitalProfessional table.
                String str = "DROP TABLE " + HospitalProfessionalTable.NAME;
                statement.execute(str); //check HospitalProfessionals table
                System.out.println("HospitalProfessionals table dropped.");
            } catch (SQLException ex) {
                //Table did not exist
            }
        } catch (SQLException e) {
            System.out.println("Could not drop HospitalProfessionals table");
            e.printStackTrace();
        }
    }

    /**
     * This is the function that will create all tables in our database
     */
    private void buildTable() {
        try {
            statement = connection.createStatement(); //Statement object

            // Create HospitalProfessional table.
            String str = "CREATE TABLE " + HospitalProfessionalTable.NAME + "(" +
                    HospitalProfessionalTable.Cols.ID + " CHAR(100) NOT NULL PRIMARY KEY, " +
                    HospitalProfessionalTable.Cols.NAME + " VARCHAR(50) NOT NULL, " +
                    HospitalProfessionalTable.Cols.TITLE + " VARCHAR(50) NOT NULL, " +
                    HospitalProfessionalTable.Cols.LOCATION + " VARCHAR(20) )";

            statement.execute(str);

            System.out.println("HospitalProfessional table created.");
        } catch (SQLException e) {
            System.out.println("Could not build HospitalProfessional table");
            e.printStackTrace();
        }
    }
}