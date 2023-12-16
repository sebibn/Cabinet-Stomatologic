
package repository;
import domeniu.Pacient;
import domeniu.Programare;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgramareTextFileRepository extends GenericRepository<Programare> implements Repository<Programare> {
    private final String filePath;
    private final List<Programare> programari = new ArrayList<>();
    private final PacientTextFileRepository pacientRepository;

    public ProgramareTextFileRepository(String filePath, PacientTextFileRepository pacientRepository) {
        this.filePath = filePath;
        this.pacientRepository = pacientRepository;
        incarcaDateDinFisier();
    }

    private void incarcaDateDinFisier() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    int pacientId = Integer.parseInt(parts[1]);
                    Date data = dateFormat.parse(parts[2]);
                    String ora = parts[3];
                    String scop = parts[4];
                    Pacient pacient = (Pacient) pacientRepository.gasesteDupaId(pacientId);
                    if (pacient != null) {
                        Programare programare = new Programare(id, pacient, data, ora, scop);
                        adauga(programare);
                    } else {
                        System.err.println("Pacientul cu ID-ul " + pacientId + " nu a putut fi găsit.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void salveazaDateInFisier() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Programare programare : listaToate()) {
                Pacient pacient = programare.getPacient();
                String line = programare.getId() + "," + pacient.getId() + "," + programare.getData() + "," + programare.getOra() + "," + programare.getScopulProgramarii();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void adauga(Programare programare) {
        super.adauga(programare);
        programari.add(programare);
        salveazaDateInFisier();
    }

    @Override
    public void actualizeaza(Programare programare) {
        super.actualizeaza(programare);
        salveazaDateInFisier();
    }

    @Override
    public void sterge(Programare programare) {
        super.sterge(programare);
        programari.remove(programare);
        salveazaDateInFisier();
    }

    public Programare gasesteDupaId(int id) {
        return programari.stream()
                .filter(programare -> programare.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Programare> listaEntitati() {
        return new ArrayList<>(programari);
    }
}
