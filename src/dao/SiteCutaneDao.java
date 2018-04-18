package src.dao;

import src.table.CutaneousSite;


import javax.swing.text.html.ListView;
import java.util.List;

public interface SiteCutaneDao {
    void insert(CutaneousSite site);

    CutaneousSite selectById(int id);

    List<CutaneousSite> selectAll();

    List<CutaneousSite> selectSain();

    List<CutaneousSite> selectNonSain();

    void update(CutaneousSite site, int id);
}
