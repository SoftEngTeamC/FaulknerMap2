package service;


import model.HospitalService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class HospitalServiceService extends AbstractService<HospitalService> {
    @Override
    public HospitalService find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(HospitalService.class, id);
    }

    public HospitalService findHospitalServiceByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        String sql = "SELECT s FROM HospitalService s where s.name = :name";
        TypedQuery<HospitalService> query = (TypedQuery<HospitalService>) manager.createQuery(sql);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
