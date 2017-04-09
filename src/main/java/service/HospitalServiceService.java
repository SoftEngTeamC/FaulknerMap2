package service;


import model.HospitalProfessional;
import model.HospitalService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class HospitalServiceService extends AbstractService<HospitalService> {
    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalService.class, id);
    }

    public HospitalService findHospitalServiceByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
            return (HospitalService) manager.createQuery(
                    "SELECT s FROM HospitalService s WHERE s.name LIKE :name")
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
    }

    public List<HospitalService> getAllServices() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from HospitalService", HospitalService.class)
                .getResultList();
    }
}
