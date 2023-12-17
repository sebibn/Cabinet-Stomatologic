package servicii;

import domeniu.Pacient;
import domeniu.Programare;
import exceptii.IDNeduplicatException;

import java.io.Serializable;
import java.util.List;

public interface PacientService extends Serializable {
    void adaugaPacient(Pacient pacient) throws IDNeduplicatException;
    Pacient gasestePacientDupaId(int id);
    List<Pacient> listaPacienti();
    void actualizeazaPacient(Pacient pacient);
    void stergePacient(Pacient pacient);
    void afiseazaZileDeLaUltimaProgramare(List<Pacient> pacienti, List<Programare> programari);
}

