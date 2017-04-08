package service;


import model.Tag;

import javax.persistence.EntityManager;

public class TagService extends AbstractService<Tag> {
    public Tag find(Long id) {
        EntityManager manager = this.managerFactory.createEntityManager();
        return manager.find(Tag.class, id);
    }
}
