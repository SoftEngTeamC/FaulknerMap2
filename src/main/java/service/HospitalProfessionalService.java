package service;


import model.HospitalProfessional;
import model.HospitalService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
}
