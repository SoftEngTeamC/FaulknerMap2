package service;


import model.HospitalService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HospitalServiceService extends AbstractService<HospitalService> {
    public HospitalServiceService(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalService.class, id);
    }
}
