package src.dao;

import src.table.Patient;

import java.util.Date;
import java.util.List;

public interface PatientDao {
    void insert(Patient patient);

    Patient selectById(int id);

    List<Patient> selectAll();

    void delete(int id);

    void update(Patient patient, int id);

}
