package service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EMFHelper {

    private static EMFHelper myHelper = new EMFHelper();
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
    public static EMFHelper getInstance() {
        if (myHelper == null) {
            myHelper = new EMFHelper();
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