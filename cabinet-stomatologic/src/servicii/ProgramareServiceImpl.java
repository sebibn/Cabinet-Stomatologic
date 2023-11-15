package servicii;

import domeniu.Pacient;
import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.NuExistaException;
import repository.ProgramareTextFileRepository;
import repository.Repository;

import java.util.Date;
import java.util.List;

public class ProgramareServiceImpl<T> implements ProgramareService {
    private final Repository<T> programareRepository;

    public ProgramareServiceImpl(Repository<T> programareRepository) {
        this.programareRepository = programareRepository;
    }

    @Override
    public void adaugaProgramare(Programare programare) throws DateSuprapuseException {
        try{
            if (verificaDateSuprapuse(programare.getData(), programare.getOra())){
                throw new DateSuprapuseException("Exista deja o programare la data si ora introdusa.");
            }
            programareRepository.adauga((T) programare);
        }
        catch (DateSuprapuseException e){
            System.out.println("Eroare: " + e.getMessage());
        }

    }

    @Override
    public Programare gasesteProgramareDupaId(int id) {
        return (Programare) programareRepository.gasesteDupaId(id);
    }

    @Override
    public List<Programare> listaProgramari() {
        return (List<Programare>) programareRepository.listaEntitati();
    }

    @Override
    public void actualizeazaProgramare(Programare programare) throws NuExistaException {
        try{
            if (verificaExistaProgramare(programare.getId())){
                throw new NuExistaException("Nu exista programarea cu id-ul dat.");
            }
            if (verificaDateSuprapuse(programare.getData(), programare.getOra())){
                throw new DateSuprapuseException("Exista deja o programare la data si ora introdusa.");
            }
            programareRepository.actualizeaza((T) programare);
        }
        catch (NuExistaException | DateSuprapuseException e){
            System.out.println("Eroare: " + e.getMessage());
        }

    }

    @Override
    public void stergeProgramare(int idProgramare) throws NuExistaException {
        if (verificaExistaProgramare(idProgramare)){
            throw new NuExistaException("Nu exista programarea cu id-ul dat.");
        }
        Programare programare = gasesteProgramareDupaId(idProgramare);
        programareRepository.sterge((T) programare);

//        try{
//            if (verificaExistaProgramare(idProgramare)){
//                throw new NuExistaException("Nu exista programarea cu id-ul dat.");
//            }
//            Programare programare = gasesteProgramareDupaId(idProgramare);
//            programareRepository.sterge((T) programare);
//        }
//        catch (NuExistaException e) {
//            System.out.println("Eroare: " + e.getMessage());
//        }
    }

    private boolean verificaExistaProgramare(int id) {
        List<Programare> programari = listaProgramari();

        for (Programare programare : programari) {
            if (programare.getId() == id) {
                return false; // Am gasit programare
            }
        }
        return true; // Nu exista programare
    }

    private boolean verificaDateSuprapuse(Date data, String ora) {
        List<Programare> programari = listaProgramari();

        for (Programare programare : programari) {
            if (programare.getData().equals(data) && programare.getOra().equals(ora)) {
                return true;
            }
        }
        return false;
    }
}
