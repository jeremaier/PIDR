package src.dao;


import src.table.HistologicLamella;

import java.util.List;

public interface LameHistologiqueDao {
    void insert(HistologicLamella lame);

    HistologicLamella selectById(int id);

    List<HistologicLamella> selectAll();

    void update(HistologicLamella site, int id);
}
