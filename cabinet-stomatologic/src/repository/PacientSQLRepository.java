package repository;

import domeniu.Pacient;
import domeniu.Programare;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PacientSQLRepository implements Repository<Pacient> {
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
        try (final Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pacienti(id int, nume varchar(50), prenume varchar(50), varsta int);");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS programari(id int, idPacient int, datap varchar(50), ora varchar(50), scop varchar(100));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Pacient> listaEntitati() {
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

    public void adauga(Pacient p) {
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

    @Override
    public void actualizeaza(Pacient entity) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement updateNume = conn.prepareStatement("UPDATE pacienti SET nume = ? WHERE id = ?");
                 PreparedStatement updatePrenume = conn.prepareStatement("UPDATE pacienti SET prenume = ? WHERE id = ?");
                 PreparedStatement updateVarsta = conn.prepareStatement("UPDATE pacienti SET varsta = ? WHERE id = ?")) {
                updateNume.setString(1, entity.getNume());
                updateNume.setInt(2, entity.getId());
                updatePrenume.setString(1, entity.getPrenume());
                updatePrenume.setInt(2,entity.getId());
                updateVarsta.setInt(1,entity.getVarsta());
                updateVarsta.setInt(2,entity.getId());

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

    @Override
    public void sterge(Pacient entity) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM pacienti WHERE id=?")) {
                statement.setInt(1, entity.getId());
                statement.executeUpdate();
            }
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM programari WHERE idPacient=?")) {
                statement.setInt(1, entity.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pacient gasesteDupaId(int id) {
        ArrayList<Pacient> pacienti = (ArrayList<Pacient>) listaEntitati();

        for(Pacient pacient: pacienti) {
            if(pacient.getId() == id)
                return pacient;
        }
        return pacienti.getFirst();
    }
}
