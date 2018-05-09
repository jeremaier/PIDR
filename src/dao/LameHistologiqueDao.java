package src.dao;


import javafx.collections.ObservableList;
import src.table.HistologicLamella;

import java.util.List;

public interface LameHistologiqueDao {
    void insert(HistologicLamella lame);

    HistologicLamella selectById(int id);

    ObservableList<HistologicLamella> selectByLesion(int id);

    List<HistologicLamella> selectAll();

    void update(HistologicLamella site, int id);

    List<Integer> idList();
}
