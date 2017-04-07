package service;


import model.HospitalService;

import javax.persistence.EntityManager;

public class HospitalServiceService extends AbstractService<HospitalService> {
    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalService.class, id);
    }
}
