package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.SiteCutaneDao;
import src.table.CutaneousSite;
import src.utils.FileManager;

import java.sql.*;

public class SiteCutaneDaompl extends daoImpl implements SiteCutaneDao {
    private Connection connection;

    public SiteCutaneDaompl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(CutaneousSite site) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO site_cutane ( ID_LESION, SAIN, SITE, ORIENTATION, DIAGNOSTIC, AUTRE_DIAG, FICHIER_DIAG, SPECTROSCOPIE)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO site_cutane ( ID_LESION, SAIN, SITE, ORIENTATION, DIAGNOSTIQUE, AUTRE_DIAG, FICHIER_DIAG, SPECTROSCOPIE)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM site_cutane WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            site = this.addToSite(resultSet);

            System.out.println("SELECT * FROM site_cutane WHERE ID ORDER BY ID");
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

        return site;
    }

    @Override
    public ObservableList<CutaneousSite> selectAll() {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM site_cutane ORDER BY ID");
            this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane ORDER BY ID");
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
    public ObservableList<CutaneousSite> selectBySain(int id, int sain) {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM site_cutane WHERE SAIN = ? AND ID_LESION = ? ORDER BY ID");
            preparedStatement.setInt(1, sain);
            preparedStatement.setInt(2, id);
            resultSet = preparedStatement.executeQuery();
            this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane SAIN^ID_LESION ORDER BY ID");
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

        return site;
    }

    @Override
    public void update(CutaneousSite site, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE site_cutane SET " + "ID_LESION = ?, SAIN = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ?, FICHIER_DIAG =?, SPECTROSCOPIE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 0);
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE site_cutane SET" + "ID_LESION = ?, SAIN = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIQUE = ?, AUTRE_DIAG =?, FICHIER_DIAG=?, SPECTROSCOPIE = ? WHERE ID = ?");
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

    public void delete(int id) {
        this.delete(connection, "site_cutane", id);
    }

    private CutaneousSite addToSite(ResultSet resultSet) throws SQLException {
        CutaneousSite site = new CutaneousSite();

        site.setId(resultSet.getInt("ID"));
        site.setIdLesion(resultSet.getInt("ID_LESION"));
        site.setHealthy(resultSet.getInt("SAIN"));
        site.setSite(resultSet.getString("SITE"));
        site.setOrientation(resultSet.getInt("ORIENTATION"));
        site.setDiag(resultSet.getString("DIAGNOSTIC"));
        site.setAutreDiag(resultSet.getString("AUTRE_DIAG"));
        site.setFichierDiag(resultSet.getString("FICHIER_DIAG"));
        site.setSpectre(resultSet.getString("SPECTROSCOPIE"));

        return site;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((CutaneousSite) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((CutaneousSite) object).getIdLesion());
        preparedStatement.setInt(indexDebut + 2, ((CutaneousSite) object).getHealthy());
        preparedStatement.setString(indexDebut + 3, ((CutaneousSite) object).getSite());
        preparedStatement.setInt(indexDebut + 4, ((CutaneousSite) object).getOrientation());
        preparedStatement.setString(indexDebut + 5, ((CutaneousSite) object).getDiag());
        preparedStatement.setString(indexDebut + 6, ((CutaneousSite) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 7, ((CutaneousSite) object).getFichierDiag());
        preparedStatement.setString(indexDebut + 8, ((CutaneousSite) object).getSpectre());

        return preparedStatement;
    }

}
