package src.dao;

import src.table.Patient;

import java.util.List;

public interface PatientDao {
    void insert(Patient patient);

    Patient selectById(int id);

    List<Patient> selectByFilters(int id, String initiales);

    List<Patient> selectAll();

    void update(Patient patient, int id);
}
