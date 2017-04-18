package service;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class AbstractService<T> implements Service<T> {
    protected EntityManagerFactory managerFactory;

    public AbstractService() {
        this.managerFactory = EMFProvider.getInstance().getEMFactory();
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
