package service;


import model.HospitalService;
import org.hibernate.search.exception.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class HospitalServiceService extends AbstractService<HospitalService> {
    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        HospitalService service = manager.find(HospitalService.class, id);
        manager.close();
        return service;
    }

    public HospitalService findHospitalServiceByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        try {
            HospitalService service = (HospitalService) manager.createQuery(
                    "SELECT s FROM HospitalService s WHERE s.name LIKE :name")
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
            manager.close();
            return service;
        } catch (NoResultException e) {
            manager.close();
            return null;
        }
    }

    public List<HospitalService> getAllServices() {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<HospitalService> services = manager.createQuery("from HospitalService", HospitalService.class)
                .getResultList();
        manager.close();
        return services;
    }

//    public List<HospitalService> search(String s) {
//        EntityManager manager = managerFactory.createEntityManager();
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);
//        manager.getTransaction().begin();
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(HospitalService.class).get();
//        org.apache.lucene.search.Query query = qb.keyword().onFields("name").matching(s).createQuery();
//        javax.persistence.Query JPAQuery = fullTextEntityManager.createFullTextQuery(query, HospitalService.class);
//
//        List<HospitalService> result = JPAQuery.getResultList();
//        manager.getTransaction().commit();
//        manager.close();
//        return result;
//    }

    public List<HospitalService> search(String s) {
        EntityManager manager = managerFactory.createEntityManager();
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);
//        manager.getTransaction().begin();
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder().forEntity(HospitalService.class).get();
        List<HospitalService> temp = new ArrayList<>();
        for(HospitalService hs: getAllServices()){
            if(hs.getName().toLowerCase().contains(s.toLowerCase())){
                temp.add(hs);
            }
        }
        manager.close();
        return temp;
//        try {
//            System.out.println("looking for service " + s);
//            org.apache.lucene.search.Query query = qb.keyword().onFields("name").matching("*" + s +"*").createQuery();
//            javax.persistence.Query JPAQuery = fullTextEntityManager.createFullTextQuery(query, HospitalService.class);
//            return JPAQuery.getResultList();
//        } catch (EmptyQueryException e) {
//            System.out.println("cant find service");
//            e.printStackTrace();
//            return getAllServices();
//        } finally {
//            System.out.println("done for service");
//            manager.getTransaction().commit();
//            fullTextEntityManager.close();
//        }
    }

}
