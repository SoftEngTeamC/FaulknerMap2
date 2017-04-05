package service;

public interface Service<T> {
    T find(Long id);
    void remove(T item);
    void persist(T item);
    T merge(T item);
}
