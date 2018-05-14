package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.PatientDao;
import src.table.Patient;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;

public class PatientDaoImpl extends Dao implements PatientDao {
    private static Connection connection;

    public PatientDaoImpl(Connection connection) {
        PatientDaoImpl.connection = connection;
    }

    @Override
    public void insert(Patient patient) {
        PreparedStatement preparedStatement = null;

        if (patient.getAnneeNaissance() < 1900)
            FileManager.openAlert("Annee invalide");
        else try {
            preparedStatement = PatientDaoImpl.connection.prepareStatement("INSERT INTO patient (ID, GENRE, ANNEE_NAISSANCE) " + "VALUES (?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, patient, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO patient (ID, INITIALES, GENRE, ANNEE_NAISSANCE)");
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
    public Patient selectById(int id) {
        Patient patient = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (id <= 0)
            return null;
        else try {
            preparedStatement = PatientDaoImpl.connection.prepareStatement("SELECT * FROM patient WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                patient = this.addToPatient(resultSet);

            System.out.println("SELECT * FROM patient WHERE ID ORDER BY ID");
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

        return patient;
    }

    @Override
    public ObservableList<Patient> selectByFilters(int id, String genre, String date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (id != -100) {
                preparedStatement = PatientDaoImpl.connection.prepareStatement("SELECT * FROM patient WHERE ID = ? ORDER BY ID");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                this.addToObservableList(patients, resultSet);
            }

            if (genre != null) {
                if (!genre.equals("")) {
                    preparedStatement = PatientDaoImpl.connection.prepareStatement("SELECT * FROM patient WHERE GENRE = ? ORDER BY ID");
                    preparedStatement.setString(1, genre);
                    resultSet = preparedStatement.executeQuery();
                    this.refreshList(patients, resultSet);
                }
            }

            if (!date.equals("")) {
                preparedStatement = PatientDaoImpl.connection.prepareStatement("SELECT * FROM patient WHERE ANNEE_NAISSANCE = ? ORDER BY ID");
                preparedStatement.setString(1, date);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(patients, resultSet);
            }

            if(resultSet == null)
                return this.selectAll();

            System.out.println("SELECT * FROM patient WHERE ID ^ INITIALES ^ ANNEE_NAISSANCE ORDER BY ID");
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

        return patients;
    }

    private void refreshList(ObservableList<Patient> patients, ResultSet resultSet) {
        ObservableList<Patient> patients1 = FXCollections.observableArrayList();

        if (!patients.isEmpty()) {
            this.addToObservableList(patients1, resultSet);
            this.retainAllById(patients, patients1);
        } else this.addToObservableList(patients, resultSet);
    }

    private void retainAllById(ObservableList<Patient> patients1, ObservableList<Patient> patients2) {
        for (int i = patients1.size() - 1; i >= 0; i--) {
            boolean found = false;

            for (Patient patient2 : patients2) {
                if (patient2.getId() == patients1.get(i).getId()) {
                    found = true;
                    patients2.remove(patient2);
                    break;
                }
            }

            if (!found)
                patients1.remove(i);
        }
    }

    @Override
    public ObservableList<Patient> selectAll() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = PatientDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM patient ORDER BY ID");
            this.addToObservableList(patients, resultSet);

            System.out.println("SELECT * FROM patient ORDER BY ID");
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

        return patients;
    }

    @Override
    public void update(Patient patient, int id) {
        PreparedStatement preparedStatement = null;

        if (patient.getAnneeNaissance() <= 1900)
            FileManager.openAlert("Annee invalide");
        else try {
            preparedStatement = PatientDaoImpl.connection.prepareStatement("UPDATE patient SET " + "GENRE = ?, ANNEE_NAISSANCE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, patient, 0);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE patient SET INITIALES = ?, GENRE = ?, ANNEE_NAISSANCE = ? WHERE ID = ?");
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

    private void addToObservableList(ObservableList<Patient> patients, ResultSet resultSet) {
        try {
            while(resultSet.next())
                patients.add(this.addToPatient(resultSet));
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Patient addToPatient(ResultSet resultSet) throws SQLException {
        Patient patient = new Patient();

        patient.setId(resultSet.getInt("ID"));
        patient.setGenre(resultSet.getString("GENRE"));
        patient.setDateNaissance(resultSet.getInt("ANNEE_NAISSANCE"));

        return patient;
    }

    public void delete(int id) {
        PatientDaoImpl.delete(SQLConnection.getConnection(), "patient", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((Patient) object).getId());

        preparedStatement.setString(indexDebut + 1, ((Patient) object).getGenre());
        preparedStatement.setInt(indexDebut + 2, ((Patient) object).getAnneeNaissance());

        return preparedStatement;
    }
}
