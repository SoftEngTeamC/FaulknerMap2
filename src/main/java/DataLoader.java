import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.*;
import service.*;

import javax.persistence.EntityManagerFactory;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    public static void main(String[] args) {
        EntityManagerFactory emf = EMFProvider.getInstance().getEMFactory();
        try {
       //     loadLocations(emf, "data/belkinHouse/locations.tsv");
            loadLocations(emf, "data/floor1/locations.tsv");
            loadLocations(emf, "data/floor2/locations.tsv");
            loadLocations(emf, "data/floor3/locations.tsv");
            loadLocations(emf, "data/floor4/locations.tsv");
            loadLocations(emf, "data/floor5/locations.tsv");
            loadLocations(emf, "data/floor6/locations.tsv");
            loadLocations(emf, "data/floor7/locations.tsv");

        //    loadPeople(emf, "data/belkinHouse/people.tsv");
            loadPeople(emf, "data/floor2/people.tsv");
            loadPeople(emf, "data/floor3/people.tsv");
            loadPeople(emf, "data/floor4/people.tsv");
            loadPeople(emf, "data/floor5/people.tsv");

        //    loadService(emf, "data/belkinHouse/services.tsv");
            loadService(emf, "data/floor1/services.tsv");
            loadService(emf, "data/floor2/services.tsv");
            loadService(emf, "data/floor3/services.tsv");
            loadService(emf, "data/floor4/services.tsv");
            loadService(emf, "data/floor5/services.tsv");
            loadService(emf, "data/floor6/services.tsv");
            loadService(emf, "data/floor7/services.tsv");

           // loadEdges(emf, "data/belkinHouse/edges.tsv");
            loadEdges(emf, "data/floor1/edges.tsv");
            loadEdges(emf, "data/floor2/edges.tsv");
            loadEdges(emf, "data/floor3/edges.tsv");
            loadEdges(emf, "data/floor4/edges.tsv");
            loadEdges(emf, "data/floor5/edges.tsv");
            loadEdges(emf, "data/floor6/edges.tsv");
            loadEdges(emf, "data/floor7/edges.tsv");

            addEdgeIntersections();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

    private static void loadLocations(EntityManagerFactory emf, String locationsFilePath) throws FileNotFoundException {
        NodeService nodeService = new NodeService();
        CoordinateService coordinateService = new CoordinateService();


        // Create all the nodes
        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null)) continue;  // Test for blank line or value

            String[] split = row[0].split("\\s+\\-\\s+");
            if(split.length < 3){
                System.out.print("Could not add ");
                for(int i = 0; i < split.length; i++){
                    System.out.print(split[i]);
                }
                System.out.println("");
            } else {

                // Parse the Coordinate
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                Coordinate location = new Coordinate(x, y, 4);

                coordinateService.persist(location);

                String name = split[0];

                nodeService.persist(new Node(location, name));
            }
        }
    }

    private static void loadPeople(EntityManagerFactory emf, String peopleFilePath) throws FileNotFoundException {
        HospitalProfessionalService professionalService = new HospitalProfessionalService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(peopleFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null)) continue;

            String[] split = row[0].split("\\s+\\-\\s+");
            if(split.length < 3){
                System.out.print("Could not add ");
                for(int i = 0; i < split.length; i++){
                    System.out.print(split[i]);
                }
                System.out.println("");
            } else {

                String name =split[0];
                String title = split[1];

                List<Node> nodes = new ArrayList<>();
                //System.out.println(split[2]);
                nodes.add(nodeService.findNodeByName(split[2]));
                HospitalProfessional hp = new HospitalProfessional(name, title, nodes);

                professionalService.persist(new HospitalProfessional(name, title, nodes));

            }
        }
    }

    private static void loadService(EntityManagerFactory emf, String serviceFilePath) throws FileNotFoundException {
        HospitalServiceService serviceService = new HospitalServiceService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(serviceFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null)) continue;

            String[] split = row[0].split("\\s+\\-\\s+");
            if(split.length < 2){
                System.out.print("Could not add ");
                for(int i = 0; i < split.length; i++){
                    System.out.print(split[i]);
                }
                System.out.println("");
            } else {

                String name =split[0];

                List<Node> nodes = new ArrayList<>();
                nodes.add(nodeService.findNodeByName(split[1]));
            //    System.out.println(split[1]);

                HospitalService hs = new HospitalService(nodes, name);

                serviceService.persist(new HospitalService(nodes, name));


            }
        }
    }

    private static void loadEdges(EntityManagerFactory emf, String locationsFilePath) throws FileNotFoundException {
        EdgeService edgeService = new EdgeService();
        NodeService nodeService = new NodeService();

        // Create all the edges
        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null)) continue;  // Test for blank line or value

            String[] split = row[0].split("\\s+\\-\\s+");
            if(split.length < 2){
                System.out.print("Could not add ");
                for(int i = 0; i < split.length; i++){
                    System.out.print(split[i]);
                }
                System.out.println("");
            } else {

                Node start = nodeService.findNodeByName(split[0]);
                Node end = nodeService.findNodeByName(split[1]);

                if(start == null){
                    System.out.println("could not add edge " + split[0]);
                }

                if(end == null){
                    System.out.println("could not add edge " + split[1]);
                }

                edgeService.persist(new Edge(start, end, 0));
            }
        }
    }

    private static void addEdgeIntersections(){
        NodeService nodeService = new NodeService();
        EdgeService edgeService = new EdgeService();
        for(int i = 1; i < 8; i ++){
            List<Node> floor = nodeService.findNodeIntersectionByFloor(i);
            for(int j = 0; j < floor.size()-1; j ++){
                Edge tempEdge = new Edge(floor.get(j), floor.get(j+1), getEdgeLength(floor.get(j), floor.get(j+1)));
                edgeService.persist(tempEdge);
            }
        }
    }

    private static double getEdgeLength(Node from, Node end){
        double yLen = from.getLocation().getY() - end.getLocation().getY();
        double xLen = from.getLocation().getX() - end.getLocation().getX();
        return Math.sqrt(yLen * yLen + xLen * xLen);
    }
}
