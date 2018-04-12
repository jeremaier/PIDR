package src.dao;

import src.table.Patient;
import src.utils.Gender;

import java.sql.Date;
import java.util.List;

public interface PatientDao {
    void insert(Patient patient);

    Patient selectById(int id);

    List<Patient> selectByFilters(int id, Gender genre, Date anneeDeNaissance);

    List<Patient> selectAll();

    void delete(int id);

    void update(Patient patient, int id);
}
