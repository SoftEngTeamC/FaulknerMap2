package service;


import model.HospitalProfessional;

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
        String sql = "SELECT p FROM HospitalProfessional p where s.name = :name";
        TypedQuery<HospitalProfessional> query = (TypedQuery<HospitalProfessional>) manager.createQuery(sql);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
