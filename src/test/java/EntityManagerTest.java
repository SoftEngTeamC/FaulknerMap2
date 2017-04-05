import junit.framework.TestCase;
import model.Coordinate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EntityManagerTest extends TestCase {
    private EntityManagerFactory entityManagerFactory;

    @Override
    protected void setUp() throws Exception {
        // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
        // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
        entityManagerFactory = Persistence.createEntityManagerFactory( "derby" );
    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    public void testBasicUsage() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Coordinate(10, 10, 1));
        entityManager.persist(new Coordinate(2, 3, 2));
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Coordinate> result = entityManager.createQuery( "from Coordinate", Coordinate.class ).getResultList();
        for ( Coordinate c : result ) {
            System.out.println(c.getX()*c.getY()*c.getFloor());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
