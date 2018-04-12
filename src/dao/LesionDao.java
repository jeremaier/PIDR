package src.dao;

import src.table.Lesion;
import src.utils.Diag;

import java.sql.Date;
import java.util.List;

public interface LesionDao {
    void insert(Lesion lesion);

    Lesion selectById(int id);

    List<Lesion> selectAll();

    void delete(int id);

    void update(Lesion lesion, int id);
}
