package service;

import java.io.Serializable;

public interface Service<T extends Serializable> {
    T find(Long id);
    void remove(T item);
    void persist(T item);
    T merge(T item);
}
