package src.dao;

import src.table.Lesion;

import java.util.List;

public interface LesionDao {
    void insert(Lesion lesion);

    Lesion selectById(int id);

    List<Lesion> selectAll();

    void update(Lesion lesion, int id);

    List<Integer> idList();
}
