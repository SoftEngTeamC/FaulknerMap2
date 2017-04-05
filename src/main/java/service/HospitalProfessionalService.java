package service;


import model.HospitalProfessional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HospitalProfessionalService extends AbstractService<HospitalProfessional> {
    public HospitalProfessionalService(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public HospitalProfessional find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalProfessional.class, id);
    }
}
