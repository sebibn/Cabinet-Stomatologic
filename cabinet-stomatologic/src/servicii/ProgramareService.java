package servicii;

import domeniu.Programare;
import repository.GenericRepository;
import java.util.List;

public interface ProgramareService {
    void adaugaProgramare(Programare programare);
    Programare gasesteProgramareDupaId(int id);
    List<Programare> listaProgramari();
    void actualizeazaProgramare(Programare programare);
    void stergeProgramare(int id);
}

