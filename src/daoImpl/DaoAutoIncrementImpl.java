package src.daoImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class DaoAutoIncrementImpl extends Dao {
    protected abstract PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object) throws SQLException;
}
