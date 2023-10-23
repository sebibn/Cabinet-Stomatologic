package domeniu;

import java.util.Date;

public class Programare implements Identifiable {
    private int id;
    private Pacient pacient;
    private Date data;
    private String ora;
    private String scopulProgramarii;

    public Programare(int id, Pacient pacient, Date data, String ora, String scopulProgramarii) {
        this.id = id;
        this.pacient = pacient;
        this.data = data;
        this.ora = ora;
        this.scopulProgramarii = scopulProgramarii;
    }

    @Override
    public int getId() {
        return id;
    }

    public Pacient getPacient() {
        return pacient;
    }

    public Date getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getScopulProgramarii() {
        return scopulProgramarii;
    }
}

