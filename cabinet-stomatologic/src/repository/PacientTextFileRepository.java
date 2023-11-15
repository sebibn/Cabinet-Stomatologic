package repository;

import domeniu.Pacient;
import domeniu.Programare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacientTextFileRepository extends GenericRepository<Pacient> implements Repository<Pacient> {

    private final String filePath;

    private final List<Pacient> pacienti = new ArrayList<>();

    public PacientTextFileRepository(String filePath) {
        this.filePath = filePath;
        incarcaDateDinFisier();
    }

    private void incarcaDateDinFisier() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String nume = parts[1];
                    String prenume = parts[2];
                    int varsta = Integer.parseInt(parts[3]);
                    Pacient pacient = new Pacient(id, nume, prenume, varsta);
                    adauga(pacient);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salveazaDateInFisier() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Pacient pacient : listaToate()) {
                String line = pacient.getId() + "," + pacient.getNume() + "," + pacient.getPrenume() + "," + pacient.getVarsta();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adauga(Pacient pacient) {
        super.adauga(pacient);
        pacienti.add(pacient);
        salveazaDateInFisier();
    }

    @Override
    public void actualizeaza(Pacient pacient) {
        super.actualizeaza(pacient);
        salveazaDateInFisier();
    }

    @Override
    public void sterge(Pacient pacient) {
        super.sterge(pacient);
        pacienti.remove(pacient);
        salveazaDateInFisier();
    }

    public Pacient gasesteDupaId(int id) {
        return pacienti.stream()
                .filter(pacient -> pacient.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Pacient> listaEntitati() {
        return new ArrayList<>(pacienti);
    }
}
