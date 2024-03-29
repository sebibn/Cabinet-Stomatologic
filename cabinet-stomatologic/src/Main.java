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
            CabinetStomatologicUI ui = new CabinetStomatologicUI(pacientService, programareService);

            Tests.allTests();

            ui.start();
        } else if ("text".equals(repositoryType)) {
            PacientTextFileRepository pacientRepository = new PacientTextFileRepository(patientsFileName);
            ProgramareTextFileRepository programareRepository = new ProgramareTextFileRepository(appointmentsFileName, pacientRepository);
            pacientService = new PacientServiceImpl<>(pacientRepository);
            programareService = new ProgramareServiceImpl<>(programareRepository);
            CabinetStomatologicUI ui = new CabinetStomatologicUI(pacientService, programareService);

            Tests.allTests();

            ui.start();
        } else if ("sql".equals(repositoryType)) {
            PacientSQLRepository pacientRepository = new PacientSQLRepository();
            ProgramareSQLRepository programareRepository = new ProgramareSQLRepository();
            pacientRepository.openConnection();
            pacientRepository.createSchema();
            programareRepository.openConnection();
            programareRepository.createSchema();

            pacientService = new PacientServiceImpl<>(pacientRepository);
            programareService = new ProgramareServiceImpl<>(programareRepository);
            CabinetStomatologicUI ui = new CabinetStomatologicUI(pacientService, programareService);

            Tests.allTests();

            ui.start();

            pacientRepository.closeConnection();
            programareRepository.closeConnection();
        }
        else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }
    }
}