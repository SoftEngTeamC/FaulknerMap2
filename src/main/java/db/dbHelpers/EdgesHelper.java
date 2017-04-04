package db.dbHelpers;


import db.HospitalSchema.EdgeSchema.*;
import db.dbClasses.Edge;
import db.dbClasses.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Gina on 3/31/17.
 */
public class EdgesHelper {

    private static EdgesHelper edgesHelper;

    private static Connection connection;
    private static Statement statement;
    private static ArrayList<Edge> originalList;

    /**
     * Initialize EdgesHelper if not already created
     * We only need one instance of it since there is only one table
     *
     * @param connection
     * @return
     */
    public static EdgesHelper get(Connection connection) {

        if (edgesHelper == null) {
            edgesHelper = new EdgesHelper(connection);
            System.out.println("Created new EdgesHelper");
        }
        return edgesHelper;
    }

    /**
     * Constructor for EdgesHelper
     * Populates database if first time calling
     *
     * @param connection
     */
    private EdgesHelper(Connection connection) {
        this.connection = connection;

        try {
            statement = connection.createStatement();

//            //check if table is empty
//            if (getEdges(null).isEmpty()) {
//                originalList = new ArrayList<>(); //initialize empty array and populate
//                //populate table
//                populateArray();
//            }
        } catch (SQLException e) {
            System.out.println("Edge constructor error");
            //   e.printStackTrace();
        }
    }

    /**
     * Add a Edge to the database
     *
     * @param edge New Edge
     * @return success
     */
    public static boolean addEdge(Edge edge) {
        //insert Edge into table
        String str = "INSERT INTO " + EdgeTable.NAME + " VALUES (" +
                "'" + edge.getId().toString() + "', '" + edge.getFrom().getId().toString() +
                "', '" + edge.getTo().getId().toString() + "', " + edge.getLength() +
                ", '" + edge.getDisabled()
                + "')";
        try {
            statement.executeUpdate(str);
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add Edge: " + edge.toString());
            //  e.printStackTrace();
            return false;
        }
    }

    /**
     * Function takes in edited Edge and updates it
     *
     * @param edge
     * @return success
     */
    public boolean updateEdge(Edge edge) {
        //check table to make sure edge is already there
        Edge temp = getEdgeByID(edge.getId());
        if (temp == null) { //could not find edge to edit
            System.out.println("Could not find Edge " + edge.toString() + " to update");
            return false;
        } else {
            //updating
            String str = "UPDATE " + EdgeTable.NAME + " SET " + EdgeTable.Cols.ID +
                    " = '" + edge.getId().toString() + "', " + EdgeTable.Cols.FROM_NODE +
                    " = '" + edge.getFrom().getId().toString() + "', " + EdgeTable.Cols.TO_NODE +
                    " = '" + edge.getTo().getId().toString() + "', " + EdgeTable.Cols.LENGTH +
                    " = '" + edge.getLength() + "', " + EdgeTable.Cols.DISABLED +
                    " = '" + edge.getDisabled() + "' WHERE " + EdgeTable.Cols.ID + " = '" +
                    edge.getId().toString() + "'";
            try {
                statement.executeUpdate(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not update Edge: " + edge.toString());
                //  e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function that takes in a edge and deletes it from the database
     *
     * @param edge Edge
     * @return success
     */
    public boolean deleteEdge(Edge edge) {
        //check table to make sure edge is already there
        Edge temp = getEdgeByID(edge.getId());
        if (temp == null) { //could not find edge to edit
            System.out.println("Could not find Edge " + edge.toString() + " to delete");
            return false;
        } else {
            String str = "DELETE FROM " + EdgeTable.NAME + " WHERE " +
                    EdgeTable.Cols.ID + " = '" + edge.getId().toString() + "'";
            try {
                statement.execute(str);
                return true;
            } catch (SQLException e) {
                System.out.println("Could not delete Edge: " + edge.toString());
                //   e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function finds a Edge by id
     *
     * @param id
     * @return the Edge found or null if could not be found
     */
    public Edge getEdgeByID(UUID id) {
        //query table for specific Edge
        String str = "SELECT * FROM " + EdgeTable.NAME + " WHERE " +
                EdgeTable.Cols.ID + " = '" + id.toString() + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            Edge tempEdge = null;
            while (resultSet.next()) {
                Node from = NodesHelper.getNodeByID(UUID.fromString(resultSet.getString(EdgeTable.Cols.FROM_NODE)));
                Node to = NodesHelper.getNodeByID(UUID.fromString(resultSet.getString(EdgeTable.Cols.TO_NODE)));
                tempEdge = new Edge(from, to, resultSet.getFloat(EdgeTable.Cols.LENGTH));
                tempEdge.setDisabled(resultSet.getBoolean(EdgeTable.Cols.DISABLED));
            }
            return tempEdge;
        } catch (SQLException e) {
            System.out.println("Could not select edge with id: " + id.toString());
            //  e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> temp = new ArrayList<>();
        try {
            String str;
            //query all Edges in table without order
            str = "SELECT * FROM " + EdgeTable.NAME + " WHERE " +
                    EdgeTable.Cols.FROM_NODE + " = '" + node.getId().toString() + "'";

            ResultSet resultSet = statement.executeQuery(str);

            //iterate through result
            while (resultSet.next()) {
                //get neighbor Nodes from resultSet
                Node neighbor = NodesHelper.getNodeByID(UUID.fromString(resultSet.getString(EdgeTable.Cols.TO_NODE)));
                temp.add(neighbor); //add to array
            }
        } catch (Exception e) {
            System.out.println("No neighbors where found for node : " + node.toString());
            //  e.printStackTrace();
            return temp;
        }

        return temp;

    }


    /**
     * Function takes in a order by clause and generates list of all Edges
     * if no order is needed, order gets set to null when called
     * Default sort of Edge ID order
     *
     * @param order Order to be added to list
     * @return List of all Edge
     */
    public ArrayList<Edge> getEdges(String order) {
        ArrayList<Edge> temp = new ArrayList<>();
        try {
            String str;
            if (order == null) {
                //query all Edges in table without order
                str = "SELECT * FROM " + EdgeTable.NAME;
            } else {
                //query all Edges in table with order
                str = "SELECT * FROM " + EdgeTable.NAME
                        + " ORDER BY " + order + " ASC";
            }
            ResultSet resultSet = statement.executeQuery(str);

            //iterate through result
            while (resultSet.next()) {
                //get Edge from resultSet
                Node from = NodesHelper.getNodeByID(UUID.fromString(resultSet.getString(EdgeTable.Cols.FROM_NODE)));
                Node to = NodesHelper.getNodeByID(UUID.fromString(resultSet.getString(EdgeTable.Cols.TO_NODE)));

                Edge tempEdge = new Edge(from, to, resultSet.getFloat(EdgeTable.Cols.LENGTH));
                tempEdge.setDisabled(resultSet.getBoolean(EdgeTable.Cols.DISABLED));
                tempEdge.setId(UUID.fromString(resultSet.getString(EdgeTable.Cols.ID)));
                temp.add(tempEdge); //add to array
            }
        } catch (Exception e) {
            System.out.println("No Edges are available to list");
            //  e.printStackTrace();
        }

        return temp;
    }

    /**
     * Print entire table, used for testing
     */
    public void printAllEdges() {
        ArrayList<Edge> temp = getEdges(null);
        for (Edge edge : temp) {
            System.out.println(edge.toString());
        }
    }

    /**
     * This function populates the originalList array of all Edges
     */
    private void populateArray() {
        //populate with originalList of edges
        System.out.println("\nStoring initial Edges");

        populateTable(originalList); //put array in database now
    }

    /**
     * This function populates the database table from the array
     */
    public void populateTable(ArrayList<Edge> list) {
        dropTable();
        buildTable();

        for (Edge edge : list) {
            addEdge(edge);

        }
    }

    /**
     * dropTable checks that the db doesn't already exist, and if so drops all tables
     */
    protected static void dropTable() {
        System.out.println("Checking for existing tables.");
        try {
            statement = connection.createStatement(); //Statement object
            try {
                // Drop the Edge table.
                String str = "DROP TABLE " + EdgeTable.NAME;
                statement.execute(str); //check Edge table
                System.out.println("Edge table dropped.");
            } catch (SQLException e) {
                System.out.println("No Edge table to drop");
                //   e.printStackTrace();
                //Table did not exist
            }
        } catch (SQLException e) {
            System.out.println("Could not create statement in Edge Table");
            e.printStackTrace();
        }
    }

    /**
     * This is the function that will create the actual table
     */
    protected static void buildTable() {
        try {
            statement = connection.createStatement(); //Statement object

            // Create Edge table.
            String str = "CREATE TABLE " + EdgeTable.NAME + "(" +
                    EdgeTable.Cols.ID + " VARCHAR(100) NOT NULL PRIMARY KEY, " +
                    EdgeTable.Cols.FROM_NODE + " VARCHAR(100) NOT NULL, " +
                    EdgeTable.Cols.TO_NODE + " VARCHAR(100) NOT NULL, " +
                    EdgeTable.Cols.LENGTH + " FLOAT NOT NULL, " +
                    EdgeTable.Cols.DISABLED + " BOOLEAN NOT NULL " +
//                    "CONSTRAINT " + EdgeTable.Constraints.FROM_NODE_CON + " FOREIGN KEY (" +
//                    EdgeTable.Cols.FROM_NODE + ") REFERENCES " + NodeTable.NAME + "("
//                    + NodeTable.Cols.ID + "), " +
//                    "CONSTRAINT " + EdgeTable.Constraints.TO_NODE_CON + " FOREIGN KEY (" +
//                    EdgeTable.Cols.TO_NODE + ") REFERENCES " + NodeTable.NAME + "("
//                    + NodeTable.Cols.ID + "))";
                    ")";

            statement.execute(str);

            System.out.println("Edge table created.");
        } catch (SQLException e) {
            System.out.println("Could not build Edge table");
            // e.printStackTrace();
        }
    }

}