package repository;

import domeniu.Pacient;
import domeniu.Programare;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JDBC {
    private static final String JDBC_URL =
            "C:\\Users\\sebib\\Desktop\\Programare\\Javra\\a2-sebibn\\cabinet-stomatologic\\src\\cabinet_stomatologic.db";

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

    public void addPacient(Pacient p) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO pacienti VALUES (?, ?, ?, ?)")) {
                statement.setInt(1, p.getId());
                statement.setString(2, p.getNume());
                statement.setString(3, p.getPrenume());
                statement.setInt(4, p.getVarsta());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProgramare(Programare p) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO programari VALUES (?, ?, ?, ?, ?)")) {
                statement.setInt(1, p.getId());
                statement.setInt(2, p.getPacient().getId());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatata = dateFormat.format(p.getData());
                statement.setString(3, dataFormatata);
                statement.setString(4, p.getOra());
                statement.setString(5, p.getScopulProgramarii());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updatePacient(int id, String nume, String prenume, int varsta) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateNume = conn.prepareStatement("UPDATE pacienti SET nume = ? WHERE id = ?");
                 PreparedStatement updatePrenume = conn.prepareStatement("UPDATE pacienti SET prenume = ? WHERE id = ?");
                 PreparedStatement updateVarsta = conn.prepareStatement("UPDATE pacienti SET varsta = ? WHERE id = ?")) {
                updateNume.setString(1, nume);
                updateNume.setInt(2, id);
                updatePrenume.setString(1, prenume);
                updatePrenume.setInt(2,id);
                updateVarsta.setInt(1,varsta);
                updateVarsta.setInt(2,id);

                updateNume.executeUpdate();
                updatePrenume.executeUpdate();
                updateVarsta.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void updateProgramare(int id, int idPacient, String data, String ora, String scop) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateidPacient = conn.prepareStatement("UPDATE programari SET idPacient = ? WHERE id = ?");
                 PreparedStatement updateData = conn.prepareStatement("UPDATE programari SET data = ? WHERE id = ?");
                 PreparedStatement updateOra = conn.prepareStatement("UPDATE programari SET ora = ? WHERE id = ?");
                 PreparedStatement updateScop = conn.prepareStatement("UPDATE programari SET scop = ? WHERE id = ?");) {

                updateidPacient.setInt(1, idPacient);
                updateidPacient.setInt(2, id);

                updateData.setString(1, data);
                updateData.setInt(2, id);

                updateOra.setString(1, ora);
                updateOra.setInt(2, id);

                updateScop.setString(1, scop);
                updateScop.setInt(2, id);

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

    public void removePacientById(int id) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM pacienti WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM programari WHERE idPacient=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProgramareById(int id) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM programari WHERE id=?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public Date convertStringToDate(String dataString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = (Date) dateFormat.parse(dataString);

        return data;
    }

    public ArrayList<Programare> getAllProgramari() {
        ArrayList<Programare> programari = new ArrayList<>();
        ArrayList<Pacient> pacienti = getAllPacienti();

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
}