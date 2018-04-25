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
            preparedStatement = connection.prepareStatement("INSERT INTO site_cutane (ID, ID_LESION, SAIN, NUM_MESURE, SITE, ORIENTATION, DIAGNOSTIC, AUTRE_DIAG, SPECTROSCOPIE)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO site_cutane (ID, ID_LESION, SAIN, NUM_MESURE, SITE, ORIENTATION, DIAGNOSTIQUE, AUTRE_DIAG, SPECTROSCOPIE)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
        CutaneousSite site = new CutaneousSite();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM site_cutane WHERE ID = ?");
            preparedStatement.setInt(1, site.getId());
            resultSet = preparedStatement.executeQuery();
            site = this.addToSite(site, resultSet);
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
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM site_cutane");
            site = this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return site;
    }

    @Override
    public ObservableList<CutaneousSite> selectSain(int id) {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM site_cutane WHERE SAIN = 1 AND ID_LESION = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            site = this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane SAIN = 1");
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
    public ObservableList<CutaneousSite> selectNonSain(int id) {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM site_cutane WHERE SAIN = 0 AND ID_LESION = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            site = this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane SAIN = 0");
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
            preparedStatement = connection.prepareStatement("UPDATE site_cutane SET " + "ID_LESION = ?, SAIN = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIC = ?, AUTRE_DIAG =?, SPECTROSCOPIE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE site_cutane SET" + "ID_LESION = ?, SAIN = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIQUE = ?, AUTRE_DIAG =?, SPECTROSCOPIE = ? WHERE ID = ?");
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

    private ObservableList<CutaneousSite> addToObservableList(ObservableList<CutaneousSite> site, ResultSet resultSet) {
        try {
            while (resultSet.next())
                site.add(this.addToSite(new CutaneousSite(), resultSet));

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

        return site;
    }

    public void delete(int id) {
        this.delete(connection, "site_cutane", id);
    }

    private CutaneousSite addToSite(CutaneousSite site, ResultSet resultSet) throws SQLException {

        site.setId(resultSet.getInt("ID"));
        site.setIdLesion(resultSet.getInt("ID_LESION"));
        site.setHealthy(resultSet.getInt("SAIN"));
        site.setSite(resultSet.getString("SITE"));
        site.setOrientation(resultSet.getInt("ORIENTATION"));
        site.setDiag(resultSet.getString("DIAGNOSTIC"));
        site.setAutreDiag(resultSet.getString("AUTRE_DIAG"));
        site.setSpectre(resultSet.getString("SPECTROSCOPIE"));
        return site;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((CutaneousSite) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((CutaneousSite) object).getId());
        preparedStatement.setInt(indexDebut + 2, ((CutaneousSite) object).getIdLesion());
        preparedStatement.setInt(indexDebut + 3, ((CutaneousSite) object).getHealthy());
        preparedStatement.setString(indexDebut + 4, ((CutaneousSite) object).getSite());
        preparedStatement.setInt(indexDebut + 5, ((CutaneousSite) object).getOrientation());
        preparedStatement.setString(indexDebut + 6, ((CutaneousSite) object).getDiag().toString());
        preparedStatement.setString(indexDebut + 7, ((CutaneousSite) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 8, ((CutaneousSite) object).getSpectre());

        return preparedStatement;
    }

}
