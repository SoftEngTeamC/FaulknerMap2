import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import model.Coordinate;
import model.Node;
import service.CoordinateService;
import service.NodeService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class DataLoader {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "derby" );
        try {
            loadLocations(emf, "data/locations.tsv");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

    private static void loadLocations(EntityManagerFactory emf, String locationsFilePath) throws FileNotFoundException {
        NodeService nodeService = new NodeService(emf);
        CoordinateService coordinateService = new CoordinateService(emf);


        // Create all the nodes
        TsvParserSettings parserSettings = new TsvParserSettings();
        TsvParser parser = new TsvParser(parserSettings);
        List<String[]> allRows = parser.parseAll(DataLoader.class.getClassLoader().getResourceAsStream(locationsFilePath));
        for (String[] row : allRows.subList(1, allRows.size())) {
            for (String c : row) System.out.println(c);
            double x = Double.parseDouble(row[1]);
            double y = Double.parseDouble(row[2]);
            Coordinate location = new Coordinate(x, y, 4);
            coordinateService.persist(location);
            String name = row[0];
            nodeService.persist(new Node(location, name));
        }
    }
}
