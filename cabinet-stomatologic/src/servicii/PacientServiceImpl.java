package servicii;

import domeniu.Pacient;
import repository.GenericRepository;

import java.util.List;

public class PacientServiceImpl implements PacientService {
    private GenericRepository<Pacient> pacientRepository;

    public PacientServiceImpl(GenericRepository<Pacient> pacientRepository) {
        this.pacientRepository = pacientRepository;
    }

    @Override
    public void adaugaPacient(Pacient pacient) {
        pacientRepository.adauga(pacient);
    }

    @Override
    public Pacient gasestePacientDupaId(int id) {
        return pacientRepository.gasesteDupaId(id);
    }

    @Override
    public List<Pacient> listaPacienti() {
        return pacientRepository.listaToate();
    }

    @Override
    public void actualizeazaPacient(Pacient pacient) {
        pacientRepository.actualizeaza(pacient);
    }

    @Override
    public void stergePacient(int id) {
        Pacient pacient = pacientRepository.gasesteDupaId(id);
        if (pacient != null) {
            pacientRepository.sterge(pacient);
        }
    }
}
