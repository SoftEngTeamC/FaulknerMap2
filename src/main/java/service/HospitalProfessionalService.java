package service;


import model.HospitalProfessional;

import javax.persistence.EntityManager;
import java.util.List;

public class HospitalProfessionalService extends AbstractService<HospitalProfessional> {
    @Override
    public HospitalProfessional find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalProfessional.class, id);
    }

    public HospitalProfessional findHospitalProfessionalByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return (HospitalProfessional) manager.createQuery(
                "SELECT p FROM HospitalProfessional p WHERE p.name LIKE :name")
                .setParameter("name", name)
                .setMaxResults(1).getSingleResult();
    }

    public List<HospitalProfessional> getAllProfessionals() {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.createQuery("from HospitalProfessional", HospitalProfessional.class)
                .getResultList();
    }
}
