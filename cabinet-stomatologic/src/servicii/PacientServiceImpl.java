package servicii;

import domeniu.Pacient;
import domeniu.Programare;
import exceptii.IDNeduplicatException;
import repository.Repository;

import java.time.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PacientServiceImpl<T> implements PacientService {
    private final Repository<T> pacientRepository;

    public PacientServiceImpl(Repository<T> pacientRepository) {
        this.pacientRepository = pacientRepository;
    }

    @Override
    public void adaugaPacient(Pacient pacient) throws IDNeduplicatException {
        try {
            if (!verificaUnicitateIDPacient(pacient.getId())) {
                throw new IDNeduplicatException("ID-ul pacientului nu este unic.");
            }
            pacientRepository.adauga((T) pacient);
        }
        catch (IDNeduplicatException e){
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    @Override
    public Pacient gasestePacientDupaId(int id) {
        return (Pacient) pacientRepository.gasesteDupaId(id);
    }

    @Override
    public List<Pacient> listaPacienti() { return (List<Pacient>) pacientRepository.listaEntitati();}

    @Override
    public void actualizeazaPacient(Pacient pacient) {
        pacientRepository.actualizeaza((T) pacient);
    }

    @Override
    public void stergePacient(Pacient pacient) {
        if (pacient != null) {
            pacientRepository.sterge((T) pacient);
        }
    }

    private boolean verificaUnicitateIDPacient(int id) {
        List<Pacient> pacienti = listaPacienti();

        for (Pacient pacient : pacienti) {
            if (pacient.getId() == id) {
                return false; // ID-ul nu este unic
            }
        }
        return true; // ID-ul este unic
    }

    private Optional<Programare> ultimaProgramare(Pacient pacient, List<Programare> programari) {
        return programari.stream()
                .filter(programare -> programare.getPacient().equals(pacient))
                .max(Comparator.comparing(Programare::getData));
    }

    public void afiseazaZileDeLaUltimaProgramare(List<Pacient> pacienti, List<Programare> programari) {
        pacienti.stream()
                .filter(pacient -> areProgramari(pacient, programari))
                .sorted(Comparator.comparingLong(pacient -> -ultimaProgramare(pacient, programari)
                        .map(Programare::getData)
                        .map(date -> date.toInstant().toEpochMilli())
                        .orElse(Long.MIN_VALUE)))
                .forEach(pacient -> {
                    ultimaProgramare(pacient, programari).ifPresentOrElse(ultimaProgramare -> {
                        Instant instantUltimaProgramare = ultimaProgramare.getData().toInstant();
                        Instant instantNow = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

                        long zileDeLaUltimaProgramare = Duration.between(instantUltimaProgramare, instantNow).toDays();

                        System.out.println("Pacient: " + pacient.getNume() + " " + pacient.getPrenume() +
                                ", Ultima programare: " + ultimaProgramare.getData() +
                                ", Zile de la ultima programare: " + zileDeLaUltimaProgramare);
                    }, () -> System.out.println("Pacient: " + pacient.getNume() + " " + pacient.getPrenume() +
                            " nu are programari."));
                });
    }


    private boolean areProgramari(Pacient pacient, List<Programare> programari) {
        return programari.stream().anyMatch(programare -> programare.getPacient().equals(pacient));
    }
}
