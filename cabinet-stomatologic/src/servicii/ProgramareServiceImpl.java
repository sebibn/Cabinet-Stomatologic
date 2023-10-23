package servicii;

import domeniu.Programare;
import repository.GenericRepository;

import java.util.List;

public class ProgramareServiceImpl implements ProgramareService {
    private GenericRepository<Programare> programareRepository;

    public ProgramareServiceImpl(GenericRepository<Programare> programareRepository) {
        this.programareRepository = programareRepository;
    }

    @Override
    public void adaugaProgramare(Programare programare) {
        programareRepository.adauga(programare);
    }

    @Override
    public Programare gasesteProgramareDupaId(int id) {
        return programareRepository.gasesteDupaId(id);
    }

    @Override
    public List<Programare> listaProgramari() {
        return programareRepository.listaToate();
    }

    @Override
    public void actualizeazaProgramare(Programare programare) {
        programareRepository.actualizeaza(programare);
    }

    @Override
    public void stergeProgramare(int id) {
        Programare programare = programareRepository.gasesteDupaId(id);
        if (programare != null) {
            programareRepository.sterge(programare);
        }
    }
}
