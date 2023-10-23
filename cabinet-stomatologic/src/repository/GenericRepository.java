package repository;

import java.util.ArrayList;
import java.util.List;

import domeniu.Identifiable;
import domeniu.Pacient;
import domeniu.Programare;

public class GenericRepository<T> {
    private List<T> entities = new ArrayList<>();

    public void adauga(T entity) {
        entities.add(entity);
    }

    public T gasesteDupaId(int id) {
        for (T entity : entities) {
            if (entity instanceof Identifiable) {
                Identifiable identifiableEntity = (Identifiable) entity;
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
}
