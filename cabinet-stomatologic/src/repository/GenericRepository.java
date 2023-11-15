package repository;

import java.util.ArrayList;
import java.util.List;

import domeniu.Identifiable;

public abstract class GenericRepository<T> {
    private final List<T> entities = new ArrayList<>();

    public void adauga(T entity) {
        entities.add(entity);
    }

    public T gasesteDupaId(int id) {
        for (T entity : entities) {
            if (entity instanceof Identifiable identifiableEntity) {
                if (identifiableEntity.getId() == id) {
                    return entity;
                }
            }
        }
        return null;
    }

    public List<T> listaToate() {
        return entities;
    }

    public void actualizeaza(T entity) {
        int index = entities.indexOf(entity);
        if (index != -1) {
            entities.set(index, entity);
        }
    }

    public void sterge(T entity) {
        entities.remove(entity);
    }

    public abstract List<T> listaEntitati();
}
