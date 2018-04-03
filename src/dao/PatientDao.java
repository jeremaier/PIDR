package src.dao;

import src.table.Patient;

import java.util.Date;
import java.util.List;

public interface PatientDao {
    void creationPatientTable();

    void insert (Patient patient);

    Patient selectById(int id);

    List<Patient> selectByName(String nom);

    List<Patient> selectByGenre(String genre);

    List<Patient> selectByDate(Date dateNaissance);

    List<Patient> selectAll();

    void delete( int id);

    void update(Patient patient, int id);

}
