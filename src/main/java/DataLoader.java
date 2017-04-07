import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.Coordinate;
import model.Node;
import service.CoordinateService;
import service.EMFProvider;
import service.HospitalProfessionalService;
import service.NodeService;

import javax.persistence.EntityManagerFactory;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    public static void main(String[] args) {
        EntityManagerFactory emf = EMFProvider.getInstance().getEMFactory();
        try {
            loadLocations(emf, "data/locations.tsv");
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

            // Parse the Coordinate
            double x = Double.parseDouble(row[1]);
            double y = Double.parseDouble(row[2]);
            Coordinate location = new Coordinate(x, y, 4);

            coordinateService.persist(location);

            String name = row[0];

            nodeService.persist(new Node(location, name));
        }
    }

    private static void loadPeople(EntityManagerFactory emf, String peopleFilePath) throws FileNotFoundException {
        HospitalProfessionalService professionalService = new HospitalProfessionalService();

        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);
        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(peopleFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {

        }

    }
}
