import exceptii.DateSuprapuseException;
import exceptii.IDNeduplicatException;
import exceptii.NuExistaException;
import interfata.CabinetStomatologicUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;
import teste.Tests;

import java.io.IOException;
import java.text.ParseException;

public class MainApp extends Application {

    private PacientService pacientService;
    private ProgramareService programareService;

    @Override
    public void start(Stage primaryStage) throws NuExistaException, IDNeduplicatException, IOException, ParseException, DateSuprapuseException {
        String repositoryType = Settings.getRepositoryType();
        String patientsFileName = Settings.getPatientsFileName();
        String appointmentsFileName = Settings.getAppointmentsFileName();


        if ("binary".equals(repositoryType)) {
            BinaryFileRepository<Object> pacientRepository = new BinaryFileRepository<>(patientsFileName);
            BinaryFileRepository<Object> programareRepository = new BinaryFileRepository<>(appointmentsFileName);
            this.pacientService = new PacientServiceImpl<>(pacientRepository);
            this.programareService = new ProgramareServiceImpl<>(programareRepository);
        } else if ("text".equals(repositoryType)) {
            PacientTextFileRepository pacientRepository = new PacientTextFileRepository(patientsFileName);
            ProgramareTextFileRepository programareRepository = new ProgramareTextFileRepository(appointmentsFileName, pacientRepository);
            this.pacientService = new PacientServiceImpl<>(pacientRepository);
            this.programareService = new ProgramareServiceImpl<>(programareRepository);
        } else if ("sql".equals(repositoryType)) {
            PacientSQLRepository pacientRepository = new PacientSQLRepository();
            ProgramareSQLRepository programareRepository = new ProgramareSQLRepository();
            pacientRepository.openConnection();
            pacientRepository.createSchema();
            programareRepository.openConnection();
            programareRepository.createSchema();

            pacientService = new PacientServiceImpl<>(pacientRepository);
            programareService = new ProgramareServiceImpl<>(programareRepository);
        }
        else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }

        Tests.allTests();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CabinetStomatologic.fxml"));
        Parent root = loader.load();
        MyController controller = loader.getController();
        controller.initService(pacientService,programareService);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
