package src.daoImpl;


import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LameHistologiqueDao;
import src.table.HistologicLamella;
import src.utils.FileManager;

import java.sql.*;
import java.util.List;

public class LameHistologiqueDaompl extends DaoImpl implements LameHistologiqueDao {
    private Connection connection;

    public LameHistologiqueDaompl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void insert(HistologicLamella lame) {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO lame_histologique (ID, ID_LESION, SITE_COUPE, ORIENTATION_NOIR, ORIENTATION_VERT, COLORATION, PHOTO)" + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lame, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lame_histoloqique (ID, ID_SITE_CUTANE, SITE_COUPE, ORIENTATION_NOIR, ORIENTATION_VERT, COLORATION, PHOTO)" + "VALUES (?, ?, ?, ?, ?, ?, ?)");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public HistologicLamella selectById(int id) {
        HistologicLamella lame = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM lame_histologique WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                lame = this.addToLame(resultSet);

            System.out.println("SELECT * FROM lame_histologique WHERE ID ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lame;
    }

    @Override
    public ObservableList<HistologicLamella> selectByLesion(int id) {
        ObservableList<HistologicLamella> lame = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lame_histologique WHERE ID_LESION = ? ORDER BY ID");

            while (resultSet.next())
                lame.add(this.addToLame(resultSet));

            System.out.println("SELECT * FROM lame_histologique WHERE ID_LESION ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lame;
    }

    public void delete(int id) {
        this.delete(connection, "lame_histologique", id);
    }

    @Override
    public List<HistologicLamella> selectAll() {
        ObservableList<HistologicLamella> lame = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lame_histologique ORDER BY ID");

            while (resultSet.next())
                lame.add(this.addToLame(resultSet));

            System.out.println("SELECT * FROM lame ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lame;
    }

    @Override
    public void update(HistologicLamella lame, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE lame_histologique SET " + "ID_LESION = ?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lame, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE lame SET ID_LESION = ?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private HistologicLamella addToLame(ResultSet resultSet) throws SQLException {
        HistologicLamella lame = new HistologicLamella();

        lame.setId(resultSet.getInt("ID"));
        lame.setIdLesion(resultSet.getInt("ID_LESION"));
        lame.setSiteCoupe(resultSet.getString("SITE_COUPE"));
        lame.setOrientationNoir(resultSet.getInt("ORIENTATION_NOIR"));
        lame.setOrientationVert(resultSet.getInt("ORIENTATION_VERT"));
        lame.setColoration(resultSet.getString("COLORAION"));
        lame.setPhoto(resultSet.getString("PHOTO"));

        return lame;
    }


    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((HistologicLamella) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((HistologicLamella) object).getIdLesion());
        preparedStatement.setString(indexDebut + 2, ((HistologicLamella) object).getSiteCoupe());
        preparedStatement.setInt(indexDebut + 3, ((HistologicLamella) object).getOrientationNoir());
        preparedStatement.setInt(indexDebut + 4, ((HistologicLamella) object).getOrientationVert());
        preparedStatement.setString(indexDebut + 5, ((HistologicLamella) object).getColoration());
        preparedStatement.setString(indexDebut + 6, ((HistologicLamella) object).getPhoto());

        return preparedStatement;
    }
}
