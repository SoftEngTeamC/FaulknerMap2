package service;


import model.HospitalProfessional;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class HospitalProfessionalService extends AbstractService<HospitalProfessional> {
    @Override
    public HospitalProfessional find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        HospitalProfessional temp =  manager.find(HospitalProfessional.class, id);
        manager.close();
        return temp;
    }

    public HospitalProfessional findHospitalProfessionalByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        try {
            HospitalProfessional temp = (HospitalProfessional) manager.createQuery(
                    "SELECT p FROM HospitalProfessional p WHERE p.name LIKE :name")
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
            manager.close();
            return temp;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<HospitalProfessional> getAllProfessionals() {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<HospitalProfessional> temp = manager.createQuery("from HospitalProfessional", HospitalProfessional.class)
                .getResultList();
        manager.close();
        return temp;
    }

    public List<HospitalProfessional> search(String s) {
        EntityManager manager = managerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(manager);
        manager.getTransaction().begin();
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(HospitalProfessional.class).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields("title", "name").matching(s).createQuery();
        javax.persistence.Query JPAQuery = fullTextEntityManager.createFullTextQuery(query, HospitalProfessional.class);

        List<HospitalProfessional> result = JPAQuery.getResultList();
        manager.getTransaction().commit();
        manager.close();
        return result;
    }
}
