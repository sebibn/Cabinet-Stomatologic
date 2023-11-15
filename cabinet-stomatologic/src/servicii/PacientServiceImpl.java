package servicii;

import domeniu.Pacient;
import exceptii.IDNeduplicatException;
import repository.BinaryFileRepository;
import repository.PacientRepository;
import repository.Repository;

import java.io.Serializable;
import java.util.List;

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
}
