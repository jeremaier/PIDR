package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LesionDao;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class LesionDaoImpl extends DaoAutoIncrementImpl implements LesionDao {
    private Connection connection;

    public LesionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO lesion (ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG, FICHIER_MOY)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FILE_DIAG, FICHIER_MOY)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?))");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM lesion WHERE ID = ? ORDER BY ID");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM lesion WHERE ID_INCLUSION = ? ORDER BY ID");
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

    @Override
    public ObservableList<Lesion> selectAll() {
        ObservableList<Lesion> lesions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
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

    @Override
    public void update(Lesion lesion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE lesion SET " + "ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ?, FICHIER_MOY = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.setInt(10, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE inclusion SET ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FILE_DIAG = ?, FICHIER_MOY = ? WHERE ID = ?");
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
    public int getLastId() {
        Statement statement = null;
        ResultSet resultSet = null;
        int lastId = 0;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT last_insert_id() AS lastId FROM lesion");

            if (resultSet.next())
                lastId = resultSet.getInt("lastId");
            else return -1;

            System.out.println("SELECT last_insert_id() AS lastId FROM lesion");
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

        return lastId;
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
        lesion.setFichierMoy(resultSet.getString("FICHIER_MOY"));

        return lesion;
    }

    public void delete(int id) {
        this.delete(connection, "lesion", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object) throws SQLException {
        preparedStatement.setInt(1, ((Lesion) object).getIdInclusion());
        preparedStatement.setString(2, ((Lesion) object).getPhotoSur());
        preparedStatement.setString(3, ((Lesion) object).getPhotoHors());
        preparedStatement.setString(4, ((Lesion) object).getPhotoFixe());
        preparedStatement.setString(5, ((Lesion) object).getSiteAnatomique());

        if (((Lesion) object).getDiag() == null)
            preparedStatement.setString(6, null);
        else preparedStatement.setString(6, ((Lesion) object).getDiag().toString());

        preparedStatement.setString(7, ((Lesion) object).getAutreDiag());
        preparedStatement.setString(8, ((Lesion) object).getFileDiag());
        preparedStatement.setString(9, ((Lesion) object).getFichierMoy());

        return preparedStatement;
    }

    public boolean moreThanOneDiag(Diag diag) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            ArrayList<String> diags = new ArrayList<>();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DIAGNOSTIC FROM lesion");

            while (resultSet.next())
                diags.add(resultSet.getString("DIAGNOSTIC"));

            System.out.println("SELECT DIAGNOSTIC FROM lesion");

            if (Collections.frequency(diags, diag) > 1)
                return true;
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

        return false;
    }
}