import domeniu.Pacient;
import domeniu.Programare;
import exceptii.DateSuprapuseException;
import exceptii.IDNeduplicatException;
import exceptii.NuExistaException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import repository.JDBC;
import servicii.PacientService;
import servicii.ProgramareService;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MyController {

    private PacientService pacientService;
    private ProgramareService programareService;
    private JDBC db;

    public void initService(PacientService pacientService, ProgramareService programareService) {
        this.pacientService = pacientService;
        this.programareService = programareService;
    }

    //Butoane pt Pacient
    @FXML
    private Tab tabPacienti;
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text21;
    @FXML
    private Text text31;
    @FXML
    private Text text41;
    @FXML
    private Button button1;
    @FXML
    private Button button3;
    @FXML
    private Button button5;

    @FXML
    private Button afisarePacienti;

    //Text field pt pacienti si programari
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;

    //+text field in plus pt programari
    @FXML
    private TextField textField5;


    //Butoane pt Programari
    @FXML
    private Tab tabProgramari;
    @FXML
    private Text text5;
    @FXML
    private Text text6;
    @FXML
    private Text text7;
    @FXML
    private Text text8;
    @FXML
    private Text text9;
    @FXML
    private Text text61;
    @FXML
    private Text text71;
    @FXML
    private Text text81;
    @FXML
    private Text text91;
    @FXML
    private Button button2;
    @FXML
    private Button button4;
    @FXML
    private Button button6;

    //Afisare pacienti/programari
    @FXML
    private ListView lista;

    @FXML
    private void afisarePacienti() {
        initialize();
        lista.setVisible(true);
        List<Pacient> pacienti = pacientService.listaPacienti();
        for (Pacient pacient : pacienti){
            lista.getItems().add("ID: " + pacient.getId() + ", Nume: " + pacient.getNume() + ", Prenume: " + pacient.getPrenume() + ", Varsta: " + pacient.getVarsta());
        }
    }

    @FXML
    private void afisareProgramari() {
        initialize();
        lista.setVisible(true);
        List<Programare> programari = programareService.listaProgramari();
        for(Programare programare : programari){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dataFormatata = dateFormat.format(programare.getData());
            lista.getItems().add("ID: " + programare.getId() + ", Pacient: " + programare.getPacient().getNume() + " " + programare.getPacient().getPrenume() + ", Data: " + dataFormatata + ", Ora: " + programare.getOra() + ", Scop: " + programare.getScopulProgramarii());
        }
    }
    @FXML
    private void onPressAdaugaPacient() throws IDNeduplicatException {
        int id = Integer.parseInt(textField1.getText());

        List<Pacient> pacienti = pacientService.listaPacienti();

        for (Pacient pacient : pacienti) {
            if (pacient.getId() == id) {
                Error("Eroare la adaugarea pacientului:", "ID-ul pacientului de adaugat nu este unic.");
                break;
            }
        }

        String nume = textField2.getText();
        String prenume = textField3.getText();
        int varsta = Integer.parseInt(textField4.getText());

        Pacient pacient = new Pacient(id, nume, prenume, varsta);
        pacientService.adaugaPacient(pacient);

        clearTextFields();
    }

    @FXML
    private void onPressAdaugaProgramare() throws IDNeduplicatException, DateSuprapuseException {
        int idProgramare = Integer.parseInt(textField1.getText());

        List<Programare> programari = programareService.listaProgramari();

        for (Programare programare : programari) {
            if (programare.getId() == idProgramare) {
                Error("Eroare la adaugarea programarii:", "ID-ul programării nu este unic.");
                break;
            }
        }

        int idPacient = Integer.parseInt(textField2.getText());

        List<Pacient> pacienti = pacientService.listaPacienti();

        int ok=0;
        for (Pacient pacient : pacienti) {
            if(pacient.getId() == idPacient)
                ok=1;
        }
        if(ok==0){
            Error("Eroare la adaugarea programarii:", "Nu exista pacient cu ID-ul specificat.");
        }

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        String dataInput = textField3.getText();
        Date dataProgramare;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataProgramare = dateFormat.parse(dataInput);
        } catch (ParseException e) {
            Error("Eroare la adaugarea programarii:", "Format de data incorect. Utilizati formatul 'YYYY-MM-DD'.");
            return;
        }

        String ora = textField4.getText();

        String scopulProgramarii = textField5.getText();

        Programare programare = new Programare(idProgramare, pacient, dataProgramare, ora, scopulProgramarii);
        programareService.adaugaProgramare(programare);

        clearTextFields();
    }

    @FXML
    private void onPressActualizeazaPacient() {
        int id = Integer.parseInt(textField1.getText());

        List<Pacient> pacienti = pacientService.listaPacienti();

        int ok=0;
        for (Pacient pacient : pacienti) {
            if (pacient.getId() == id) {
                ok=1;
                break;
            }
        }
        if(ok==0){
            Error("Eroare la actualizarea pacientului:", "Nu exista pacient cu ID-ul dat.");
        }


        String nume = textField2.getText();
        String prenume = textField3.getText();
        int varsta = Integer.parseInt(textField4.getText());

        Pacient pacient = new Pacient(id, nume, prenume, varsta);
        pacientService.actualizeazaPacient(pacient);

        clearTextFields();
    }

    @FXML
    private void onPressActualizeazaProgramare() throws NuExistaException {
        int idProgramare = Integer.parseInt(textField1.getText());

        Programare programare = programareService.gasesteProgramareDupaId(idProgramare);

        int idPacient = Integer.parseInt(textField2.getText());

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        if (pacient == null) {
            Error("Eroare la actualizarea programarii:", "Nu exista pacient cu ID-ul dat.");
            return;
        }

        String dataInput = textField3.getText();
        Date dataProgramareNou;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dataProgramareNou = dateFormat.parse(dataInput);
        } catch (ParseException e) {
            Error("Eroare la actualizarea programarii:", "Format de data incorect. Utilizati formatul 'YYYY-MM-DD'.");
            return;
        }

        String oraNou = textField4.getText();

        List<Programare> programari = programareService.listaProgramari();

        for (Programare programarex : programari) {
            if (programarex.getData().equals(dataProgramareNou) && programare.getOra().equals(oraNou)) {
                Error("Eroare la actualizarea programarii:", "Exista deja o programare la data si ora introdusa.");
            }
        }

        String scopulProgramariiNou = textField5.getText();

        programare.setPacient(pacient);
        programare.setData(dataProgramareNou);
        programare.setOra(oraNou);
        programare.setScopulProgramarii(scopulProgramariiNou);

        programareService.actualizeazaProgramare(programare);

        clearTextFields();
    }

    @FXML
    private void onPressStergePacient() throws NuExistaException {
        int idPacient = Integer.parseInt(textField1.getText());

        Pacient pacient = pacientService.gasestePacientDupaId(idPacient);

        List<Pacient> pacienti = pacientService.listaPacienti();

        int ok=0;
        for (Pacient pacientx : pacienti) {
            if (pacientx.getId() == idPacient) {
                ok=1;
                break;
            }
        }
        if(ok==0){
            Error("Eroare la stergerea pacientului:", "Nu exista pacient cu ID-ul dat.");
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

        textField1.clear();
    }

    @FXML
    private void onPressStergeProgramare() throws NuExistaException {
        int idProgramare = Integer.parseInt(textField1.getText());

        int ok=0;
        List<Programare> programari = programareService.listaProgramari();

        for(Programare programare : programari) {
            if(programare.getId() == idProgramare){
                ok=1;
                break;
            }
        }
        if(ok==0){
            Error("Eroare la stergerea programarii:", "Nu exista programare cu ID-ul dat.");
        }

        programareService.stergeProgramare(idProgramare);

        textField1.clear();
    }

    @FXML
    private void Error(String header, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setContentText(mesaj);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @FXML
    private void adaugaPacient() {
        initialize();

        button1.setVisible(true);
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
        text1.setVisible(true);
        text2.setVisible(true);
        text3.setVisible(true);
        text4.setVisible(true);
    }

    @FXML
    private void adaugaProgramare() {
        initialize();

        button2.setVisible(true);
        text5.setVisible(true);
        text6.setVisible(true);
        text7.setVisible(true);
        text8.setVisible(true);
        text9.setVisible(true);
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
        textField5.setVisible(true);
    }

    @FXML
    private void actualizeazaPacient() {
        initialize();

        button3.setVisible(true);
        text1.setVisible(true);
        text21.setVisible(true);
        text31.setVisible(true);
        text41.setVisible(true);
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
    }

    @FXML
    private void actualizeazaProgramare() {
        initialize();

        button4.setVisible(true);
        text5.setVisible(true);
        text61.setVisible(true);
        text71.setVisible(true);
        text81.setVisible(true);
        text91.setVisible(true);
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
        textField5.setVisible(true);
    }

    @FXML
    private void stergePacient() {
        initialize();

        button5.setVisible(true);
        text1.setVisible(true);
        textField1.setVisible(true);
    }

    @FXML
    private void stergeProgamare() {
        initialize();

        button6.setVisible(true);
        text5.setVisible(true);
        textField1.setVisible(true);
    }

    @FXML
    private void clearTextFields() {
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
        textField5.clear();
    }

    @FXML
    private void initialize() {
        text1.setVisible(false);
        text2.setVisible(false);
        text3.setVisible(false);
        text4.setVisible(false);
        text5.setVisible(false);
        text6.setVisible(false);
        text7.setVisible(false);
        text8.setVisible(false);
        text9.setVisible(false);
        text21.setVisible(false);
        text31.setVisible(false);
        text41.setVisible(false);
        text61.setVisible(false);
        text71.setVisible(false);
        text81.setVisible(false);
        text91.setVisible(false);

        button1.setVisible(false);
        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);
        button5.setVisible(false);
        button6.setVisible(false);

        textField1.setVisible(false);
        textField2.setVisible(false);
        textField3.setVisible(false);
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
        textField5.clear();

        lista.setVisible(false);
        lista.getItems().clear();
    }
}
