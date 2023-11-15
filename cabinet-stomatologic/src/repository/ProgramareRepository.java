package repository;

import domeniu.Programare;

import java.util.List;

public abstract class ProgramareRepository extends GenericRepository<Programare> {
    public abstract List<Programare> listaEntitati();
//
}