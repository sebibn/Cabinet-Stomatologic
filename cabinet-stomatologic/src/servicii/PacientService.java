package servicii;
import domeniu.Pacient;
import repository.GenericRepository;

import java.util.List;

public interface PacientService {
    void adaugaPacient(Pacient pacient);
    Pacient gasestePacientDupaId(int id);
    List<Pacient> listaPacienti();
    void actualizeazaPacient(Pacient pacient);
    void stergePacient(int id);
}

