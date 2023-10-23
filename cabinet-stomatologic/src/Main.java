import domeniu.Pacient;
import domeniu.Programare;
import interfata.CabinetStomatologicUI;
import repository.GenericRepository;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException {
        GenericRepository<Pacient> pacientGenericRepository = new GenericRepository<>();
        GenericRepository<Programare> programareGenericRepository = new GenericRepository<>();

        PacientService pacientService = new PacientServiceImpl(pacientGenericRepository);
        ProgramareService programareService = new ProgramareServiceImpl(programareGenericRepository);

        CabinetStomatologicUI ui = new CabinetStomatologicUI(pacientService, programareService);

        Pacient pacient1 = new Pacient(1,"Popescu","Maria",20);
        Pacient pacient2 = new Pacient(2,"Pop","Alin",31);
        Pacient pacient3 = new Pacient(3,"Matei","Andrei",28);
        Pacient pacient4 = new Pacient(4,"Marcel","David",23);
        Pacient pacient5 = new Pacient(5,"Bunta","Sebastian",15);

        pacientService.adaugaPacient(pacient1);
        pacientService.adaugaPacient(pacient2);
        pacientService.adaugaPacient(pacient3);
        pacientService.adaugaPacient(pacient4);
        pacientService.adaugaPacient(pacient5);

        String dataString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dataString="2020-12-12";
        Date dataProgramare = dateFormat.parse(dataString);
        Programare programare1 = new Programare(1,pacient4,dataProgramare,"18:45","scop test 1");
        dataString="2022-10-05";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare2 = new Programare(2,pacient3,dataProgramare,"12:30","scop test 2");
        dataString="2023-06-30";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare3 = new Programare(3,pacient5,dataProgramare,"14:30","scop test 3");
        dataString="2021-20-03";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare4 = new Programare(4,pacient2,dataProgramare,"09:40","scop test 4");
        dataString="2019-12-30";
        dataProgramare = dateFormat.parse(dataString);
        Programare programare5 = new Programare(5,pacient1,dataProgramare,"10:20","scop test 5");

        programareService.adaugaProgramare(programare1);
        programareService.adaugaProgramare(programare2);
        programareService.adaugaProgramare(programare3);
        programareService.adaugaProgramare(programare4);
        programareService.adaugaProgramare(programare5);

        ui.start();
    }
}