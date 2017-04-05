package service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;

public abstract class AbstractService<T extends Serializable> implements Service<T> {
    protected EntityManagerFactory managerFactory;

    public AbstractService(EntityManagerFactory emf) {
        this.managerFactory = emf;
    }

    @Override
    public void remove(T item) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.remove(manager.merge(item));
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public void persist(T item) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(item);
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public T merge(T item) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        T mergedItem = manager.merge(item);
        manager.getTransaction().commit();
        manager.close();
        return mergedItem;
    }
}
