package src.daoImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class DaoImpl extends Dao {
    protected abstract PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException;
}
