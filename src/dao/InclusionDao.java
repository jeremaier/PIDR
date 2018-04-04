package src.dao;

import src.table.Inclusion;

import java.util.List;

public interface InclusionDao {
    void createInclusionTable();

    void insert(Inclusion inclusion);

    Inclusion selectById(int id);

    List<Inclusion> selectAll();

    void delete(int id);

    void update(Inclusion inclusion, int id);
}
