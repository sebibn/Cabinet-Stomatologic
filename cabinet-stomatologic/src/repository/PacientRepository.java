package repository;

import domeniu.Pacient;

import java.util.List;

public abstract class PacientRepository extends GenericRepository<Pacient> {
    public abstract List<Pacient> listaEntitati();
//
}