package service;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

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

    public List<T> search(String s, Class<T> c) {
        EntityManager manager = managerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);
        manager.getTransaction().begin();
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(c).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields().matching(s).createQuery();
        javax.persistence.Query JPAQuery = fullTextEntityManager.createFullTextQuery(query, c);

        List<T> result = JPAQuery.getResultList();
        manager.getTransaction().commit();
        manager.close();
        return result;
    }
}
