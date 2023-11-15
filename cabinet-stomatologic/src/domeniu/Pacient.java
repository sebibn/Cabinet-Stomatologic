package domeniu;

import java.io.Serializable;

public class Pacient implements Identifiable, Serializable {
    private int id;
    private String nume;
    private String prenume;
    private int varsta;

    public Pacient(int id, String nume, String prenume, int varsta) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }
}

