import domeniu.Pacient;
import domeniu.Programare;
import repository.PacientSQLRepository;
import repository.ProgramareSQLRepository;

import java.security.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DatabasePopulator {

    private static final String[] FIRST_NAMES = {"Maria", "Andreea", "Marcel", "Sebastian", "Alin", "Alexandru", "Elena", "Raluca"};
    private static final String[] LAST_NAMES = {"Popescu", "Marinescu", "Matei", "Popa", "Craciun", "Negru", "Fierar", "Dacia"};
    private static final String[] SCOPS = {"Consultatie", "Curatare", "Extractie", "Dentara", "X-Ray"};

    private static final int NUM_ENTRIES = 100;

    private static final Random random = new Random();

    private static final PacientSQLRepository pacientRepository = new PacientSQLRepository();

    private static final ProgramareSQLRepository programareRepository = new ProgramareSQLRepository();

    public static void main(String[] args) throws ParseException {

        pacientRepository.openConnection();
        programareRepository.openConnection();

        // Generate and insert Pacient entries
        for (int i = 0; i < NUM_ENTRIES; i++) {
            Pacient pacient = generateRandomPacient();
            pacientRepository.adauga(pacient);

            // Generate and insert Programare entry associated with the Pacient
            Programare programare = generateRandomProgramare(pacient.getId());
            programareRepository.adauga(programare);
        }

        pacientRepository.closeConnection();
        programareRepository.closeConnection();
    }

    private static Pacient generateRandomPacient() {
        int id = generateRandomId();
        String nume = getRandomElement(FIRST_NAMES);
        String prenume = getRandomElement(LAST_NAMES);
        int varsta = random.nextInt(80) + 1; // Assuming age between 1 and 80

        return new Pacient(id, nume, prenume, varsta);
    }

    private static Programare generateRandomProgramare(int idPacient) throws ParseException {
        int id = generateRandomId();
        String data = generateRandomDate();
        String ora = generateRandomTime();
        String scop = getRandomElement(SCOPS);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dataFormatata = dateFormat.parse(data);

        return new Programare(id, pacientRepository.gasesteDupaId(idPacient), dataFormatata, ora, scop);
    }

    private static int generateRandomId() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    private static String getRandomElement(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static String generateRandomDate() {
        int randomYear = random.nextInt(2009,2023) + 1;
        int randomMonth = random.nextInt(0,12) + 1;
        int randomDay = random.nextInt(0,29) + 1;
        return randomYear + "-" + randomMonth + "-" + randomDay;
    }

    private static String generateRandomTime() {
        int randomHour = random.nextInt(0, 24) + 1;
        int randomMinute = random.nextInt(0, 60) + 1;
        if(randomHour < 10 && randomMinute < 10) {
            return "0" + randomHour + ":0" + randomMinute;
        } else if (randomHour >= 10 && randomMinute < 10) {
            return randomHour + ":0" + randomMinute;
        } else if (randomHour >= 10) {
            return randomHour + ":" + randomMinute;
        } else {
            return "0" + randomHour + ":" + randomMinute;
        }
    }
}
