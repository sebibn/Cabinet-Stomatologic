package servicii;

import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.NuExistaException;

import java.util.List;

public interface ProgramareService {
    void adaugaProgramare(Programare programare) throws DateSuprapuseException;
    Programare gasesteProgramareDupaId(int id);
    List<Programare> listaProgramari();
    void actualizeazaProgramare(Programare programare) throws NuExistaException;
    void stergeProgramare(int idProgramare) throws NuExistaException;
}

