package repository;

import domeniu.Identifiable;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class BinaryFileRepository<T> implements Repository<T> {
    private final String filePath;
    private final List<T> entities = new ArrayList<>();

    public BinaryFileRepository(String filePath) {
        this.filePath = filePath;
        incarcaDateDinFisier();
    }

    private void incarcaDateDinFisier() {
        try{
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
                // Deserializați datele din fișier într-o listă de obiecte de tip T
                List<T> entitatiInFisier = (List<T>) inputStream.readObject();
                entities.clear();  // Eliberați lista curentă
                entities.addAll(entitatiInFisier);
            } catch (FileNotFoundException e) {
                // Poate fi tratată o excepție pentru cazul în care fișierul nu există încă
            }
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }


    private void salveazaDateInFisier() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(entities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adauga(T entity) {
        entities.add(entity);
        salveazaDateInFisier();
    }

    @Override
    public void actualizeaza(T entity) {
        if (entities.contains(entity)) {
            entities.set(entities.indexOf(entity), entity);
        }
    }
    @Override
    public void sterge(T entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
        }
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

    @Override
    public List<T> listaEntitati() {
        return new ArrayList<>(entities);
    }
}
