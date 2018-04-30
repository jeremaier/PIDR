package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.InclusionDao;
import src.table.Inclusion;
import src.utils.Diag;
import src.utils.FileManager;

import java.sql.*;

public class InclusionDaoImpl extends DaoImpl implements InclusionDao {
    private Connection connection;

    public InclusionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Inclusion inclusion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
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
    public Inclusion selectById(int id) {
        Inclusion inclusion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                inclusion = this.addToInclusion(resultSet);

            System.out.println("SELECT * FROM inclusion WHERE ID ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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

        return inclusion;
    }

    @Override
    public ObservableList<Inclusion> selectByFilters(int id, Date dateInclusion, int numAnaPat, String initiales, Diag diag) {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if(id != 0) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? ORDER BY ID");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                this.addToObservableList(inclusions, resultSet);
            }

            if(dateInclusion != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ? ORDER BY ID");
                preparedStatement.setDate(1, dateInclusion);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if(numAnaPat != 0) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ? ORDER BY ID");
                preparedStatement.setInt(1, numAnaPat);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if (!initiales.equals("")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion JOIN patient ON inclusion.ID_PATIENT = patient.ID WHERE INITIALES = ? ORDER BY inclusion.ID");
                preparedStatement.setString(1, initiales);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if (diag != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion JOIN lesion ON inclusion.ID = lesion.ID_INCLUSION WHERE DIAGNOSTIC = ? ORDER BY inclusion.ID");
                preparedStatement.setString(1, initiales);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if(resultSet == null)
                return this.selectAll();

            System.out.println("SELECT * FROM inclusion WHERE ID ^ DATE_INCLUSION ^ NUM_ANAPATH ^ INITIALES ^ DIAG ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
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

        return inclusions;
    }

    private void refreshList(ObservableList<Inclusion> inclusions, ResultSet resultSet) {
        ObservableList<Inclusion> inclusions1 = FXCollections.observableArrayList();

        if (!inclusions.isEmpty()) {
            this.addToObservableList(inclusions1, resultSet);
            this.retainAllById(inclusions, inclusions1);
        } else this.addToObservableList(inclusions, resultSet);
    }

    private void retainAllById(ObservableList<Inclusion> inclusions1, ObservableList<Inclusion> inclusions2) {
        for (int i = inclusions1.size() - 1; i >= 0; i--) {
            boolean found = false;

            for (Inclusion patient2 : inclusions2) {
                if (patient2.getId().equals(inclusions1.get(i).getId())) {
                    found = true;
                    inclusions2.remove(patient2);
                    break;
                }
            }

            if (!found)
                inclusions1.remove(i);
        }
    }

    @Override
    public ObservableList<Inclusion> selectAll() {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inclusion ORDER BY ID");
            this.addToObservableList(inclusions, resultSet);

            System.out.println("SELECT * FROM inclusion ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
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

        return inclusions;
    }

    @Override
    public void update(Inclusion inclusion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID_PATIENT = ?, REFERENCE1 = ?, REFERENCE2 = ?, DATE_INCLUSION = ?, NUM_ANAPATH = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion, 0);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
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

    private void addToObservableList(ObservableList<Inclusion> inclusions, ResultSet resultSet) {
        try {
            while(resultSet.next())
                inclusions.add(this.addToInclusion(resultSet));

        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Inclusion addToInclusion(ResultSet resultSet) throws SQLException {
        Inclusion inclusion = new Inclusion();

        inclusion.setId(resultSet.getInt("ID"));
        inclusion.setIdPatient(resultSet.getInt("ID_PATIENT"));
        inclusion.setReference1(resultSet.getString("REFERENCE1"));
        inclusion.setReference2(resultSet.getString("REFERENCE2"));
        inclusion.setDateInclusion(resultSet.getDate("DATE_INCLUSION"));
        inclusion.setNumAnaPath(resultSet.getInt("NUM_ANAPATH"));

        return inclusion;
    }

    public void delete(int id) {
        this.delete(connection, "inclusion", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, Integer.parseInt(((Inclusion) object).getId()));

        preparedStatement.setInt(indexDebut + 1, ((Inclusion) object).getIdPatient());
        preparedStatement.setString(indexDebut + 2, ((Inclusion) object).getReference1());
        preparedStatement.setString(indexDebut + 3, ((Inclusion) object).getReference2());
        preparedStatement.setDate(indexDebut + 4, ((Inclusion) object).getDateInclusion());
        preparedStatement.setInt(indexDebut + 5, ((Inclusion) object).getNumAnaPat());

        return preparedStatement;
    }
}