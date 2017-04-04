package db;

import db.dbHelpers.EdgesHelper;
import db.dbHelpers.HospitalProfessionalsHelper;
import db.dbHelpers.HospitalServicesHelper;
import db.dbHelpers.NodesHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Gina on 3/27/17.
 */
public class Driver {

    private static HospitalServicesHelper hospitalServicesHelper;
    private static HospitalProfessionalsHelper hospitalProfessionalsHelper;
    private static NodesHelper nodesHelper;
    private static EdgesHelper edgesHelper;

    public static void main(String[] args) {
        registerDriver(true);
        runDatabase(true);
    }

    public static void registerDriver(boolean print){
        if(print) {
            System.out.println("-------Embedded Java DB Connection Testing --------");
        }
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            if(print) {
                System.out.println("Java DB db.Driver not found. Add the classpath to your module.");
                System.out.println("For IntelliJ do the following:");
                System.out.println("File | Project Structure, Modules, Dependency tab");
                System.out.println("Add by clicking on the green plus icon on the right of the window");
                System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
                System.out.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
                System.out.println("Click OK, compile the code and run it.");

                e.printStackTrace();
            }
            return;
        }
        if(print) {
            System.out.println("Java DB db.Driver registered!");
        }
    }

    public static void runDatabase(boolean print){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:derby:faulknerDatabase;create=True");

        } catch (SQLException e) {
            if(print) {
                System.out.println("Connection failed. Check output console.");
                e.printStackTrace();
            }
            return;
        }
        if(print) {
            System.out.println("Java DB connection established!");
        }

        hospitalServicesHelper = HospitalServicesHelper.get(connection);
        hospitalProfessionalsHelper = HospitalProfessionalsHelper.get(connection);

        //Nodes must be created before edges since the edges table references the node table
        nodesHelper = NodesHelper.get(connection);
        edgesHelper = EdgesHelper.get(connection);

    }

    //getter for all other classes to access the HospitalService table
    public static HospitalServicesHelper getHospitalServiceHelper() {
        return hospitalServicesHelper;
    }

    //getter for all other classes to access the HospitalProfessional table
    public static HospitalProfessionalsHelper getHospitalProfessionalHelper() {
        return hospitalProfessionalsHelper;
    }

    //getter for all other classes to access the Edges table
    public static EdgesHelper getEdgesHelper() {
        return edgesHelper;
    }

    //getter for all other classes to access the Nodes table
    public static NodesHelper getNodesHelper() {
        return nodesHelper;
    }

}

