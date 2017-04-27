package service;


import model.Hours;

import javax.persistence.EntityManager;

public class HoursService extends AbstractService<Hours> {
    public Hours find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        Hours hours = manager.find(Hours.class, id);
        manager.close();
        return hours;
    }
}
