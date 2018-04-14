package src.daoImpl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LameHistologiqueDao;
import src.table.HistologicLamella;

import java.sql.*;

import java.util.List;

public class LameHistologiqueDaompl implements LameHistologiqueDao {
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    public HistologicLamella selectById(int id) {
        HistologicLamella lame = new HistologicLamella();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM lame_histologique WHERE ID = ?");
            preparedStatement.setInt(1, lame.getId());
            resultSet = preparedStatement.executeQuery();
            lame = this.addToLame(lame, resultSet);
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lame;
    }

    @Override
    public List<HistologicLamella> selectAll() {
        ObservableList<HistologicLamella> lame = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lame");
            lame = this.addToObservableList(lame, resultSet);

            System.out.println("SELECT * FROM lame");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
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
            preparedStatement = connection.prepareStatement("UPDATE lame SET " + "ID_LESION = ?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lame, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE lame SET ID_LESION = ?, SITE_COUPE = ?, ORIENTATION_NOIR = ?, ORIENTATION_VERT = ?, COLORATION = ?, PHOTO = ? WHERE ID = ?");
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ObservableList<HistologicLamella> addToObservableList(ObservableList<HistologicLamella> lame, ResultSet resultSet) {
        try {
            while (resultSet.next())
                lame.add(this.addToLame(new HistologicLamella(), resultSet));

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
        }

        return lame;
    }

    private HistologicLamella addToLame(HistologicLamella lame, ResultSet resultSet) throws SQLException {

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

        preparedStatement.setInt(indexDebut + 1, ((HistologicLamella) object).getId());
        preparedStatement.setInt(indexDebut + 2, ((HistologicLamella) object).getIdLesion());
        preparedStatement.setString(indexDebut + 3, ((HistologicLamella) object).getSiteCoupe());
        preparedStatement.setInt(indexDebut + 4, ((HistologicLamella) object).getOrientationNoir());
        preparedStatement.setInt(indexDebut + 5, ((HistologicLamella) object).getOrientationVert());
        preparedStatement.setString(indexDebut + 6, ((HistologicLamella) object).getColoration());
        preparedStatement.setString(indexDebut + 7, ((HistologicLamella) object).getPhoto());

        return preparedStatement;
    }
}
