package dao;

import java.util.List;

public interface BaseDao<E, ID> {

    boolean insert(E e);

    boolean update(E e);

    boolean deleteById(ID id);

    List<E> getAll();

    E getById(ID id);

}

