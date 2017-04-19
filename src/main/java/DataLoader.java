import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.*;
import service.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoader {
    public static void main(String[] args) {
        try {
            loadLocations("data/belkinHouse/floor1/locations.tsv", 1);
            loadLocations("data/belkinHouse/floor2/locations.tsv", 2);
            loadLocations("data/belkinHouse/floor3/locations.tsv", 3);
            loadLocations("data/belkinHouse/floor4/locations.tsv", 4);
            loadLocations("data/floor1/locations.tsv", 1);
            loadLocations("data/floor2/locations.tsv", 2);
            loadLocations("data/floor3/locations.tsv", 3);
            loadLocations("data/floor4/locations.tsv", 4);
            loadLocations("data/floor5/locations.tsv", 5);
            loadLocations("data/floor6/locations.tsv", 6);
            loadLocations("data/floor7/locations.tsv", 7);
            loadLocations("data/locationsTemp.tsv", 1);


            loadPeople("data/belkinHouse/floor1/people.tsv");
            loadPeople("data/floor1/kiosk.tsv");
            loadPeople("data/floor2/people.tsv");
            loadPeople("data/floor3/people.tsv");
            loadPeople("data/floor4/people.tsv");
            loadPeople("data/floor5/people.tsv");

            loadService("data/belkinHouse/floor1/services.tsv");
            loadService("data/belkinHouse/floor2/services.tsv");
            loadService("data/belkinHouse/floor3/services.tsv");
            loadService("data/belkinHouse/floor4/services.tsv");
            loadService("data/floor1/services.tsv");
            loadService("data/floor2/services.tsv");
            loadService("data/floor3/services.tsv");
            loadService("data/floor4/services.tsv");
            loadService("data/floor5/services.tsv");
            loadService("data/floor6/services.tsv");
            loadService("data/floor7/services.tsv");

//            loadEdges("data/belkinHouse/floor1/edges.tsv", 1);
//            loadEdges("data/belkinHouse/floor2/edges.tsv", 2);
//            loadEdges("data/belkinHouse/floor3/edges.tsv", 3);
//            loadEdges("data/belkinHouse/floor4/edges.tsv", 4);
 //           loadEdges("data/floor1/edges.tsv", 1);
//            loadEdges("data/floor2/edges.tsv",2);
//            loadEdges("data/floor3/edges.tsv",3);
//            loadEdges("data/floor4/edges.tsv",4);
//            loadEdges("data/floor5/edges.tsv",5);
//            loadEdges("data/floor6/edges.tsv",6);
//            loadEdges("data/floor7/edges.tsv",7);

            loadEdges("data/allEdges.tsv", 1);

                     connectElevators();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            EMFProvider.getInstance().getEMFactory().close();
        }
    }

    private static void loadLocations(String locationsFilePath, int floor) throws FileNotFoundException {
        NodeService nodeService = new NodeService();
        CoordinateService coordinateService = new CoordinateService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 3) {
                    System.out.print("Could not parse location ");
                    for(int i = 0; i < row.length; i++){
                        System.out.print(" " + row[i] + " ");
                    }
                    System.out.println();
                    return;
                    // Test for blank line or value
                }
                double x = (Double) row[1];
                double y = (Double) row[2];
                Coordinate location = new Coordinate(x, y, floor);

                coordinateService.persist(location);

                String name = (String) row[0];

                nodeService.persist(new Node(name, location));
            }
        };
        rowProcessor.convertIndexes(Conversions.toDouble()).set(1);
        rowProcessor.convertIndexes(Conversions.toDouble()).set(2);
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
    }

    private static void loadPeople(String peopleFilePath) throws FileNotFoundException {
        HospitalProfessionalService professionalService = new HospitalProfessionalService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 3) {
                    System.out.print("Could not parse doctor ");
                    for(int i = 0; i < row.length; i++){
                        System.out.print(" " + row[i] + " ");
                    }
                    System.out.println();
                    return;
                }

                String name = (String) row[0];
                String title = (String) row[1];

                String locationName = (String) row[2];
                Node location = nodeService.findNodeByName(locationName);
                if (location == null) {
                    System.err.println("Couldn't find a node with named " + locationName + " while parsing line " + context.currentLine() + " in " + peopleFilePath);

                    return;
                }
                List<Node> nodes = new ArrayList<>();
                nodes.add(location);

                professionalService.persist(new HospitalProfessional(name, title, nodes));
            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(peopleFilePath));
    }

    private static void loadService(String serviceFilePath) throws FileNotFoundException {
        HospitalServiceService serviceService = new HospitalServiceService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 2) {
                    System.out.print("Could not parse service  ");
                    for(int i = 0; i < row.length; i++){
                        System.out.print(" " + row[i] + " ");
                    }
                    System.out.println();
                    return;
                }

                String name = (String) row[0];

                String locationName = (String) row[1];
                Node location = nodeService.findNodeByName(locationName);
                if (location == null) {
                    System.err.println("Couldn't find a node with named " + locationName + " while parsing line " + context.currentLine() + " in " + serviceFilePath);
                    return;
                }
                List<Node> nodes = new ArrayList<>();
                nodes.add(location);

                serviceService.persist(new HospitalService(name, nodes));

            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(serviceFilePath));
    }

    private static void loadEdges(String locationsFilePath, int floor) throws FileNotFoundException {
        EdgeService edgeService = new EdgeService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 2) {
                    System.out.print("Could not parse edge ");
                    for(int i = 0; i < row.length; i++){
                        System.out.print(" " + row[i] + " ");
                    }
                    System.out.println();
                    return;
                }

                String startName = (String) row[0];
                String endName = (String) row[1];
                Node start = nodeService.find(Long.parseLong((String)row[0]));
                Node end = nodeService.find(Long.parseLong((String)row[1]));

                if (start == null) {
                    System.err.println("Couldn't find a node with name " + startName + " while parsing line " + context.currentLine() + " in " + locationsFilePath);
                    return;
                }

                if (end == null) {
                    System.err.println("Couldn't find a node with name " + endName + " while parsing line " + context.currentLine() + " in " + locationsFilePath);
                    return;
                }

              //     System.out.println(start.getName());
                edgeService.persist(new Edge(start, end, 0));
            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
    }

    private static void connectElevators() {
        NodeService nodeService = new NodeService();
        EdgeService edgeService = new EdgeService();

        List<Node> elevators = nodeService.getAllNodes().stream()
                .filter(n -> n.getName().toLowerCase().contains("elevator"))
                .collect(Collectors.toList());
        // Group elevators by name
        Map<String, Set<Node>> elevatorGroups = new HashMap<>();
        for (Node elevator : elevators) {
            if (elevatorGroups.containsKey(elevator.getName())) {
                elevatorGroups.get(elevator.getName()).add(elevator);
            } else {
                Set<Node> newGroup = new HashSet<>();
                newGroup.add(elevator);
                elevatorGroups.put(elevator.getName(), newGroup);
            }
        }
        // Connect all the groups
        for (Set<Node> group : elevatorGroups.values()) {
            for (Node n1 : group) {
                for (Node n2 : group) {
                    edgeService.persist(new Edge(n1, n2, 0));
                }
            }
        }
    }

    private static void addEdgeIntersections(){
        NodeService nodeService = new NodeService();
        EdgeService edgeService = new EdgeService();
        for(int i = 1; i < 8; i ++){
            List<Node> floor = nodeService.findNodeIntersectionByFloor(i);
            for(int j = 0; j < floor.size()-1; j ++){
                edgeService.persist(new Edge(floor.get(j), floor.get(j+1), getEdgeLength(floor.get(j), floor.get(j+1))));
                edgeService.persist(new Edge(floor.get(j+1), floor.get(j), getEdgeLength(floor.get(j+1), floor.get(j))));

            }
        }
    }

    private static double getEdgeLength(Node from, Node end){
        double yLen = from.getLocation().getY() - end.getLocation().getY();
        double xLen = from.getLocation().getX() - end.getLocation().getX();
        return Math.sqrt(yLen * yLen + xLen * xLen);
    }
}
