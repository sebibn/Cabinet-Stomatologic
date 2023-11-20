import domeniu.Pacient;
import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.IDNeduplicatException;
import exceptii.NuExistaException;
import interfata.CabinetStomatologicUI;
import repository.*;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;
import teste.Tests;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException, IDNeduplicatException, IOException, NuExistaException, DateSuprapuseException {
        String repositoryType = Settings.getRepositoryType();
        String patientsFileName = Settings.getPatientsFileName();
        String appointmentsFileName = Settings.getAppointmentsFileName();

        PacientService pacientService;
        ProgramareService programareService;

        if ("binary".equals(repositoryType)) {
            BinaryFileRepository<Object> pacientRepository = new BinaryFileRepository<>(patientsFileName);
            BinaryFileRepository<Object> programareRepository = new BinaryFileRepository<>(appointmentsFileName);
            pacientService = new PacientServiceImpl<>(pacientRepository);
            programareService = new ProgramareServiceImpl<>(programareRepository);
        } else if ("text".equals(repositoryType)) {
            PacientTextFileRepository pacientRepository = new PacientTextFileRepository(patientsFileName);
            ProgramareTextFileRepository programareRepository = new ProgramareTextFileRepository(appointmentsFileName, pacientRepository);
            pacientService = new PacientServiceImpl<>(pacientRepository);
            programareService = new ProgramareServiceImpl<>(programareRepository);
        } else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }

        CabinetStomatologicUI ui = new CabinetStomatologicUI(pacientService, programareService);

        Tests.allTests();

        ui.start();

        //x
        // Adaugare pacienti
        //adaugaPacienti(pacientService);

        // Adaugare programari
        //adaugaProgramari(programareService);
    }

//    public static void adaugaPacienti(PacientService pacientService) throws IDNeduplicatException {
//        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
//        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);
//        Pacient pacient3 = new Pacient(3,"Matei","Andrei",28);
//        Pacient pacient4 = new Pacient(4,"Marcel","David",23);
//        Pacient pacient5 = new Pacient(5,"Bunta","Sebastian",15);
//
//        pacientService.adaugaPacient(pacient1);
//        pacientService.adaugaPacient(pacient2);
//        pacientService.adaugaPacient(pacient3);
//        pacientService.adaugaPacient(pacient4);
//        pacientService.adaugaPacient(pacient5);
//    }
//
//    public static void adaugaProgramari(ProgramareService programareService) throws ParseException {
//        String dataString = "";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
//        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);
//        Pacient pacient3 = new Pacient(3,"Matei","Andrei",28);
//        Pacient pacient4 = new Pacient(4,"Marcel","David",23);
//        Pacient pacient5 = new Pacient(5,"Bunta","Sebastian",15);
//
//        dataString="2020-12-12";
//        Date dataProgramare = dateFormat.parse(dataString);
//        Programare programare1 = new Programare(1,pacient4,dataProgramare,"18:45","scop test 1");
//
//        dataString="2022-10-05";
//        dataProgramare = dateFormat.parse(dataString);
//        Programare programare2 = new Programare(2,pacient3,dataProgramare,"12:30","scop test 2");
//
//        dataString="2023-06-30";
//        dataProgramare = dateFormat.parse(dataString);
//        Programare programare3 = new Programare(3,pacient5,dataProgramare,"14:30","scop test 3");
//
//        dataString="2021-20-03";
//        dataProgramare = dateFormat.parse(dataString);
//        Programare programare4 = new Programare(4,pacient2,dataProgramare,"09:40","scop test 4");
//
//        dataString="2019-12-30";
//        dataProgramare = dateFormat.parse(dataString);
//        Programare programare5 = new Programare(5,pacient1,dataProgramare,"10:20","scop test 5");
//
//        programareService.adaugaProgramare(programare1);
//        programareService.adaugaProgramare(programare2);
//        programareService.adaugaProgramare(programare3);
//        programareService.adaugaProgramare(programare4);
//        programareService.adaugaProgramare(programare5);
//    }
}