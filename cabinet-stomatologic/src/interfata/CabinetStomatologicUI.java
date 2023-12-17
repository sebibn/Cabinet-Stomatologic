package interfata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import domeniu.Pacient;
import domeniu.Programare;
import servicii.PacientService;
import servicii.ProgramareService;
import exceptii.IDNeduplicatException;
import exceptii.NuExistaException;
import exceptii.DateSuprapuseException;

public class CabinetStomatologicUI {
    private final PacientService pacientService;
    private final ProgramareService programareService;
    private final Scanner scanner;

    public CabinetStomatologicUI(PacientService pacientService, ProgramareService programareService) {
        this.pacientService = pacientService;
        this.programareService = programareService;
        this.scanner = new Scanner(System.in);
    }

    public void start() throws IDNeduplicatException {
        while (true) {
            System.out.println("\n");
            System.out.println("Meniu:");
            System.out.println("1.  Adauga pacient");
            System.out.println("2.  Adauga programare");
            System.out.println("3.  Afisare pacienti");
            System.out.println("4.  Afisare programari");
            System.out.println("5.  Actualizeaza pacient");
            System.out.println("6.  Actualizeaza programare");
            System.out.println("7.  Sterge pacient");
            System.out.println("8.  Sterge programare");
            System.out.println("9.  Numar programari pentru fiecare pacient");
            System.out.println("10. Numar programari pentru fiecare luna din an");
            System.out.println("11. Cele mai aglomerate luni");
            System.out.println("12. Zile de la ultima programare pt fiecare pacient");
            System.out.println("0. Iesire");
            System.out.print("Alegeti o optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    adaugaPacient();
                    break;
                case 2:
                    try {
                        adaugaProgramare();
                    } catch (IDNeduplicatException | NuExistaException | DateSuprapuseException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 3:
                    afiseazaPacienti();
                    break;
                case 4:
                    afiseazaProgramari();
                    break;
                case 5:
                    try {
                        actualizeazaPacient();
                    } catch (NuExistaException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        actualizeazaProgramare();
                    } catch (NuExistaException | DateSuprapuseException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        stergePacient();
                    } catch (NuExistaException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        stergeProgramare();
                    } catch (NuExistaException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case 9:
                    NumarProgramariFiecarePacient();
                    break;
                case 10:
                    NumarProgramariFiecareLunaAn();
                    break;
                case 11:
                    CeleMaiAglomerateLuni();
                    break;
                case 12:
                    ZileDeLaUltimaProgramare();
                    break;
                case 0:
                    System.out.println("Programul se va inchide.");
                    return;
                default:
                    System.out.println("Optiune invalida, reincercati.");
            }
        }
    }

    private boolean verificaUnicitateIDProgramare(int id) {
        List<Programare> programari = programareService.listaProgramari();

        for (Programare programare : programari) {
            if (programare.getId() == id) {
                return false; // ID-ul nu este unic
            }
        }
        return true; // ID-ul este unic
    }

    private boolean verificaExistaPacient(int id) {
        List<Pacient> pacienti = pacientService.listaPacienti();

        for (Pacient pacient : pacienti) {
            if(pacient.getId() == id)
                return false; // Am gasit pacient
        }
        return true; // Nu exista pacient
    }

    private void adaugaPacient() throws IDNeduplicatException {
        System.out.println("\n");

        System.out.print("Introduceti ID-ul pacientului: ");
        int id = scanner.nextInt();

        System.out.print("Introduceti numele pacientului: ");
        String nume = scanner.next();

        System.out.print("Introduceti prenumele pacientului: ");
        String prenume = scanner.next();

        System.out.print("Introduceti varsta pacientului: ");
        int varsta = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = new Pacient(id, nume, prenume, varsta);
        pacientService.adaugaPacient(pacient);
        System.out.println("Pacient adaugat cu succes!");
    }

    private void adaugaProgramare() throws IDNeduplicatException, NuExistaException, DateSuprapuseException {
        System.out.println("\n");

        System.out.print("Introduceti ID-ul programarii: ");
        int idProgramare = scanner.nextInt();
        scanner.nextLine();

        if (!verificaUnicitateIDProgramare(idProgramare)) {
            throw new IDNeduplicatException("ID-ul programării nu este unic.");
        }

        System.out.print("Introduceti ID-ul pacientului pentru programare: ");
        int idPacient = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (verificaExistaPacient(idPacient)){
            throw new NuExistaException("Nu exista pacientul cu ID-ul dat.");
        }

        System.out.print("Introduceti data programarii (YYYY-MM-DD): ");
        String dataInput = scanner.nextLine();
        Date dataProgramare;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataProgramare = dateFormat.parse(dataInput);
        } catch (ParseException e) {
            System.out.println("Format de data incorect. Utilizati formatul 'YYYY-MM-DD'.");
            return;
        }

        System.out.print("Introduceti ora programarii (HH:mm): ");
        String ora = scanner.nextLine();

        System.out.print("Introduceti scopul programarii: ");
        String scopulProgramarii = scanner.nextLine();

        Programare programare = new Programare(idProgramare, pacient, dataProgramare, ora, scopulProgramarii);
        programareService.adaugaProgramare(programare);
        System.out.println("Programare adaugata cu succes!");
    }

    private void afiseazaPacienti() {
        System.out.println("\n");
        List<Pacient> pacienti = pacientService.listaPacienti();
        if (pacienti.isEmpty()) {
            System.out.println("Nu exista pacienti inregistrati.");
        } else {
            System.out.println("Lista de pacienti:");
            for (Pacient pacient : pacienti) {
                System.out.println("ID: " + pacient.getId() + ", Nume: " + pacient.getNume() + ", Prenume: " + pacient.getPrenume() + ", Varsta: " + pacient.getVarsta());
            }
        }
    }

    private void afiseazaProgramari() {
        System.out.println("\n");
        List<Programare> programari = programareService.listaProgramari();
        if (programari.isEmpty()) {
            System.out.println("Nu exista programari inregistrate.");
        } else {
            System.out.println("Lista de programari:");
            for (Programare programare : programari) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatata = dateFormat.format(programare.getData());
                System.out.println("ID: " + programare.getId() + ", Pacient: " + programare.getPacient().getNume() + " " + programare.getPacient().getPrenume() + ", Data: " + dataFormatata + ", Ora: " + programare.getOra() + ", Scop: " + programare.getScopulProgramarii());
            }
        }
    }

    private void actualizeazaPacient() throws NuExistaException {
        System.out.print("Introduceti ID-ul pacientului pe care doriti sa-l actualizati: ");
        int idPacient = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (verificaExistaPacient(idPacient)){
            throw new NuExistaException("Nu exista pacientul cu ID-ul dat.");
        }

        System.out.print("Introduceti numele nou al pacientului: ");
        String numeNou = scanner.nextLine();

        System.out.print("Introduceti prenumele nou al pacientului: ");
        String prenumeNou = scanner.nextLine();

        System.out.print("Introduceti noua varsta a pacientului: ");
        int varstaNoua = scanner.nextInt();

        scanner.nextLine();

        pacient.setNume(numeNou);
        pacient.setPrenume(prenumeNou);
        pacient.setVarsta(varstaNoua);

        pacientService.actualizeazaPacient(pacient);
        System.out.println("Pacient actualizat cu succes!");
    }

    private void actualizeazaProgramare() throws NuExistaException, DateSuprapuseException {
        System.out.print("Introduceti ID-ul programarii pe care doriti sa o actualizati: ");
        int idProgramare = scanner.nextInt();
        scanner.nextLine();

        Programare programare = programareService.gasesteProgramareDupaId(idProgramare);

        System.out.print("Introduceti ID-ul nou al pacientului pentru programare: ");
        int idPacient = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (pacient == null) {
            System.out.println("Pacientul cu ID-ul " + idPacient + " nu exista.");
            return;
        }

        System.out.print("Introduceti noua data a programarii (YYYY-MM-DD): ");
        String dataInput = scanner.nextLine();
        Date dataProgramareNou;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataProgramareNou = dateFormat.parse(dataInput);
        } catch (ParseException e) {
            System.out.println("Format de data incorect. Utilizati formatul 'YYYY-MM-DD'.");
            return;
        }

        System.out.print("Introduceti noua ora programarii (HH:mm): ");
        String oraNou = scanner.nextLine();

        System.out.print("Introduceti noul scop al programarii: ");
        String scopulProgramariiNou = scanner.nextLine();

        programare.setPacient(pacient);
        programare.setData(dataProgramareNou);
        programare.setOra(oraNou);
        programare.setScopulProgramarii(scopulProgramariiNou);

        programareService.actualizeazaProgramare(programare);

        System.out.println("Programare actualizata cu succes!");
    }

    private void stergePacient() throws NuExistaException {
        System.out.print("Introduceti ID-ul pacientului pe care doriti sa-l stergeti: ");
        int idPacient = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (verificaExistaPacient(idPacient)){
            throw new NuExistaException("Nu exista pacientul cu ID-ul dat.");
        }

        List<Programare> programari = programareService.listaProgramari();
        List<Integer> programariDeSters = new ArrayList<>();

        for (Programare programare : programari) {
//            System.out.println("Am intrat in for");
            if (programare.getPacient().getId() == pacient.getId()) {
//                System.out.println("Am gasit programare de sters");
                programariDeSters.add(programare.getId());
            }
        }

        for (Integer id : programariDeSters) {
            Programare programare = programareService.gasesteProgramareDupaId(id);
//            System.out.println(programare + " <- se sterge");
            programareService.stergeProgramare(programare.getId());
            programari.remove(programare);
        }

        pacientService.stergePacient(pacient);
        System.out.println("Pacient sters cu succes!");
    }

    private void stergeProgramare() throws NuExistaException {
        System.out.print("Introduceti ID-ul programarii pe care doriti sa o stergeti: ");
        int idProgramare = scanner.nextInt();
        scanner.nextLine();

        programareService.stergeProgramare(idProgramare);
        System.out.println("Programare stearsa cu succes!");
    }

    private void NumarProgramariFiecarePacient() {
        programareService.afiseazaNumarProgramariFiecarePacient();
    }

    private void NumarProgramariFiecareLunaAn() {
        programareService.afiseazaNumarProgramariFiecareLunaAn();
    }

    private void CeleMaiAglomerateLuni() {
        programareService.afiseazaCeleMaiAglomerateLuni();
    }

    private void ZileDeLaUltimaProgramare() {
        List<Pacient> pacienti = pacientService.listaPacienti();
        List<Programare> programari = programareService.listaProgramari();

        pacientService.afiseazaZileDeLaUltimaProgramare(pacienti, programari);
    }
}

