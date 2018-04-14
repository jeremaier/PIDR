package src.dao;

import src.table.CutaneousSite;


import java.util.List;

public interface SiteCutaneDao {
    void insert(CutaneousSite site);

    CutaneousSite selectById(int id);

    List<CutaneousSite> selectAll();

    void update(CutaneousSite site, int id);
}
