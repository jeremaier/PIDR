package src.daoImpl;


import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LameHistologiqueDao;
import src.table.HistologicLamella;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LameHistologiqueDaoImpl extends DaoImpl implements LameHistologiqueDao {
    private static Connection connection;

    public LameHistologiqueDaoImpl(Connection connection) {
        LameHistologiqueDaoImpl.connection = connection;
    }

    public static ArrayList<HistologicLamella> removeLamellas(String id) {
        ArrayList<HistologicLamella> lamellas = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SQLConnection.getConnection().prepareStatement("SELECT ID, ID_LESION, PHOTO FROM lame_histologique WHERE ID_LESION = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HistologicLamella lamella = new HistologicLamella();

                lamella.setId(resultSet.getInt("ID"));
                lamella.setIdLesion(resultSet.getInt("ID_LESION"));
                lamella.setPhoto(resultSet.getString("PHOTO"));

                lamellas.add(lamella);
            }

            System.out.println("SELECT ID, ID_LESION, PHOTO FROM lame_histologique WHERE ID_LESION = ?");
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

        return lamellas;
    }

    @Override
    public HistologicLamella selectById(int id) {
        HistologicLamella lame = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LameHistologiqueDaoImpl.connection.prepareStatement("SELECT * FROM lame_histologique WHERE ID = ? ORDER BY ID");
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
    public void insert(HistologicLamella lame) {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LameHistologiqueDaoImpl.connection.prepareStatement("INSERT INTO lame_histologique (ID, ID_LESION, SITE_COUPE, ORIENTATION_NOIR, ORIENTATION_VERT, COLORATION, PHOTO)" + "VALUES (?, ?, ?, ?, ?, ?, ?)");
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
    public ObservableList<HistologicLamella> selectByLesion(int id) {
        ObservableList<HistologicLamella> lame = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LameHistologiqueDaoImpl.connection.prepareStatement("SELECT * FROM lame_histologique WHERE ID_LESION = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            this.addToObservableList(lame, resultSet);


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

    private void addToObservableList(ObservableList<HistologicLamella> site, ResultSet resultSet) {
        try {
            while (resultSet.next())
                site.add(this.addToLame(resultSet));
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        LameHistologiqueDaoImpl.delete(SQLConnection.getConnection(), "lame_histologique", id);
    }

    @Override
    public ObservableList<HistologicLamella> selectAll() {
        ObservableList<HistologicLamella> lame = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = LameHistologiqueDaoImpl.connection.createStatement();
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

    private HistologicLamella addToLame(ResultSet resultSet) throws SQLException {
        HistologicLamella lame = new HistologicLamella();

        lame.setId(resultSet.getInt("ID"));
        lame.setIdLesion(resultSet.getInt("ID_LESION"));
        lame.setSiteCoupe(resultSet.getString("SITE_COUPE"));
        lame.setOrientationNoir(resultSet.getInt("ORIENTATION_NOIR"));
        lame.setOrientationVert(resultSet.getInt("ORIENTATION_VERT"));
        lame.setColoration(resultSet.getString("COLORATION"));
        lame.setPhoto(resultSet.getString("PHOTO"));

        return lame;
    }


    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((HistologicLamella) object).getId());

        preparedStatement.setInt(indexDebut+1,((HistologicLamella) object).getId());
        preparedStatement.setInt(indexDebut + 2, ((HistologicLamella) object).getIdLesion());
        preparedStatement.setString(indexDebut + 3, ((HistologicLamella) object).getSiteCoupe());
        preparedStatement.setInt(indexDebut + 4, ((HistologicLamella) object).getOrientationNoir());
        preparedStatement.setInt(indexDebut + 5, ((HistologicLamella) object).getOrientationVert());
        preparedStatement.setString(indexDebut + 6, ((HistologicLamella) object).getColoration());
        preparedStatement.setString(indexDebut + 7, ((HistologicLamella) object).getPhoto());

        return preparedStatement;
    }

    @Override
    public void update(HistologicLamella lame, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LameHistologiqueDaoImpl.connection.prepareStatement("UPDATE lame_histologique SET " + "ID = ?, ID_LESION =?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lame, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE lame SET ID=?, ID_LESION=?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
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
}
