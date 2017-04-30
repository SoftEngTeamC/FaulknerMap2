package service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EMFProvider {

    private static EMFProvider myHelper = new EMFProvider();
    private static EntityManagerFactory emf = null;
    private static EntityManagerFactory testEMF = null;
    private boolean useTestDB = false;

    static {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("derby");
                System.out.println("Created new Entity Manager Factory");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (testEMF == null) {
            try {
                testEMF = Persistence.createEntityManagerFactory("testDB");
                System.out.println("Created testDB emf.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static EMFProvider getInstance() {
        if (myHelper == null) {
            myHelper = new EMFProvider();
        }
        return myHelper;
    }

    public void close() {
        emf.close();
        testEMF.close();
    }

    public EntityManagerFactory getEMFactory() {
        if (useTestDB) return testEMF;
        return emf;
    }

    public void useTest() {
        useTestDB = true;
    }

}