package dao;

import java.util.List;

public interface IDao<T> {
    void save(T data);
    T retrieve(int id);
    void delete(int id);
    List<T> getAll();
}