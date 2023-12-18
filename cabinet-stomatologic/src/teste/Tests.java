package teste;

import domeniu.Pacient;
import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.IDNeduplicatException;
import exceptii.NuExistaException;
import interfata.CabinetStomatologicUI;
import org.junit.jupiter.api.Test;
import repository.*;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.IDN;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {
    /// REPOSITORIES TESTS
    @Test
    public static void BinaryRepoPacient() throws IOException {
        deleteFileContent("PacientiTest.dat");

        BinaryFileRepository<Object> pacientRepository = new BinaryFileRepository<>("PacientiTest.dat");
        Pacient pacient = new Pacient(2,"Popescu","Maria",20);
        pacientRepository.adauga(pacient);
        assertEquals(pacientRepository.listaEntitati().size(), 1);

        pacientRepository.actualizeaza(new Pacient(2,"Pop","Alin",31));
        Pacient pacientGasit = (Pacient) pacientRepository.gasesteDupaId(2);

        assert (Objects.equals(pacientGasit, new Pacient(2,"Pop","Alin",31)));

        pacientRepository.sterge(pacient);
        assertEquals(pacientRepository.listaEntitati().size(), 0);
    }

    @Test
    public static void BinaryRepoProgramare() throws ParseException {
        deleteFileContent("PacientiTest.dat");
        deleteFileContent("ProgramariTest.dat");

        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        BinaryFileRepository<Object> programareRepository = new BinaryFileRepository<>("ProgramariTest.dat");
        BinaryFileRepository<Object> pacientRepository = new BinaryFileRepository<>("PacientiTest.dat");
        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
        pacientRepository.adauga(pacient1);

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare = new Programare(1,pacient1,dataProgramare,"18:45","scop test 1");
        programareRepository.adauga(programare);

        assertEquals(programareRepository.listaEntitati().size(), 1);

        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);

        dataString="2022-10-05";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare2 = new Programare(1,pacient2,dataProgramare,"12:30","scop test 2");

        programareRepository.actualizeaza(programare2);

        Programare programareGasita = (Programare) programareRepository.gasesteDupaId(1);

        assert (Objects.equals(programareGasita, new Programare(1,pacient2,dataProgramare,"12:30","scop test 2")));

        programareRepository.sterge(programare2);
        assertEquals(programareRepository.listaEntitati().size(), 1);

    }

    @Test
    public static void TextFileRepoPacient() throws IOException {
        deleteFileContent("PacientiTest.txt");

        PacientTextFileRepository pacientRepository = new PacientTextFileRepository("PacientiTest.txt");
        Pacient pacient = new Pacient(2,"Popescu","Maria",20);
        pacientRepository.adauga(pacient);
        assertEquals(pacientRepository.listaEntitati().size(), 1);

        pacientRepository.actualizeaza(new Pacient(2,"Pop","Alin",31));
        Pacient pacientGasit = pacientRepository.gasesteDupaId(2);

        assert (Objects.equals(pacientGasit, new Pacient(2,"Pop","Alin",31)));

        pacientRepository.sterge(pacient);
        assertEquals(pacientRepository.listaEntitati().size(), 0);
    }

    @Test
    public static void TextFileRepoProgramare() throws ParseException {
        deleteFileContent("PacientiTest.txt");
        deleteFileContent("ProgramariTest.txt");

        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        PacientTextFileRepository pacientRepository = new PacientTextFileRepository("PacientiTest.txt");
        ProgramareTextFileRepository programareRepository = new ProgramareTextFileRepository("ProgramariTest.txt", pacientRepository);
        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
        pacientRepository.adauga(pacient1);

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare = new Programare(1,pacient1,dataProgramare,"18:45","scop test 1");
        programareRepository.adauga(programare);

        assertEquals(programareRepository.listaEntitati().size(), 1);

        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);

        dataString="2022-10-05";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare2 = new Programare(1,pacient2,dataProgramare,"12:30","scop test 2");

        programareRepository.actualizeaza(programare2);

        Programare programareGasita = (Programare) programareRepository.gasesteDupaId(1);

        assert (Objects.equals(programareGasita, new Programare(1,pacient2,dataProgramare,"12:30","scop test 2")));

        programareRepository.sterge(programare2);
        assertEquals(programareRepository.listaEntitati().size(), 1);

    }

    @Test
    public static void SQLRepo() throws ParseException {
        PacientSQLRepository pacientRepository = new PacientSQLRepository();
        ProgramareSQLRepository programareRepository = new ProgramareSQLRepository();

        pacientRepository.openConnection();
        programareRepository.openConnection();

        pacientRepository.createSchema();
        programareRepository.createSchema();

        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
        Pacient pacient2 = new Pacient(1,"Pop","Alin",31);

        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare1 = new Programare(1,pacient1,dataProgramare,"18:45","scop test 1");
        Programare programare2 = new Programare(1,pacient2,dataProgramare,"12:30","scop test 2");

        pacientRepository.adauga(pacient1);
        pacientRepository.actualizeaza(pacient2);

        programareRepository.adauga(programare1);
        programareRepository.actualizeaza(programare2);

        List<Pacient> pacienti = pacientRepository.listaEntitati();
        assertEquals(1075384216, pacienti.getFirst().getId());

        List<Programare> programari = programareRepository.listaEntitati();
        assertEquals(2015857860, programari.getFirst().getId());

        Pacient pacientGasit = pacientRepository.gasesteDupaId(1075384216);
        Programare programareGasita = programareRepository.gasesteDupaId(2015857860);

        programareRepository.sterge(programare2);
        pacientRepository.sterge(pacient2);

        pacientRepository.closeConnection();
        programareRepository.closeConnection();
    }


    /// SERVICE TESTS

    @Test
    public static void ServiceTest() throws ParseException, IDNeduplicatException, NuExistaException, DateSuprapuseException {
        BinaryFileRepository<Object> pacientRepository = new BinaryFileRepository<>("PacientiTest.dat");
        BinaryFileRepository<Object> programareRepository = new BinaryFileRepository<>("ProgramariTest.dat");
        PacientService pacientService = new PacientServiceImpl<>(pacientRepository);
        ProgramareService programareService = new ProgramareServiceImpl<>(programareRepository);

        Pacient pacient = new Pacient(1,"Popescu","Maria",20);
        pacientService.adaugaPacient(pacient);

        assertEquals(pacientService.listaPacienti().size(),1);

        pacientService.actualizeazaPacient(new Pacient(1,"Pop","Alin",31));
        assert(Objects.equals(pacientService.listaPacienti().get(0), new Pacient(1,"Pop","Alin",31)));

        pacientService.stergePacient(pacient);
        assertEquals(pacientService.listaPacienti().size(),1);

        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare = new Programare(1,pacient,dataProgramare,"18:45","scop test 1");
        programareService.adaugaProgramare(programare);

        assertEquals(programareService.listaProgramari().size(), 1);

        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);

        dataString="2022-10-05";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare2 = new Programare(1,pacient2,dataProgramare,"12:30","scop test 2");

        programareService.actualizeazaProgramare(programare2);

        Programare programareGasita = programareService.gasesteProgramareDupaId(1);

        assert (Objects.equals(programareGasita, new Programare(1,pacient2,dataProgramare,"12:30","scop test 2")));

        programareService.stergeProgramare(programare2.getId());
        assertEquals(programareService.listaProgramari().size(), 0);

        try{
            pacientService.adaugaPacient(pacient);
        }
        catch (IDNeduplicatException e){
            //
        }

        Pacient pacient3 = new Pacient(3,"Matei","Andrei",28);

        try{
            pacientService.adaugaPacient(pacient3);
        }
        catch (IDNeduplicatException e){
            //
        }

        Pacient pacientGasitService = pacientService.gasestePacientDupaId(3);
        assertEquals(pacientGasitService.getId(), 3);

        try{
            programareService.stergeProgramare(29);
        }
        catch (NuExistaException e){
            //
        }

        List<Pacient> pacienti = pacientService.listaPacienti();
        List<Programare> programari = programareService.listaProgramari();

        pacientService.afiseazaZileDeLaUltimaProgramare(pacienti, programari);

        programareService.afiseazaCeleMaiAglomerateLuni();
        programareService.afiseazaNumarProgramariFiecareLunaAn();
        programareService.afiseazaNumarProgramariFiecarePacient();
    }

    /// Masina

    @Test
    public static void PacientTest(){
        Pacient pacient = new Pacient(1,"Popescu","Maria",20);

        assertEquals(pacient.getId(), 1);
        assertEquals(pacient.getNume(), "Popescu");
        assertEquals(pacient.getPrenume(), "Maria");
        assertEquals(pacient.getVarsta(), 20);

        pacient.setNume("Papucel");
        assertEquals(pacient.getNume(), "Papucel");

        pacient.setPrenume("Marcelio");
        assertEquals(pacient.getPrenume(), "Marcelio");

        pacient.setVarsta(37);
        assertEquals(pacient.getVarsta(), 37);
    }

    /// Inchiriere

    @Test
    public static void ProgramareTest() throws ParseException {
        Pacient pacient = new Pacient(1,"Popescu","Maria",20);
        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare = new Programare(1,pacient,dataProgramare,"18:45","scop test 1");

        assertEquals(programare.getId(), 1);
        assertEquals(programare.getPacient().getId(), 1);
        assertEquals(programare.getPacient().getNume(), "Popescu");
        assertEquals(programare.getPacient().getPrenume(), "Maria");
        assertEquals(programare.getPacient().getVarsta(), 20);
        assertEquals(programare.getData(), dataProgramare);
        assertEquals(programare.getOra(), "18:45");
        assertEquals(programare.getScopulProgramarii(), "scop test 1");

        programare.setPacient(new Pacient(2,"Popescu","Anghel",38));
        assertEquals(programare.getPacient().getId(), 2);

        programare.setOra("12:12");
        assertEquals(programare.getOra(), "12:12");

        programare.setScopulProgramarii("Scop test Setter");
        assertEquals(programare.getScopulProgramarii(), "Scop test Setter");

        dataString="2024-12-12";
        Date dataProgramare2 = dateFormat.parse(dataString);
        programare.setData(dataProgramare2);
        assertEquals(programare.getData(), dataProgramare2);
    }

    static void deleteFileContent(String fileName){
        try (BufferedWriter bf = Files.newBufferedWriter(Path.of(fileName),
                StandardOpenOption.TRUNCATE_EXISTING)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void allTests() throws IOException, ParseException, IDNeduplicatException, NuExistaException, DateSuprapuseException {
        BinaryRepoPacient();
        BinaryRepoProgramare();
        TextFileRepoPacient();
        TextFileRepoProgramare();
        SQLRepo();

        ServiceTest();

        PacientTest();
        ProgramareTest();
    }
}