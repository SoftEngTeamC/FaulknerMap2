package service;


import model.HospitalProfessional;
import model.HospitalService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class HospitalServiceService extends AbstractService<HospitalService> {
    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        HospitalService temp = manager.find(HospitalService.class, id);
        manager.close();
        return temp;
    }

    public HospitalService findHospitalServiceByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        try {
            HospitalService temp = (HospitalService) manager.createQuery(
                    "SELECT s FROM HospitalService s WHERE s.name LIKE :name")
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
            manager.close();
            return temp;
        } catch (NoResultException e) {
            manager.close();
            return null;
        }
    }

    public List<HospitalService> getAllServices() {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<HospitalService> temp = manager.createQuery("from HospitalService", HospitalService.class)
                .getResultList();
        manager.close();
        return temp;
    }
}
