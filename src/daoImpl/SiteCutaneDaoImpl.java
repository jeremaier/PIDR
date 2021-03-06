package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.SiteCutaneDao;
import src.table.CutaneousSite;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;

public class SiteCutaneDaoImpl extends Dao implements SiteCutaneDao {
    private static Connection connection;

    public SiteCutaneDaoImpl(Connection connection) {
        SiteCutaneDaoImpl.connection = connection;
    }

    public static ArrayList<CutaneousSite> removeSites(String id) {
        ArrayList<CutaneousSite> siteCutanes = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SQLConnection.getConnection().prepareStatement("SELECT ID, ID_LESION, DIAGNOSTIC, FICHIER_DIAG, IMAGES_SPECTRES, SPECTROSCOPIE FROM site_cutane WHERE ID_LESION = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CutaneousSite cutaneousSite = new CutaneousSite();

                cutaneousSite.setId(resultSet.getInt("ID"));
                cutaneousSite.setIdLesion(resultSet.getInt("ID_LESION"));
                cutaneousSite.setDiag(resultSet.getString("DIAGNOSTIC"));
                cutaneousSite.setFichierDiag(resultSet.getString("FICHIER_DIAG"));
                cutaneousSite.setImagesSpectres(resultSet.getString("IMAGES_SPECTRES"));
                cutaneousSite.setSpectre(resultSet.getString("SPECTROSCOPIE"));

                siteCutanes.add(cutaneousSite);
            }

            System.out.println("SELECT ID, ID_LESION, DIAGNOSTIC, FICHIER_DIAG, SPECTROSCOPIE FROM site_cutane WHERE ID_LESION = ?");
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

        return siteCutanes;
    }

    @Override
    public void insert(CutaneousSite site) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = SiteCutaneDaoImpl.connection.prepareStatement("INSERT INTO site_cutane (ID, ID_LESION, SITE, ORIENTATION, DIAGNOSTIC, AUTRE_DIAG, FICHIER_DIAG, IMAGES_SPECTRES, SPECTROSCOPIE)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 0);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO site_cutane (ID, ID_LESION, SITE, ORIENTATION, DIAGNOSTIC, AUTRE_DIAG, FICHIER_DIAG, FICHIER_MOY, SPECTROSCOPIE)" + "VALUES (?,?, ?, ?, ?, ?, ?, ?)");
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
    public CutaneousSite selectById(int id) {
        CutaneousSite site = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SiteCutaneDaoImpl.connection.prepareStatement("SELECT * FROM site_cutane WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                site = this.addToSite(resultSet);

            System.out.println("SELECT * FROM site_cutane WHERE ID ORDER BY ID");
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

        return site;
    }

    @Override
    public ObservableList<CutaneousSite> selectAll() {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = SiteCutaneDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM site_cutane ORDER BY ID");
            this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane ORDER BY ID");
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

        return site;
    }

    @Override
    public ObservableList<CutaneousSite> selectByLesion(int id) {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = SiteCutaneDaoImpl.connection.prepareStatement("SELECT * FROM site_cutane WHERE ID_LESION = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane ID_LESION ORDER BY ID");
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

        return site;
    }

    private void addToObservableList(ObservableList<CutaneousSite> site, ResultSet resultSet) {
        try {
            while (resultSet.next())
                site.add(this.addToSite(resultSet));
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(CutaneousSite site, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = SiteCutaneDaoImpl.connection.prepareStatement("UPDATE site_cutane SET " + "ID=?, ID_LESION = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FICHIER_DIAG =?, IMAGES_SPECTRES = ?, SPECTROSCOPIE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 0);
            preparedStatement.setInt(10, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE site_cutane SET" + "ID=?, ID_LESION = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIQUE = ?, AUTRE_DIAG =?, FICHIER_DIAG=?, SPECTROSCOPIE = ? WHERE ID = ?");
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
    public ArrayList<Integer> idList() {
        ArrayList<Integer> sites = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = SiteCutaneDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT ID FROM site_cutane ORDER BY ID");

            while (resultSet.next())
                sites.add(resultSet.getInt("ID"));

            System.out.println("SELECT ID FROM site_cutane ORDER BY ID");
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

    private CutaneousSite addToSite(ResultSet resultSet) throws SQLException {
        CutaneousSite site = new CutaneousSite();

        site.setId(resultSet.getInt("ID"));
        site.setIdLesion(resultSet.getInt("ID_LESION"));
        site.setSite(resultSet.getString("SITE"));
        site.setOrientation(resultSet.getInt("ORIENTATION"));
        site.setDiag(resultSet.getString("DIAGNOSTIC"));
        site.setAutreDiag(resultSet.getString("AUTRE_DIAG"));
        site.setFichierDiag(resultSet.getString("FICHIER_DIAG"));
        site.setImagesSpectres(resultSet.getString("IMAGES_SPECTRES"));
        site.setSpectre(resultSet.getString("SPECTROSCOPIE"));

        return site;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((CutaneousSite) object).getId());
    preparedStatement.setInt(indexDebut+1,((CutaneousSite)object).getId() );
        preparedStatement.setInt(indexDebut + 2, ((CutaneousSite) object).getIdLesion());

        if (((CutaneousSite) object).getSite() == null)
            preparedStatement.setString(indexDebut + 3, null);
        else preparedStatement.setString(indexDebut + 3, ((CutaneousSite) object).getSite().toString());

        preparedStatement.setInt(indexDebut + 4, ((CutaneousSite) object).getOrientation());

        if (((CutaneousSite) object).getDiag() == null)
            preparedStatement.setString(indexDebut + 5, null);
        else preparedStatement.setString(indexDebut + 5, ((CutaneousSite) object).getDiag().toString());

        preparedStatement.setString(indexDebut + 6, ((CutaneousSite) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 7, ((CutaneousSite) object).getFichierDiag());
        preparedStatement.setString(indexDebut + 8, ((CutaneousSite) object).getImagesSpectres());
        preparedStatement.setString(indexDebut + 9, ((CutaneousSite) object).getSpectre());

        return preparedStatement;
    }

    public static void delete(int id) {
        SiteCutaneDaoImpl.delete(SQLConnection.getConnection(), "site_cutane", id);
    }
}
