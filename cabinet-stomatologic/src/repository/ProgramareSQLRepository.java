package repository;

import domeniu.Pacient;
import domeniu.Programare;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramareSQLRepository implements Repository<Programare> {
    private static final String JDBC_URL =
            "jdbc:sqlite:C:\\Users\\sebib\\Desktop\\Programare\\Javra\\a2-sebibn\\cabinet-stomatologic\\src\\cabinet_stomatologic.db";

    private Connection conn = null;

    /**
     * Gets a connection to the database.
     * If the underlying connection is closed, it creates a new connection. Otherwise, the current instance is returned.
     */
    public void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the underlying connection to the in-memory SQLite instance.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the sample schema for the database.
     */
    public void createSchema() {
        try {
            try (final Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pacienti(id int, nume varchar(50), prenume varchar(50), varsta int);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS programari(id int, idPacient int, datap varchar(50), ora varchar(50), scop varchar(100));");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    public ArrayList<Pacient> getAllPacienti() {
        ArrayList<Pacient> pacienti = new ArrayList<>();

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from pacienti"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Pacient p = new Pacient(rs.getInt("id"), rs.getString("nume"),
                            rs.getString("prenume"), rs.getInt("varsta"));
                    pacienti.add(p);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return pacienti;
    }

    public Pacient getPacientById(int id) {
        ArrayList<Pacient> pacienti = getAllPacienti();

        for(Pacient pacient: pacienti) {
            if(pacient.getId() == id)
                return pacient;
        }
        return pacienti.getFirst();
    }

    public java.util.Date convertStringToDate(String dataString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = dateFormat.parse(dataString);

        return data;
    }

    @Override
    public List<Programare> listaEntitati() {
        ArrayList<Programare> programari = new ArrayList<>();

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from programari"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Programare p = new Programare(rs.getInt("id"), getPacientById(rs.getInt("idPacient")),
                            convertStringToDate(rs.getString("datap")), rs.getString("ora"), rs.getString("scop"));
                    programari.add(p);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return programari;
    }

    @Override
    public void adauga(Programare entity) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO programari VALUES (?, ?, ?, ?, ?)")) {
                statement.setInt(1, entity.getId());
                statement.setInt(2, entity.getPacient().getId());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatata = dateFormat.format(entity.getData());
                statement.setString(3, dataFormatata);
                statement.setString(4, entity.getOra());
                statement.setString(5, entity.getScopulProgramarii());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizeaza(Programare entity) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateidPacient = conn.prepareStatement("UPDATE programari SET idPacient = ? WHERE id = ?");
                 PreparedStatement updateData = conn.prepareStatement("UPDATE programari SET datap = ? WHERE id = ?");
                 PreparedStatement updateOra = conn.prepareStatement("UPDATE programari SET ora = ? WHERE id = ?");
                 PreparedStatement updateScop = conn.prepareStatement("UPDATE programari SET scop = ? WHERE id = ?");) {

                updateidPacient.setInt(1, entity.getPacient().getId());
                updateidPacient.setInt(2, entity.getId());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatata = dateFormat.format(entity.getData());

                updateData.setString(1, dataFormatata);
                updateData.setInt(2, entity.getId());

                updateOra.setString(1, entity.getOra());
                updateOra.setInt(2, entity.getId());

                updateScop.setString(1, entity.getScopulProgramarii());
                updateScop.setInt(2, entity.getId());

                updateidPacient.executeUpdate();
                updateData.executeUpdate();
                updateOra.executeUpdate();
                updateScop.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sterge(Programare entity) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM programari WHERE id=?")) {
                statement.setInt(1, entity.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Programare gasesteDupaId(int id) {
        ArrayList<Programare> programari = (ArrayList<Programare>) listaEntitati();

        for (Programare p : programari){
            if(p.getId() == id)
                return p;
        }
        return null;
    }
}