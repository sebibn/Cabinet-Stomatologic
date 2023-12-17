package servicii;

import domeniu.Pacient;
import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.NuExistaException;
import repository.ProgramareTextFileRepository;
import repository.Repository;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.YearMonth;


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

    public void afiseazaNumarProgramariFiecarePacient() {
        List<Programare> programari = listaProgramari();

        // Gruparea programarilor după pacient și calcularea numărului de programări pentru fiecare pacient
        Map<Pacient, Long> numarProgramariPePacient = programari.stream()
                .collect(Collectors.groupingBy(Programare::getPacient, Collectors.counting()));

        // Afișarea rezultatelor în ordine descrescătoare a numărului de programări
        numarProgramariPePacient.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    Pacient pacient = entry.getKey();
                    Long numarProgramari = entry.getValue();
                    System.out.println("Pacient: " + pacient.getNume() + " " + pacient.getPrenume() +
                            ", Număr programări: " + numarProgramari);
                });
    }

        public void afiseazaNumarProgramariFiecareLunaAn() {
        List<Programare> programari = listaProgramari();

        // Gruparea programarilor după lună și calcularea numărului de programări pentru fiecare lună
            Map<YearMonth, Long> numarProgramariPeLuna = programari.stream()
                    .collect(Collectors.groupingBy(
                            programare -> YearMonth.from(programare.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                            Collectors.counting()));


            // Afișarea rezultatelor în ordine descrescătoare a numărului de programări
        numarProgramariPeLuna.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    YearMonth lunaAn = entry.getKey();
                    Long numarProgramari = entry.getValue();
                    System.out.println("Luna: " + lunaAn.getMonth() + " An: " + lunaAn.getYear() +
                            ", Număr programări: " + numarProgramari);
                });
    }

    public void afiseazaCeleMaiAglomerateLuni() {
        List<Programare> programari = listaProgramari();

        // Gruparea programarilor după lună și calcularea numărului total de programări pentru fiecare lună
        Map<YearMonth, Long> numarTotalProgramariPeLuna = programari.stream()
                .collect(Collectors.groupingBy(
                        programare -> YearMonth.from(programare.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                        Collectors.counting()));


        // Afișarea rezultatelor în ordine descrescătoare a numărului de programări
        numarTotalProgramariPeLuna.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    YearMonth lunaAn = entry.getKey();
                    Long numarTotalProgramari = entry.getValue();
                    System.out.println("Luna: " + lunaAn.getMonth() + " An: " + lunaAn.getYear() +
                            ", Număr total programări: " + numarTotalProgramari);
                });
    }
}
