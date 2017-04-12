package service;

import model.Hours;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EMFProvider {

    private static EMFProvider myHelper = new EMFProvider();
    private static EntityManagerFactory emf = null;
    public static Hours hours;

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

    public static EMFProvider getInstance() {
        if (myHelper == null) {
            myHelper = new EMFProvider();
        }
        return myHelper;
    }

    public EntityManagerFactory getEMFactory() {
        return emf;
    }
}