package src.daoImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.SiteCutaneDao;
import src.table.CutaneousSite;

import java.sql.*;
import java.util.List;

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
        } catch(Exception e) {
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
    public List<CutaneousSite> selectAll() {
        ObservableList<CutaneousSite> site = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM site_cutane");
            site = this.addToObservableList(site, resultSet);

            System.out.println("SELECT * FROM site_cutane");
        } catch(Exception e) {
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
    public void update(CutaneousSite site, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE site_cutane SET " + "ID_LESION = ?, SAIN = ?, NUM_MESURE = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIC = ?, AUTRE_DIAG =?, SPECTROSCOPIE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, site, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE site_cutane SET" + "ID_LESION = ?, SAIN = ?, NUM_MESURE = ?, SITE = ?, ORIENTATION = ?, DIAGNOSTIQUE = ?, AUTRE_DIAG =?, SPECTROSCOPIE = ? WHERE ID = ?");
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

    private CutaneousSite addToSite(CutaneousSite site, ResultSet resultSet) throws SQLException {

        site.setId(resultSet.getInt("ID"));
        site.setIdLesion(resultSet.getInt("ID_LESION"));
        site.setHealthy(resultSet.getBoolean("SAIN"));
        site.setMeasurementNumber(resultSet.getString("NUM_MESURE"));
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
        preparedStatement.setBoolean(indexDebut + 3, ((CutaneousSite) object).getHealthy());
        preparedStatement.setString(indexDebut + 4, ((CutaneousSite) object).getMeasurementNumber());
        preparedStatement.setString(indexDebut + 5, ((CutaneousSite) object).getSite());
        preparedStatement.setInt(indexDebut + 6, ((CutaneousSite) object).getOrientation());
        preparedStatement.setString(indexDebut + 7, ((CutaneousSite) object).getDiag().toString());
        preparedStatement.setString(indexDebut + 8, ((CutaneousSite) object).getAutreDiag());
        preparedStatement.setString(indexDebut + 9, ((CutaneousSite) object).getSpectre());

        return preparedStatement;
    }

}
