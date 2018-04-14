package src.daoImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.PatientDao;
import src.table.Patient;

import java.sql.*;

public class PatientDaolmpl extends daoImpl implements PatientDao {
    private Connection connection;

    public PatientDaolmpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Patient patient) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO patient (INITIALES, GENRE, ANNEE_NAISSANCE) " + "VALUES (?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, patient, 1);
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO patient (INITIALES, GENRE, DATENAISSANCE)");
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
            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public Patient selectById(int id) {
        Patient patient = new Patient();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
                patient = this.addToPatient(patient, resultSet);

            System.out.println("SELECT * FROM patient WHERE ID = ?");
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

            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patient;
    }

    @Override
    public ObservableList<Patient> selectByFilters(int id, String initiales) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        ObservableList<Patient> patients2;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if(id != 0) {
                preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                patients = this.addToObservableList(patients, resultSet);
            }

            if(initiales != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE INITIALES = ?");
                preparedStatement.setString(1, initiales);
                resultSet = preparedStatement.executeQuery();

                if(patients != null) {
                    patients2 = this.addToObservableList(patients, resultSet);
                    patients2.retainAll(this.addToObservableList(patients, resultSet));
                    patients = patients2;
                } else patients = this.addToObservableList(patients, resultSet);
            }

            if(resultSet == null)
                return this.selectAll();

            System.out.println("SELECT * FROM patient WHERE ID ^ INITIALES");
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

            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patients;
    }

    @Override
    public ObservableList<Patient> selectAll() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM patient");

            patients = this.addToObservableList(patients, resultSet);

            System.out.println("SELECT * FROM patient");
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

            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patients;
    }

    @Override
    public void update(Patient patient, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE patient SET " + "INITIALES = ?, GENRE = ?, ANNEE_NAISSANCE = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, patient, 0);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE patient SET INITIALES = ?, GENRE = ?, ANNEE_NAISSANCE = ? WHERE ID = ?");
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

            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ObservableList<Patient> addToObservableList(ObservableList<Patient> patients, ResultSet resultSet) {
        try {
            while(resultSet.next())
                patients.add(this.addToPatient(new Patient(), resultSet));
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
        }

        return patients;
    }

    private Patient addToPatient(Patient patient, ResultSet resultSet) throws SQLException {
        patient.setId(resultSet.getInt("ID"));
        patient.setInitiales(resultSet.getString("INITIALES"));
        patient.setGenre(resultSet.getString("GENRE"));
        patient.setDateNaissance(resultSet.getInt("DATENAISSANCE"));

        return patient;
    }

    public void delete(int id) {
        this.delete(connection, "patient", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((Patient) object).getId());

        preparedStatement.setString(indexDebut + 1, ((Patient) object).getInitiales());
        preparedStatement.setString(indexDebut + 2, ((Patient) object).getGenre());
        preparedStatement.setInt(indexDebut + 3, ((Patient) object).getAnneeNaissance());

        return preparedStatement;
    }
}
