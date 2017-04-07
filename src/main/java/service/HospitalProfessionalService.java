package service;


import model.HospitalProfessional;

import javax.persistence.EntityManager;

public class HospitalProfessionalService extends AbstractService<HospitalProfessional> {
    @Override
    public HospitalProfessional find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalProfessional.class, id);
    }
}
