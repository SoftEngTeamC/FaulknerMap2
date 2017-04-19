package service;


import model.HospitalProfessional;
import model.Node;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class HospitalProfessionalService extends AbstractService<HospitalProfessional> {
    @Override
    public HospitalProfessional find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        HospitalProfessional temp =  manager.find(HospitalProfessional.class, id);
        manager.close();
        return temp;
    }

    public HospitalProfessional findHospitalProfessionalByName(String name) {
        EntityManager manager = this.managerFactory.createEntityManager();
        try {
            HospitalProfessional temp = (HospitalProfessional) manager.createQuery(
                    "SELECT p FROM HospitalProfessional p WHERE p.name LIKE :name")
                    .setParameter("name", name)
                    .setMaxResults(1).getSingleResult();
            manager.close();
            return temp;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<HospitalProfessional> getAllProfessionals() {
        EntityManager manager = this.managerFactory.createEntityManager();
        List<HospitalProfessional> temp = manager.createQuery("from HospitalProfessional", HospitalProfessional.class)
                .getResultList();
        manager.close();
        return temp;
    }
}
