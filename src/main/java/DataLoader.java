import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.*;
import service.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    public static void main(String[] args) {
        try {
            loadLocations("data/floor1/locations.tsv", 1);
            loadLocations("data/floor2/locations.tsv", 2);
            loadLocations("data/floor3/locations.tsv", 3);
            loadLocations("data/floor4/locations.tsv", 4);
            loadLocations("data/floor5/locations.tsv", 5);
            loadLocations("data/floor6/locations.tsv", 6);
            loadLocations("data/floor7/locations.tsv", 7);

            loadPeople("data/floor2/people.tsv");
            loadPeople("data/floor3/people.tsv");
            loadPeople("data/floor4/people.tsv");
            loadPeople("data/floor5/people.tsv");

            loadService("data/floor1/services.tsv");
            loadService("data/floor2/services.tsv");
            loadService("data/floor3/services.tsv");
            loadService("data/floor4/services.tsv");
            loadService("data/floor5/services.tsv");
            loadService("data/floor6/services.tsv");
            loadService("data/floor7/services.tsv");

            loadEdges("data/floor1/edges.tsv");
            loadEdges("data/floor2/edges.tsv");
            loadEdges("data/floor3/edges.tsv");
            loadEdges("data/floor4/edges.tsv");
            loadEdges("data/floor5/edges.tsv");
            loadEdges("data/floor6/edges.tsv");
            loadEdges("data/floor7/edges.tsv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            EMFProvider.getInstance().getEMFactory().close();
        }
    }

    private static void loadLocations(String locationsFilePath, int floor) throws FileNotFoundException {
        NodeService nodeService = new NodeService();
        CoordinateService coordinateService = new CoordinateService();


        // Create all the nodes
        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null) || row.length < 3) continue;  // Test for blank line or value

            double x = Double.parseDouble(row[1]);
            double y = Double.parseDouble(row[2]);
            Coordinate location = new Coordinate(x, y, floor);

            coordinateService.persist(location);

            String name = row[0];

            nodeService.persist(new Node(name, location));

        }
    }

    private static void loadPeople(String peopleFilePath) throws FileNotFoundException {
        HospitalProfessionalService professionalService = new HospitalProfessionalService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(peopleFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null) || row.length < 3) continue;

            String name = row[0];
            String title = row[1];

            List<Node> nodes = new ArrayList<>();
            nodes.add(nodeService.findNodeByName(row[2]));

            professionalService.persist(new HospitalProfessional(name, title, nodes));
        }
    }

    private static void loadService(String serviceFilePath) throws FileNotFoundException {
        HospitalServiceService serviceService = new HospitalServiceService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);

        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(serviceFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            if (Arrays.asList(row).contains(null) || row.length < 2) continue;

            String name = row[0];

            List<Node> nodes = new ArrayList<>();
            nodes.add(nodeService.findNodeByName(row[1]));

            serviceService.persist(new HospitalService(name, nodes));
        }
    }

    private static void loadEdges(String locationsFilePath) throws FileNotFoundException {
        EdgeService edgeService = new EdgeService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 2) {
                    return;
                }

                String startName = (String) row[0];
                String endName = (String) row[1];
                Node start = nodeService.findNodeByName(startName);
                Node end = nodeService.findNodeByName(endName);

                if (start == null) {
                    System.err.println("Couldn't find a node with name " + startName + " while parsing the following line:");
                    System.err.println(context.currentLine());
                    return;
                }

                if (end == null) {
                    System.err.println("Couldn't find a node with name " + endName + " while parsing the following line:");
                    System.err.println(context.currentLine());
                    return;
                }

                edgeService.persist(new Edge(start, end, 0));
                edgeService.persist(new Edge(end, start, 0));
            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
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
