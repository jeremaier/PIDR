package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.LesionDao;
import src.table.Lesion;
import src.utils.FileManager;

import java.sql.*;

public class LesionDaoImpl extends daoImpl implements LesionDao {
    private Connection connection;

    public LesionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FICHIER_MOY)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG, FICHIER_MOY)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?))");
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
            preparedStatement = connection.prepareStatement("UPDATE lesion SET " + "ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FICHIER_MOY = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE inclusion SET ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FICHIER_MOY = ? WHERE ID = ?");
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
            resultSet = statement.executeQuery("select last_insert_id() as lastId from lesion");
            lastId = resultSet.getInt("lastId");

            System.out.println("select last_insert_id() as lastId from lesion");
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
        lesion.setFichierMoy(resultSet.getString("FICHIER_MOY"));

        return lesion;
    }

    public void delete(int id) {
        this.delete(connection, "lesion", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((Lesion) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((Lesion) object).getIdInclusion());
        preparedStatement.setString(indexDebut + 2, ((Lesion) object).getPhotoSur());
        preparedStatement.setString(indexDebut + 3, ((Lesion) object).getPhotoHors());
        preparedStatement.setString(indexDebut + 4, ((Lesion) object).getPhotoFixe());
        preparedStatement.setString(indexDebut + 5, ((Lesion) object).getSiteAnatomique());
        preparedStatement.setString(indexDebut + 6, ((Lesion) object).getDiag().toString());
        preparedStatement.setString(indexDebut + 7, ((Lesion) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 8, ((Lesion) object).getFichierMoy());

        return preparedStatement;
    }
}