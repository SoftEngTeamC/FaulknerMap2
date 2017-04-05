package db.dbHelpers;


import db.HospitalSchema.NodeSchema.NodeTable;
import db.dbClasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import static db.dbHelpers.HospitalProfessionalsHelper.*;
import static db.dbHelpers.HospitalServicesHelper.*;

/**
 * Created by Gina on 3/31/17.
 */
public class NodesHelper {

    private static NodesHelper nodesHelper;

    private static Connection connection;
    private static Statement statement;
    private ArrayList<Node> originalList;

    /**
     * Initialize NodesHelper if not already created
     * We only need one instance of it since there is only one table
     *
     * @param connection
     * @return
     */
    public static NodesHelper get(Connection connection) {

        if (nodesHelper == null) {
            nodesHelper = new NodesHelper(connection);
            System.out.println("Created new NodesHelper");
        }
        return nodesHelper;
    }

    /**
     * Constructor for NodesHelper
     * Populates database if first time calling
     *
     * @param connection
     */
    private NodesHelper(Connection connection) {
        this.connection = connection;
        try {
            statement = connection.createStatement();

            //check if table is empty
            if (getNodes(null).isEmpty()) {
                originalList = new ArrayList<>(); //initialize empty array and populate
                //populate table
                populateArray();
            }
        } catch (SQLException e) {
            System.out.println("Node constructor error");
         //   e.printStackTrace();
        }
    }

    /**
     * Add a Node to the database
     *
     * @param node
     * @return success
     */
    public boolean addNode(Node node) {
        //insert Node into table
        String str = "INSERT INTO " + NodeTable.NAME + " VALUES (" +
                "'" + node.getId().toString() + "', '" + node.getName() +
                "', " + node.getPosition().getXpos() + ", " +
                node.getPosition().getYpos() + ", " + node.getPosition().getZpos()
                + ")";
        try {
            statement.executeUpdate(str);
            updateNodes();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add Node " + node.getName() + ": " + node.getPosition().toString());
         //  e.printStackTrace();
            return false;
        }
    }

    /**
     * Function takes in edited Node and updates it
     *
     * @param node
     * @return success
     */
    public boolean updateNode(Node node) {
        //check table to make sure node is already there
        Node temp = getNodeByID(node.getId());
        if (temp == null) { //could not find node to edit
            System.out.println("Could not find Node " + node.getName() + ": " +
                    node.getPosition().toString() + " to update");
            return false;
        } else {
            //updating
            String str = "UPDATE " + NodeTable.NAME + " SET " + NodeTable.Cols.ID +
                    " = '" + node.getId().toString() + "', " + NodeTable.Cols.NAME +
                    " = '" + node.getName() + "', " + NodeTable.Cols.X +
                    " = '" + node.getPosition().getXpos() + "', " + NodeTable.Cols.Y +
                    " = '" + node.getPosition().getYpos() + "', " + NodeTable.Cols.Z +
                    " = '" + node.getPosition().getZpos() + "' WHERE " + NodeTable.Cols.ID + " = '" +
                    node.getId().toString() + "'";
            try {
                statement.executeUpdate(str);
                updateNodes();
                return true;
            } catch (SQLException e) {
                System.out.println("Could not update Node " + node.getName() + ": " +
                        node.getPosition().toString());
            //    e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function that takes in a node and deletes it from the database
     *
     * @param node Node
     * @return success
     */
    public static boolean deleteNode(Node node) {
        //check table to make sure node is already there
        Node temp = getNodeByID(node.getId());
        if (temp == null) { //could not find node to edit
            System.out.println("Could not find Node " + node.getName() + ": " +
                    node.getPosition().toString() + " to delete");
            return false;
        } else {
            String str = "DELETE FROM " + NodeTable.NAME + " WHERE " +
                    NodeTable.Cols.ID + " = '" + node.getId().toString() + "'";
            try {
                statement.execute(str);
                updateNodes();
                return true;
            } catch (SQLException e) {
                System.out.println("Could not delete Node " + node.getName() + ": " +
                node.getPosition().toString());
            //    e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Function finds a Node by id
     *
     * @param id
     * @return the Node found or null if could not be found
     */
    public static Node getNodeByID(UUID id) {
        //query table for specific Node
        String str = "SELECT * FROM " + NodeTable.NAME + " WHERE " +
                NodeTable.Cols.ID + " = '" + id.toString() + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            Node tempNode = null;
            while (resultSet.next()) {
                Coordinate tempCoor = new Coordinate(resultSet.getFloat(NodeTable.Cols.X),
                        resultSet.getFloat(NodeTable.Cols.Y), resultSet.getInt(NodeTable.Cols.Z));
                tempNode = new Node(null, tempCoor, resultSet.getString(NodeTable.Cols.NAME));
                tempNode.setId(UUID.fromString(resultSet.getString(NodeTable.Cols.ID)));
            }
            return tempNode;
        } catch (SQLException e) {
            System.out.println("Could not find Node with id: " + id.toString());
         //   e.printStackTrace();
        }
        return null;
    }

    /**
     * Function finds a Node by it's name
     *
     * @param name Name of node
     * @return the Node found or null if could not be found
     */
    public static Node getNodeByName(String name) {
        //query table for specific Node
        String str = "SELECT * FROM " + NodeTable.NAME + " WHERE " +
                NodeTable.Cols.NAME + " = '" + name + "'";
        try {
            ResultSet resultSet = statement.executeQuery(str);
            Node tempNode = null;
            while (resultSet.next()) {
                Coordinate tempCoor = new Coordinate(resultSet.getFloat(NodeTable.Cols.X),
                        resultSet.getFloat(NodeTable.Cols.Y), resultSet.getInt(NodeTable.Cols.Z));
                tempNode = new Node(null, tempCoor, name);
                tempNode.setId(UUID.fromString(resultSet.getString(NodeTable.Cols.ID)));
            }
            return tempNode;
        } catch (SQLException e) {
            System.out.println("Could not find Node with name: " + name);
            //   e.printStackTrace();
        }
        return null;
    }

    /**
     * Function takes in a order by clause and generates list of all Nodes
     * if no order is needed, order gets set to null when called
     * Default sort of name alphabetical order
     *
     * @param order Order to be added to list
     * @return List of all Nodes
     */
    public static ArrayList<Node> getNodes(String order) {
        ArrayList<Node> temp = new ArrayList<>();
        try {
            String str;
            if (order == null) {
                //query all Node in table with default order of name value
                str = "SELECT * FROM " + NodeTable.NAME
                        + " ORDER BY " + NodeTable.Cols.NAME + " ASC";
            } else {
                //query all Node in table with order
                str = "SELECT * FROM " + NodeTable.NAME
                        + " ORDER BY " + order + " ASC";
            }
            ResultSet resultSet = statement.executeQuery(str);

            //iterate through result, printing out values of each row
            while (resultSet.next()) {
                //get Node from resultSet
                Coordinate tempCoor = new Coordinate(resultSet.getFloat(NodeTable.Cols.X),
                        resultSet.getFloat(NodeTable.Cols.Y), resultSet.getInt(NodeTable.Cols.Z));
                Node tempNode = new Node(null, tempCoor, resultSet.getString(NodeTable.Cols.NAME));
                tempNode.setId(UUID.fromString(resultSet.getString(NodeTable.Cols.ID)));
                temp.add(tempNode); //add to array
            }
        } catch (Exception e) {
            System.out.println("No Nodes available to list");
           // e.printStackTrace();
          //  return temp;
        }

       return temp;
    }

    /**
     * Print entire table, used for testing
     */
    public void printAllNodes() {
        ArrayList<Node> temp = getNodes(null);
        for (Node node : temp) {
            System.out.println(node.getName() + ": " + node.getPosition().toString());
        }
    }

    /**
     * This function populates the originalList array of all Nodes
     */
    private void populateArray() {
        //populate with originalList of nodes
        System.out.println("\nStoring initial Nodes");

        ArrayList<Edge> edgeList = new ArrayList<>();

        //Example of how to add a Node:    TODO: delete this example when actually populating
//        Node tempFrom = new Node(null, new Coordinate(1, 2, 3), "node1");
//        Node tempTo = new Node(null, new Coordinate(1, 2, 3), "node1");
//        Node tempTo2 = new Node(null, new Coordinate(1, 2, 3), "node1");
//        Edge edge = new Edge(tempFrom,tempTo, 10);
//        edgeList.add(edge);
//        edge = new Edge(tempFrom, tempTo2, 5);
//        originalList.add(tempFrom);
//        originalList.add(tempTo);
/******************************

Here's how all the things are connected:

##Left
271 1090 MEDREC
405 1090 UROLOGY
441 1090 tynanConf
653 1085 o_44m4and44s3
783 1085 dptmedoff
810 1085 o_4211
877 1085 MEDLIB
896 1085 RAD_ADMIN
964 1085 CASMNGMT
987 1085 CONF713
##MIDDLE
1142 1085 ADMINEXE

##Right
1284 1085 o_4102-4
1327 1085 PATSAFE
1407 1085 SURGndPHYS
1537 1085 qual_spec
1620 1085 o_41m1-43s2
1675 1085 HRndCOMP
1721 1085 FINANCE
1760 1085 CLINnd4308
1808 1085 LOCKnd4311
1871 1085 PLASTICS
1907 1085 FOOT_ANLKE
1967 1085 FAC_FIN_IS
2039 1085 o_4329-31-21

##UP
1142 1026 CENTRAL
	1084 1026 o_4-5
	1194 1026 o_1-2-3
1142 922 LUNG
1142 856 HSKPG
1142 796 NURSESPACE
1142 770 SUITE4349-4995
1142 696 SUITE4985-90
1142 601 SUITE4970
1142 523 SUITE4930-35
1142 457 SUITE4950-55

##Around the top
1142 395 upperMiddle
1235 395 SUITE48
1235 237 SUITE47
1194 216 SUITE45
1142 214 TOPMIDDLE
1085 214 SUITE43
1051 236 S43B
1051 348 SUITE40
1051 395 UpLL_Corner

1142 307 o_4c68

Connect upperMiddle to o_4c68 to TOPMIDDLE
Connect upperMiddle to UpLL_Corner

*******************************/
        Node tempA = new Node(null, new Coordinate(271,1090,4),"MEDREC");
        Node tempB = new Node(null, new Coordinate(405,1090,4), "UROLOGY");
        Node tempC = new Node(null, new Coordinate(441, 1090, 4), "tynanConf");
        Node tempUpperMiddle;
        Node tempTopMiddle;
        Edge edge = new Edge(tempA, tempB,134);
        originalList.add(tempA);
        originalList.add(tempB);
        originalList.add(tempC);
        edgeList.add(edge);
        Edge edge2 = new Edge(tempB, tempC, 36);
        edgeList.add(edge2);

        /*
        HospitalProfessional temphs = getHospitalProfessionalByName("Ash, Samuel");
        temphs.setNodeId(tempA.getId());
        updateHospitalProfessional(temphs); */

        tempA = new Node(null, new Coordinate(653,1085,4),"o_44m4and44s3");
        edge = new Edge(tempC,tempA,204);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(783,1085,4), "dptmedoff");
        edge2 = new Edge(tempA, tempB, 130);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(810, 1085, 4),"o_4211");
        edge = new Edge(tempB, tempC, 27);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(877,1085,4),"MEDLIB");
        edge2 = new Edge(tempC,tempA,67);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(896, 1085, 4),"RAD_ADMIN");
        edge = new Edge(tempA, tempB,19);
        edgeList.add(edge);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(964, 1085, 4), "CASMNGMT");
        edge2 = new Edge(tempB, tempC, 68);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(987,1085,4),"CONF713");
        edge = new Edge(tempC, tempA,23);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1142,1085,4),"ADMINEXE");
        edge2 = new Edge(tempA, tempB, 155);
        edgeList.add(edge2);
        originalList.add(tempB);

        //##Central##//
        Node midNode = new Node(null, new Coordinate(1142, 1026, 4),"CENTRAL");
        edge = new Edge(tempB, midNode, 59);
        edgeList.add(edge);
        originalList.add(midNode);
        //This node will come up again later. You'll see. It'll be there.

        tempC = new Node(null, new Coordinate(1284, 1085, 4), "o_4102-4");
        edge = new Edge(tempB,tempC,142);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1327,1085,4),"PATSAFE");
        edge2 = new Edge(tempC,tempA,43);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1407,1085,4),"SURGndPHYS");
        edge = new Edge(tempA,tempB,80);
        edgeList.add(edge);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1537,1085,4),"qual_spec");
        edge2 = new Edge(tempB,tempC,130);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1620,1085,4),"o_41m1-43s2");
        edge = new Edge(tempC,tempA,83);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1675,1085,4),"HRndCOMP");
        edge2 = new Edge(tempA,tempB,55);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1721,1085,4),"FINANCE");
        edge = new Edge(tempB,tempC,46);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1760,1085,4),"CLINnd4308");
        edge2 = new Edge(tempC,tempA,39);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1808,1085,4),"LOCKnd4311");
        edge = new Edge(tempA,tempB,48);
        edgeList.add(edge);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1871,1085,4),"PLASTICS");
        edge2 = new Edge(tempB,tempC,63);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1907,1085,4),"FOOT_ANKLE");
        edge = new Edge(tempC,tempA,36);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1967,1085,4),"FAC_FIN_IS");
        edge2 = new Edge(tempA,tempB,60);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(2039,1085,4),"o_4329-31-21");
        edge = new Edge(tempB,tempC,72);
        edgeList.add(edge);
        originalList.add(tempC);

        //Eyyy, there it is! I told you the node would come up again!
        tempA = new Node(null, new Coordinate(1084,1026, 4), "o_4-5");
        edge2 = new Edge(tempA, midNode, 58);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1194, 1026, 4), "o_1-2-3");
        edge = new Edge(tempB, midNode, 52);
        edgeList.add(edge);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1142, 922, 4), "LUNG");
        edge2 = new Edge(tempC, midNode, 104);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1142, 856, 4), "HSKPG");
        edge = new Edge(tempA, tempC, 66);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1142, 796, 4), "NURSESPACE");
        edge2 = new Edge(tempB, tempA, 60);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1142, 770, 4), "SUITE4349-4995");
        edge = new Edge(tempC, tempB, 26);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1142, 696, 4), "SUITE4985-90");
        edge2 = new Edge(tempA, tempC, 74);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1142, 601, 4), "SUITE4970");
        edge = new Edge(tempB, tempA, 95);
        edgeList.add(edge);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1142, 523, 4), "SUITE4930-35");
        edge2 = new Edge(tempC, tempB, 78);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1142, 457, 4), "SUITE4950-55");
        edge = new Edge(tempA, tempC, 66);
        edgeList.add(edge);
        originalList.add(tempA);

        tempUpperMiddle = new Node(null, new Coordinate(1142, 395, 4), "upperMiddle");
        edge2 = new Edge(tempUpperMiddle, tempA, 62);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1235, 395, 4), "SUITE48");
        edge = new Edge(tempC, tempB, 93);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1235, 237, 4), "SUITE47");
        edge2 = new Edge(tempA, tempC, 158);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1194, 216, 4), "SUITE45");
        edge = new Edge(tempB, tempA, 46);
        edgeList.add(edge);
        originalList.add(tempB);

        tempTopMiddle = new Node(null, new Coordinate(1142, 214, 4), "TOPMIDDLE");
        edge2 = new Edge(tempTopMiddle, tempB, 52);
        edgeList.add(edge2);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1085, 214, 4), "SUITE43");
        edge = new Edge(tempA, tempTopMiddle, 57);
        edgeList.add(edge);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1051, 236, 4), "S43B");
        edge2 = new Edge(tempB, tempA, 40);
        edgeList.add(edge2);
        originalList.add(tempB);

        tempC = new Node(null, new Coordinate(1051, 348, 4), "SUITE40");
        edge = new Edge(tempC, tempB, 112);
        edgeList.add(edge);
        originalList.add(tempC);

        tempA = new Node(null, new Coordinate(1051, 395, 4), "UpLL_Corner");
        edge2 = new Edge(tempA, tempC, 47);
        edgeList.add(edge2);
        originalList.add(tempA);

        tempB = new Node(null, new Coordinate(1142, 307, 4), "o_4c68");
        edge = new Edge(tempB, tempTopMiddle, 93);
        edgeList.add(edge);
        originalList.add(tempB);

        edge2 = new Edge(tempB, tempUpperMiddle, 88);
        edgeList.add(edge2);

        edge = new Edge(tempA, tempUpperMiddle, 91);

        populateTable(originalList); //put array in database now

        EdgesHelper.get(connection).populateTable(edgeList); //pass over Edge List

        HospitalProfessional temphs = getHospitalProfessionalByName("Bhasin, Shalender");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        HospitalService tempService = getHospitalServiceByName("Urology");
        System.out.print(tempService.getName());
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Men''s Health Center");
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Kathrins, Martin");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Malone, Michael");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("McDonald, Michael");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("O''Leary, Michael");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Steele, Graeme");
        temphs.setNodeId(getNodeByName("UROLOGY").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Mohs and Dermatologic Surgery");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Ruiz, Emily");
        temphs.setNodeId(getNodeByName("SUITE4349-4995").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Schmults, Chrysalyne");
        temphs.setNodeId(getNodeByName("SUITE4349-4995").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Waldman, Abigail");
        temphs.setNodeId(getNodeByName("SUITE4349-4995").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Cua, Christopher");
        temphs.setNodeId(getNodeByName("SUITE4985-90").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Tarpy, Robert");
        temphs.setNodeId(getNodeByName("SUITE4985-90").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Lahive, Karen");
        temphs.setNodeId(getNodeByName("SUITE4985-90").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Brigham and Women''s Primary Physicians");
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Goldman, Jill");
        temphs.setNodeId(getNodeByName("SURGndPHYS").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Lilienfeld, Armin");
        temphs.setNodeId(getNodeByName("SURGndPHYS").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Owens, Lisa Michelle");
        temphs.setNodeId(getNodeByName("SURGndPHYS").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Headache Center");
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Neurology");
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Cardiology");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Endocrinology");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Gastroenterology");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Geriatrics/Senior Health");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Hematology");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Medical Specialties");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Pulmonary");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Renal");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Ash, Samuel");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Bachman, William");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Bonaca, Marc");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Cardet, Juan Carlos");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Cardin, Kristin");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Chan, Walter");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Connell, Nathan");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("D''Ambrosio, Carolyn");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Dave, Jatin");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Fanta, Christopher");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Hentschel, Dirk");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Hsu, Joyce");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Lilly, Leonard Stuart");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("McMahon, Gearoid");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Parnes, Aric");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Ramirez, Alberto");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Romano, Keith");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Ruff, Christian");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Saldana, Fidencio");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Schissel, Scott");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Shah, Amil");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Sheth, Samira");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Sweeney, Michael");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Tucker, Kevin");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Voiculescu, Adina");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Wickner, Paige");
        temphs.setNodeId(getNodeByName("SUITE4930-35").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Bernstein, Carolyn");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Burch, Rebecca");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Cochrane, Thomas");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Friedman, Pamela");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Loder, Elizabeth");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Mathew, Paul");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Rizzoli, Paul");
        temphs.setNodeId(getNodeByName("SUITE4970").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Arthritis Center");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Rheumatology Center");
        tempService.setNodeId(tempC.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Hoover, Paul");
        temphs.setNodeId(getNodeByName("SUITE48").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Pariser, Kenneth");
        temphs.setNodeId(getNodeByName("SUITE48").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Todd, Derrick");
        temphs.setNodeId(getNodeByName("SUITE48").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Neurology/Sleep Division");
        tempService.setNodeId(tempA.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Mullally, William");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Novak, Peter");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Pilgrim, David");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Vardeh, Daniel");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Weisholtz, Daniel");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Whitman, Gregory");
        temphs.setNodeId(getNodeByName("SUITE47").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Infectious Diseases");
        tempService.setNodeId(tempA.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Clark, Roger");
        temphs.setNodeId(getNodeByName("SUITE4950-55").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("McGowan, Katherine");
        temphs.setNodeId(getNodeByName("SUITE4950-55").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Brigham and Women''s Primary Physicians");
        tempService.setNodeId(tempA.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Caplan, Laura");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Copello, Maria");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Copello, Maria");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Healy, Barbara");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Lauretti, Linda");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Oliver, Lynn");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Walsh Samp, Kathy");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Walsh Samp, Kathy");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Welker, Roy");
        temphs.setNodeId(getNodeByName("SUITE43").getId());
        updateHospitalProfessional(temphs);

        tempService = HospitalServicesHelper.getHospitalServiceByName("Gastroenterology Associates");
        tempService.setNodeId(tempB.getId());
        HospitalServicesHelper.updateHospitalService(tempService);

        temphs = getHospitalProfessionalByName("Conant, Alene");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Drewniak, Stephen");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Homenko, Daria");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Lo, Amy");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Matloff, Daniel");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("McNabb-Balter, Julia");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);


        temphs = getHospitalProfessionalByName("Mutinga, Muthoka");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Preneta, Ewa");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        temphs = getHospitalProfessionalByName("Smith, Benjamin");
        temphs.setNodeId(getNodeByName("SUITE45").getId());
        updateHospitalProfessional(temphs);

        EdgesHelper eh = EdgesHelper.get(connection);
        EdgesHelper.populateTable(edgeList); //pass over Edge List

        updateNodes();

    }

    /**
     * This function populates the database table from the array
     */
    public void populateTable(ArrayList<Node> list) {
        dropTable();
        buildTable();

        for (Node node : list) {
            addNode(node);
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
//                //have to drop Edge table also since it references the Node Table
//                EdgesHelper temp = EdgesHelper.get(connection);
//                temp.dropTable();

                // Drop the Node table.
                String str = "DROP TABLE " + NodeTable.NAME;
                statement.execute(str); //check Node table

                System.out.println("Node table dropped.");
            } catch (SQLException e) {
                System.out.println("No Node table to drop");
             //   e.printStackTrace();
                //Table did not exist
            }
        } catch (SQLException e) {
            System.out.println("Could not create statement in Node Table");
        //    e.printStackTrace();
        }
    }

    /**
     * This is the function that will create the actual table
     */
    private void buildTable() {
        try {
            statement = connection.createStatement(); //Statement object

            // Create Node table.
            String str = "CREATE TABLE " + NodeTable.NAME + "(" +
                    NodeTable.Cols.ID + " VARCHAR(100) NOT NULL PRIMARY KEY, " +
                    NodeTable.Cols.NAME + " CHAR(100) NOT NULL, " +
                    NodeTable.Cols.X + " FLOAT NOT NULL, " +
                    NodeTable.Cols.Y + " FLOAT NOT NULL, " +
                    NodeTable.Cols.Z + " INT NOT NULL " +
                    ")";
            statement.execute(str);

            System.out.println("Node table created.");

        } catch (SQLException e) {
            System.out.println("Could not build Node table");
         //   e.printStackTrace();
        }
    }

    public static void updateNodes(){
        ArrayList<Node> list = getNodes(null);
        for(Node node: list){
            ArrayList<Node> neighbors = EdgesHelper.getNeighbors(node);
            node.setNeighbors(neighbors);
        }
    }

}