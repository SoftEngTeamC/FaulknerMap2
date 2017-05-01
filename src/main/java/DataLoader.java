import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.*;
import service.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            loadPeople("data/floor7/people.tsv");

            loadServices("data/belkinHouse/floor1/services.tsv");
            loadServices("data/belkinHouse/floor2/services.tsv");
            loadServices("data/belkinHouse/floor3/services.tsv");
            loadServices("data/belkinHouse/floor4/services.tsv");
            loadServices("data/floor1/services.tsv");
            loadServices("data/floor2/services.tsv");
            loadServices("data/floor3/services.tsv");
            loadServices("data/floor4/services.tsv");
            loadServices("data/floor5/services.tsv");
            loadServices("data/floor6/services.tsv");
            loadServices("data/floor7/services.tsv");
            loadServices("data/floor1/suites.tsv");
            loadServices("data/floor2/suites.tsv");
            loadServices("data/floor3/suites.tsv");
            loadServices("data/floor4/suites.tsv");
            loadServices("data/floor5/suites.tsv");
            loadServices("data/floor6/suites.tsv");
            loadServices("data/floor7/suites.tsv");

            loadEdges("data/tempEdges.tsv", false);
            loadEdges("data/employeeEdges.tsv", true);

            loadHours("data/hours.tsv");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            EMFProvider.getInstance().close();
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
                    System.err.print("Could not parse location ");
                    for (Object aRow : row) {
                        System.err.print(" " + aRow + " ");
                    }
                    System.err.println();
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
                    System.err.print("Could not parse doctor ");
                    for (Object aRow : row) {
                        System.err.print(" " + aRow + " ");
                    }
                    System.err.println();
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

    private static void loadServices(String serviceFilePath) throws FileNotFoundException {
        HospitalServiceService serviceService = new HospitalServiceService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 2) {
                    System.err.print("Could not parse service  ");
                    for (Object aRow : row) {
                        System.err.print(" " + aRow + " ");
                    }
                    System.err.println();
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

    private static void loadEdges(String locationsFilePath, boolean disabled) throws FileNotFoundException {
        EdgeService edgeService = new EdgeService();
        NodeService nodeService = new NodeService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 2) {
                    System.err.print("Could not parse edge ");
                    for (Object aRow : row) {
                        System.err.print(" " + aRow + " ");
                    }
                    System.err.println();
                    return;
                }

                String startName = (String) row[0];
                String endName = (String) row[1];
                Node start = nodeService.findNodeByName(startName);
                Node end = nodeService.findNodeByName(endName);

                if (start == null) {
                    System.err.println("Couldn't find a node with name " + startName + " while parsing line " + context.currentLine() + " in " + locationsFilePath);
                    return;
                }

                if (end == null) {
                    System.err.println("Couldn't find a node with name " + endName + " while parsing line " + context.currentLine() + " in " + locationsFilePath);
                    return;
                }

                Edge temp = new Edge(start, end, 0);
                temp.setDisabled(disabled);
                edgeService.persist(temp);
            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
    }

    private static void loadHours(String hoursFilePath) throws FileNotFoundException {
        HoursService hoursService = new HoursService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.setHeaderExtractionEnabled(true);

        SimpleDateFormat dateParser = new SimpleDateFormat("H:m");

        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                if (Arrays.asList(row).contains(null) || row.length < 5) {
                    System.err.println("couldn't parse hours on line " + context.currentLine() + "because there was missing information.");
                    return;
                }

                String name = (String) row[0];

                String morningStart = (String) row[1];
                String morningEnd = (String) row[2];
                String eveningStart = (String) row[3];
                String eveningEnd = (String) row[4];

                try {
                    Date morningStartTime = dateParser.parse(morningStart);
                    Date morningEndTime = dateParser.parse(morningEnd);
                    Date eveningStartTime = dateParser.parse(eveningStart);
                    Date eveningEndTime = dateParser.parse(eveningEnd);

                    hoursService.persist(new Hours(name, morningStartTime, morningEndTime, eveningStartTime, eveningEndTime));

                } catch (ParseException e) {
                    System.err.println("Couldn't parse hours on line " + context.currentLine());
                    e.printStackTrace();
                }

            }
        };
        parserSettings.setProcessor(rowProcessor);

        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(DataLoader.class.getClassLoader().getResourceAsStream(hoursFilePath));
    }

    //call this when you want to make sure there are no duplicate/unnecessary edges
    private void cleanEdges(){
        EdgeService edgeService = new EdgeService();
        List<Edge> edges = edgeService.getAllEdges();

        for(Edge e1: edges){
            for(Edge e2: edges) {
                if (e1.getStart().getName().equals(e2.getStart().getName()) &&
                        !Objects.equals(e1.getId(), e2.getId())) {
                    if (e1.getEnd().getName().equals(e2.getEnd().getName())) {
                        System.out.println("Duplicate: " + e1.getStart().getName() + " " + e2.getEnd().getName());
                        edgeService.remove(e2);
                    }
                }
                if (e1.getStart().getName().equals(e2.getEnd().getName()) &&
                        !Objects.equals(e1.getId(), e2.getId())) {
                    if (e1.getStart().getName().equals(e2.getEnd().getName())) {
                        System.out.println("Duplicate: " + e1.getStart().getName() + " " + e2.getEnd().getName());
                        edgeService.remove(e2);
                    }
                }
            }
            if (e1.getStart().getName().equals(e1.getEnd().getName())) {
                System.out.println("Unnecessary: " + e1.getStart().getName() + " " + e1.getEnd().getName());
                edgeService.remove(e1);
            }
        }

    }
}
