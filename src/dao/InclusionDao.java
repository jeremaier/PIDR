package src.dao;

import src.table.Inclusion;
import src.utils.Diag;

import java.sql.Date;
import java.util.List;

public interface InclusionDao {
    void insert(Inclusion inclusion);

    Inclusion selectById(int id);

    List<Inclusion> selectByFilters(int id, Date dateInclusion, int numAnaPat, String initials, Diag diag);

    List<Inclusion> selectAll();

    void delete(int id);

    void update(Inclusion inclusion, int id);
}
