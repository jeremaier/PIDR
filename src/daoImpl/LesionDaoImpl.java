package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LesionDao;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;

public class LesionDaoImpl extends Dao implements LesionDao {
    private static Connection connection;

    public LesionDaoImpl(Connection connection) {
        LesionDaoImpl.connection = connection;
    }

    public static void delete(int id) {
        LesionDaoImpl.delete(SQLConnection.getConnection(), "lesion", id);
    }

    public static ArrayList<Lesion> removeLesions(String id) {
        ArrayList<Lesion> lesions = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SQLConnection.getConnection().prepareStatement("SELECT ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, DIAGNOSTIC, FILE_DIAG FROM lesion WHERE ID_INCLUSION = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lesion lesion = new Lesion();

                lesion.setId(resultSet.getInt("ID"));
                lesion.setIdInclusion(resultSet.getInt("ID_INCLUSION"));
                lesion.setPhotoSur(resultSet.getString("PHOTO_SUR"));
                lesion.setPhotoHors(resultSet.getString("PHOTO_HORS"));
                lesion.setPhotoFixe(resultSet.getString("PHOTO_FIXE"));
                lesion.setDiag(resultSet.getString("DIAGNOSTIC"));
                lesion.setFileDiag(resultSet.getString("FILE_DIAG"));

                lesions.add(lesion);
            }

            System.out.println("SELECT ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, DIAGNOSTIC, FILE_DIAG FROM lesion WHERE ID_INCLUSION = ?");
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

        return lesions;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?))");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Lesion selectById(int id) {
        Lesion lesion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("SELECT * FROM lesion WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                lesion = this.addToLesion(resultSet);

            System.out.println("SELECT * FROM lesion WHERE ID ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return lesion;
    }

    public ObservableList<Lesion> selectAllByInclusion(int idInclusion) {
        ObservableList<Lesion> lesions = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("SELECT * FROM lesion WHERE ID_INCLUSION = ? ORDER BY ID");
            preparedStatement.setInt(1, idInclusion);
            resultSet = preparedStatement.executeQuery();
            this.addToObservableList(lesions, resultSet);

            System.out.println("SELECT * FROM lesion ORDER BY ID");
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

        return lesions;
    }

    private void addToObservableList(ObservableList<Lesion> lesions, ResultSet resultSet) {
        try {
            while (resultSet.next())
                lesions.add(this.addToLesion(resultSet));
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Lesion addToLesion(ResultSet resultSet) throws SQLException {
        Lesion lesion = new Lesion();

        lesion.setId(resultSet.getInt("ID"));
        lesion.setIdInclusion(resultSet.getInt("ID_INCLUSION"));
        lesion.setPhotoSur(resultSet.getString("PHOTO_SUR"));
        lesion.setPhotoHors(resultSet.getString("PHOTO_HORS"));
        lesion.setPhotoFixe(resultSet.getString("PHOTO_FIXE"));
        lesion.setSiteAnatomique(resultSet.getString("SITE_ANATOMIQUE"));
        lesion.setDiag(resultSet.getString("DIAGNOSTIC"));
        lesion.setAutreDiag(resultSet.getString("AUTRE_DIAG"));
        lesion.setFileDiag(resultSet.getString("FILE_DIAG"));

        return lesion;
    }

    @Override
    public ObservableList<Lesion> selectAll() {
        ObservableList<Lesion> lesions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = LesionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lesion ORDER BY ID");
            this.addToObservableList(lesions, resultSet);

            System.out.println("SELECT * FROM lesion ORDER BY ID");
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

        return lesions;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((Lesion) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((Lesion) object).getIdInclusion());
        preparedStatement.setString(indexDebut + 2, ((Lesion) object).getPhotoSur());
        preparedStatement.setString(indexDebut + 3, ((Lesion) object).getPhotoHors());
        preparedStatement.setString(indexDebut + 4, ((Lesion) object).getPhotoFixe());
        preparedStatement.setString(indexDebut + 5, ((Lesion) object).getSiteAnatomique());

        if (((Lesion) object).getDiag() == null)
            preparedStatement.setString(indexDebut + 6, null);
        else preparedStatement.setString(indexDebut + 6, ((Lesion) object).getDiag().toString());

        preparedStatement.setString(indexDebut + 7, ((Lesion) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 8, ((Lesion) object).getFileDiag());

        return preparedStatement;
    }

    @Override
    public void update(Lesion lesion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = LesionDaoImpl.connection.prepareStatement("UPDATE lesion SET " + "ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion, 0);
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE lesion SET ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ? WHERE ID = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ArrayList<Integer> idList() {
        ArrayList<Integer> sites = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = LesionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT ID FROM lesion ORDER BY ID");

            while (resultSet.next())
                sites.add(resultSet.getInt("ID"));

            System.out.println("SELECT ID FROM lesion ORDER BY ID");
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

        return sites;
    }
}