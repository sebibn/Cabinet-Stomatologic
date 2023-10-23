package interfata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import domeniu.Pacient;
import domeniu.Programare;
import repository.GenericRepository;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;

public class CabinetStomatologicUI {
    private PacientService pacientService;
    private ProgramareService programareService;
    private Scanner scanner;

    public CabinetStomatologicUI(PacientService pacientService, ProgramareService programareService) {
        this.pacientService = pacientService;
        this.programareService = programareService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n");
            System.out.println("Meniu:");
            System.out.println("1. Adauga pacient");
            System.out.println("2. Adauga programare");
            System.out.println("3. Afisare pacienti");
            System.out.println("4. Afisare programari");
            System.out.println("5. Iesire");
            System.out.print("Alegeti o optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    adaugaPacient();
                    break;
                case 2:
                    adaugaProgramare();
                    break;
                case 3:
                    afiseazaPacienti();
                    break;
                case 4:
                    afiseazaProgramari();
                    break;
                case 5:
                    System.out.println("Programul se va inchide.");
                    return;
                default:
                    System.out.println("Optiune invalida, reincercati.");
            }
        }
    }

    private void adaugaPacient() {
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

    private void adaugaProgramare() {
        System.out.println("\n");
        System.out.print("Introduceti ID-ul pacientului pentru programare: ");
        int idPacient = scanner.nextInt();
        scanner.nextLine();

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (pacient == null) {
            System.out.println("Pacientul cu ID-ul " + idPacient + " nu exista.");
            return;
        }

        System.out.print("Introduceti data programarii (YYYY-MM-DD): ");
        String dataInput = scanner.nextLine();
        Date dataProgramare = null;

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

        Programare programare = new Programare(idPacient, pacient, dataProgramare, ora, scopulProgramarii);
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
}

