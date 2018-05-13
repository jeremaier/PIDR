package src.dao;

import javafx.collections.ObservableList;
import src.table.Inclusion;
import src.utils.Diag;

import java.sql.Date;
import java.util.List;

public interface InclusionDao {
    void insert(Inclusion inclusion);

    Inclusion selectById(int id);

    List<Inclusion> selectByFilters(int id, Date dateInclusion, String numAnaPat, String initiales, Diag diag);

    ObservableList<Inclusion> selectAll();

    void update(Inclusion inclusion, int id);
}
