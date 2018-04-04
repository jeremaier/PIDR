package src.daoImpl;

import src.dao.PatientDao;
import src.table.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDaolmpl implements PatientDao {
    private Connection connection;

    public PatientDaolmpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void creationPatientTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Patient(id int PRIMARY KEY UNIQUE auto_increment, " +
                    "nom VARCHAR(2)" +
                    "genre VARCHAR(1)" +
                    "dateNaissance YEAR)")
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void insert(Patient patient) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO patient (nom,genre,dateBirth) " + "VALUES (?,?,?)");
            preparedStatement.setString(1, patient.getNom());
            preparedStatement.setString(2, patient.getGenre());
            preparedStatement.setDate(3, patient.getDateNaissance());
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO patient (nom,genre,dateBirth) " + "VALUES (?,?,?)");
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
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
            preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patient.setId(resultSet.getInt("id"));
                patient.setNom(resultSet.getString("nom"));
                patient.setGenre(resultSet.getString("genre"));
                patient.setDateNaissance(resultSet.getDate("dateNaissance"));
            }
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patient;
    }

    @Override
    public List<Patient> selectAll() {
        List<Patient> patients = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM patient");

            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setNom(resultSet.getString("nom"));
                patient.setGenre(resultSet.getString("genre"));
                patient.setDateNaissance(resultSet.getDate("dateNaissance"));
                patients.add(patient);
            }

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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patients;
    }


    @Override
    public void delete(int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM patient WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("DELETE FROM patient WHERE id = ?");
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void update(Patient patient, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE patient SET " + "nom = ?, genre = ?, dateNaissance = ? WHERE id = ?");
            preparedStatement.setString(1, patient.getNom());
            preparedStatement.setString(2, patient.getGenre());
            preparedStatement.setDate(3, patient.getDateNaissance());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            System.out.println("UPDATE patient SET " + "nom = ?, genre = ?, dateNaissance WHERE id = ?");
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public List<Patient> selectByName(String nom) {
        ;
    }

    @Override
    public List<Patient> selectByGenre(String genre) {
        return null;
    }

    @Override
    public List<Patient> selectByDate(Date dateNaissance) {
        return null;
    }
}
