package repository;

import java.util.List;

public interface Repository<T> {
    List<T> listaEntitati();
    void adauga(T entity);
    void actualizeaza(T entity);
    void sterge(T entity);
    T gasesteDupaId(int id);
}

