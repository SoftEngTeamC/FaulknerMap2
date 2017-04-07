package service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EMFProvider {

    private static EMFProvider myHelper = new EMFProvider();
    private static EntityManagerFactory emf = null;


    //statically check if emf has been created yet
    //only needs to happen once since singleton
    static {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("derby");
                System.out.println("Created new Entity Manager Factory");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Static Accessor Method
     *
     * @return
     */
    public static EMFProvider getInstance() {
        if (myHelper == null) {
            myHelper = new EMFProvider();
        }
        return myHelper;
    }


    /**
     * get Entity Manager Factory
     * @return
     */
    public EntityManagerFactory getEMFactory() {
        return emf;
    }
}