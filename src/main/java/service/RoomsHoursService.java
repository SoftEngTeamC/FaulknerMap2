package service;

import model.RoomsHours;

import javax.persistence.EntityManager;

/**
 * Created by wangyaofeng on 4/27/17.
 */
public class RoomsHoursService extends AbstractService{
    public RoomsHours find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        RoomsHours hours = manager.find(RoomsHours.class, id);
        manager.close();
        return hours;
    }
}
