package db.dbHelpers;

import db.dbClasses.HospitalProfessional;
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
    private static Statement statement;
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
            if (getHospitalProfessionals(null).isEmpty()) {
                originalList = new ArrayList<>(); //initialize empty array and populate
                //populate table
                populateArray();
            }
        } catch (SQLException e) {
            System.out.println("HospitalProfessional constructor error");
          //  e.printStackTrace();
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
                professional.getTitle() + "', '" + professional.getLocation() + "', '" +
                professional.getNodeId().toString() + "')";
        try {
            statement.executeUpdate(str);
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add HospitalProfessional: " + professional.getName());
          //  e.printStackTrace();
            return false;
        }
    }

    /**
     * Function takes in edited HospitalProfessional and updates it
     * @param professional HospitalProfessional being updated
     * @return success
     */
    public static boolean updateHospitalProfessional(HospitalProfessional professional) {
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
                    " = '" + professional.getLocation() + "', " + HospitalProfessionalTable.Cols.NODEID +
                    " = '" + professional.getNodeId().toString() + "' WHERE " + HospitalProfessionalTable.Cols.ID + " = '" +
                    professional.getId().toString() + "'";
            try {
                //update was successful
                statement.executeUpdate(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not update HospitalProfessional: " + professional.getName());
              //  e.printStackTrace();
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
              //  e.printStackTrace();
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
    public static HospitalProfessional getHospitalProfessional(UUID id) {
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
                tempProfessional.setNodeId(UUID.fromString(resultSet.getString(HospitalProfessionalTable.Cols.NODEID)));
                tempProfessional.setId(id);
            }
            return tempProfessional;
        } catch (SQLException e) {
            System.out.println("Could not select Hospital Professional with id: " + id.toString());
        //    e.printStackTrace();
        }
        return null;
    }

    /**
     * Function finds a HospitalProfessional by id
     *
     * @param name Name of HospitalProfessional
     * @return the HospitalProfessional found or null if could not be found
     */
    public static HospitalProfessional getHospitalProfessionalByName(String name) {
        //query table for specific HospitalProfessional
        String str = "SELECT * FROM " + HospitalProfessionalTable.NAME + " WHERE " +
                HospitalProfessionalTable.Cols.NAME + " = '" + name + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            HospitalProfessional tempProfessional = null;
            while(resultSet.next()){
                tempProfessional = new HospitalProfessional(name,
                        resultSet.getString(HospitalProfessionalTable.Cols.TITLE),
                        resultSet.getString(HospitalProfessionalTable.Cols.LOCATION));
                tempProfessional.setId(UUID.fromString(resultSet.getString(HospitalProfessionalTable.Cols.ID)));
                tempProfessional.setNodeId(UUID.fromString(resultSet.getString(HospitalProfessionalTable.Cols.NODEID)));
            }
            return tempProfessional;
        } catch (SQLException e) {
            System.out.println("Could not select Hospital Professional with name: " + name);
            //    e.printStackTrace();
        }
        return null;
    }

    /**
     * Function takes in a order by clause and generates list of all HospitalProfessional
     * if no order is needed, order gets set to null when called
     * Default sort if of name alphabetical order
     *
     * @param order Order By command
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
                tempProfessional.setNodeId(UUID.fromString(resultSet.getString(HospitalProfessionalTable.Cols.NODEID)));
                tempProfessional.setId(UUID.fromString(resultSet.getString(HospitalProfessionalTable.Cols.ID)));
                temp.add(tempProfessional); //add to array
            }
        } catch (Exception e) {
            System.out.println("No HospitalProfessionals are available to list");
         //   e.printStackTrace();
            return temp;
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

        // floor 4
        originalList.add(new HospitalProfessional("Ash, Samuel", "MD", "4G"));
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

        // floor 5
        originalList.add(new HospitalProfessional("Alqueza, Arnold", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Altschul, Nomee", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Andromalos, Laura ", "RD, LDN", "5D"));
        originalList.add(new HospitalProfessional("Angell, Trevor", "MD", "5D"));
        originalList.add(new HospitalProfessional("Angell, Trevor", "MD", "5K"));
        originalList.add(new HospitalProfessional("Ariagno, Meghan", "RD, LDN", "5D"));
        originalList.add(new HospitalProfessional("Balash, Eva", "MD", "5G"));
        originalList.add(new HospitalProfessional("Barr, Joseph Jr.", "MD", "5C"));
        originalList.add(new HospitalProfessional("Batool-Anwar, Salma", "MD, MPH", "5K"));
        originalList.add(new HospitalProfessional("Belkin, Michael", "MD", "5D"));
        originalList.add(new HospitalProfessional("Berman, Stephanie", "MD", "5J"));
        originalList.add(new HospitalProfessional("Bhattacharyya, Shamik", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Blazar, Phil", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Bluman, Eric", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Boatwright, Giuseppina", "MS, RD, LDN", "5K"));
        originalList.add(new HospitalProfessional("Bono, Christopher", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Brick, Gregory", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Budhiraja, Rohit", "MD", "5K"));
        originalList.add(new HospitalProfessional("Butler, Matthew", "MD", "5C"));
        originalList.add(new HospitalProfessional("Cahan, David", "MD", "5I"));
        originalList.add(new HospitalProfessional("Carleen, Mary Anne", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Chahal, Katie", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Chiodo, Christopher", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Davidson, Paul", "PhD", "5D"));
        originalList.add(new HospitalProfessional("Dawson, Courtney", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Divito, Sherrie", "MD, PhD", "5G"));
        originalList.add(new HospitalProfessional("Drew, Michael", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Dyer, George", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Earp, Brandon", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Epstein, Lawrence", "MD", "5K"));
        originalList.add(new HospitalProfessional("Ermann, Joerg", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Fitz, Wolfgang", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Frangos, Jason", "MD", "5G"));
        originalList.add(new HospitalProfessional("Groden, Joseph", "MD", "5B"));
        originalList.add(new HospitalProfessional("Groff, Michael", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Halperin, Florencia", "MD", "5K"));
        originalList.add(new HospitalProfessional("Harris, Mitchel", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Hartigan, Joseph", "DPM", "5 South"));
        originalList.add(new HospitalProfessional("Hartman, Katy", "MS, RD, LDN", "5D"));
        originalList.add(new HospitalProfessional("Healey, Michael", "MD", "5J"));
        originalList.add(new HospitalProfessional("Higgins, Laurence", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Hinton, Nadia", "RDN, LDN", "5A"));
        originalList.add(new HospitalProfessional("Horowitz, Sandra", "MD", "5K"));
        originalList.add(new HospitalProfessional("Innis, William", "MD", "5B"));
        originalList.add(new HospitalProfessional("Irani, Jennifer", "MD", "5D"));
        originalList.add(new HospitalProfessional("Isaac, Zacharia", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Isom, Kellene", "MS, RN, LDN", "5D"));
        originalList.add(new HospitalProfessional("Issa, Mohammed", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Javaheri, Sogol", "MD", "5K"));
        originalList.add(new HospitalProfessional("Johnsen, Jami", "MD", "5K"));
        originalList.add(new HospitalProfessional("Joyce, Eileen", "LICSW", "5H"));
        originalList.add(new HospitalProfessional("Kenney, Pardon", "MD", "5D"));
        originalList.add(new HospitalProfessional("Kessler, Joshua", "MD", "5B"));
        originalList.add(new HospitalProfessional("Khaodhiar, Lalita", "MD", "5K"));
        originalList.add(new HospitalProfessional("Kleifield, Allison", "PA-C", "5D"));
        originalList.add(new HospitalProfessional("Kornack, Fulton", "MD", "5C"));
        originalList.add(new HospitalProfessional("Kramer, Justine", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Laskowski, Karl", "MD", "5J"));
        originalList.add(new HospitalProfessional("Lu, Yi", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Mason, William", "MD", "5B"));
        originalList.add(new HospitalProfessional("Matthews, Robert", "PA-C", "5D"));
        originalList.add(new HospitalProfessional("Matzkin, Elizabeth", "MD", "5 South"));
        originalList.add(new HospitalProfessional("McCarthy, Rita", "NP", "5K"));
        originalList.add(new HospitalProfessional("McDonnell, Marie", "MD", "5K"));
        originalList.add(new HospitalProfessional("McKenna, Robert", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("McKitrick, Charles", "MD", "5K"));
        originalList.add(new HospitalProfessional("Melnitchouk, Neyla", "MD", "5D"));
        originalList.add(new HospitalProfessional("Miatto, Orietta", "MD", "5J"));
        originalList.add(new HospitalProfessional("Monaghan, Colleen", "MD", "5H"));
        originalList.add(new HospitalProfessional("Nehs, Matthew", "MD", "5D"));
        originalList.add(new HospitalProfessional("Nelson, Ehren", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Nuspl, Kristen", "PA-C", "5J"));
        originalList.add(new HospitalProfessional("O''Hare, Kitty", "MD", "5H"));
        originalList.add(new HospitalProfessional("Oliveira, Nancy", "MS, RDN, LDN", "5A"));
        originalList.add(new HospitalProfessional("Omobomi, Olabimpe", "MD", "5K"));
        originalList.add(new HospitalProfessional("Palermo, Nadine", "MD", "5K"));
        originalList.add(new HospitalProfessional("Paperno, Halie", "Au.D, CCC-A", "5B"));
        originalList.add(new HospitalProfessional("Pavlova, Milena", "MD", "5K"));
        originalList.add(new HospitalProfessional("Pingeton, Mallory", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Quan, Stuart", "MD", "5K"));
        originalList.add(new HospitalProfessional("Rangel, Erika", "MD", "5D"));
        originalList.add(new HospitalProfessional("Reil, Erin", "RD, LDN", "5D"));
        originalList.add(new HospitalProfessional("Robinson, Malcolm", "MD", "5D"));
        originalList.add(new HospitalProfessional("Samara, Mariah", "MD", "5B"));
        originalList.add(new HospitalProfessional("Savage, Robert", "MD", "5C"));
        originalList.add(new HospitalProfessional("Schoenfeld, Andrew", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Sharma, Niraj", "MD", "5H"));
        originalList.add(new HospitalProfessional("Sheu, Eric", "MD", "5D"));
        originalList.add(new HospitalProfessional("Shoji, Brent", "MD", "5D"));
        originalList.add(new HospitalProfessional("Smith, Colleen", "NP", "5K"));
        originalList.add(new HospitalProfessional("Smith, Jeremy", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Spector, David", "MD", "5D"));
        originalList.add(new HospitalProfessional("Stephens, Kelly", "MD", "5K"));
        originalList.add(new HospitalProfessional("Stone, Rebecca", "MD", "5B"));
        originalList.add(new HospitalProfessional("Tavakkoli, Ali", "MD", "5D"));
        originalList.add(new HospitalProfessional("Taylor, Cristin", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Tenforde, Adam", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Vernon, Ashley", "MD", "5D"));
        originalList.add(new HospitalProfessional("Vigneau, Shari", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Wagle, Neil", "MD", "5J"));
        originalList.add(new HospitalProfessional("Warth, James", "MD", "5F"));
        originalList.add(new HospitalProfessional("Warth, Maria", "MD", "5F"));
        originalList.add(new HospitalProfessional("Webber, Anthony", "MD", "5C"));
        originalList.add(new HospitalProfessional("Wellman, David", "MD", "5K"));
        originalList.add(new HospitalProfessional("White, David", "MD", "5K"));
        originalList.add(new HospitalProfessional("Whitlock, Kaitlyn", "PA-C", "5 South"));
        originalList.add(new HospitalProfessional("Yong, Jason", "MD", "5 South"));
        originalList.add(new HospitalProfessional("Zampini, Jay", "MD", "5 South"));

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
            } catch (SQLException e) {
                System.out.println("No HospitalProfessional Table to drop");
            //    e.printStackTrace();
                //Table did not exist
            }
        } catch (SQLException e) {
            System.out.println("Could not create statement in HospitalProfessional Table");
         //   e.printStackTrace();
        }
    }

    /**
     * This is the function that will create this table in our database
     */
    private void buildTable() {
        try {
            statement = connection.createStatement(); //Statement object

            // Create HospitalProfessional table.
            String str = "CREATE TABLE " + HospitalProfessionalTable.NAME + "(" +
                    HospitalProfessionalTable.Cols.ID + " VARCHAR(100) NOT NULL PRIMARY KEY, " +
                    HospitalProfessionalTable.Cols.NAME + " VARCHAR(50) NOT NULL, " +
                    HospitalProfessionalTable.Cols.TITLE + " VARCHAR(50) NOT NULL, " +
                    HospitalProfessionalTable.Cols.LOCATION + " VARCHAR(20), " +
                    HospitalProfessionalTable.Cols.NODEID + " VARCHAR(100))";

            statement.execute(str);

            System.out.println("HospitalProfessional table created.");
        } catch (SQLException e) {
            System.out.println("Could not build HospitalProfessional table");
         //   e.printStackTrace();
        }
    }
}