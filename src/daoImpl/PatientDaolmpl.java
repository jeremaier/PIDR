package src.daoImpl;

import src.dao.PatientDao;
import src.table.Patient;
import src.utils.Gender;

import java.sql.*;
import java.util.ArrayList;

public class PatientDaolmpl implements PatientDao {
    private Connection connection;

    public PatientDaolmpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Patient patient) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO patient (INITIALS, GENDER, BIRTHDATE) " + "VALUES (?, ?, ?)");
            preparedStatement.setString(1, patient.getInitials());
            preparedStatement.setString(2, patient.getGender());
            preparedStatement.setDate(3, patient.getBirthDate());
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO patient (initials, gender, birthDate)");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patient.setId(resultSet.getInt("ID"));
                patient.setInitials(resultSet.getString("INITIALS"));
                patient.setGender(resultSet.getString("GENDER"));
                patient.setBirthDate(resultSet.getDate("BIRTHDATE"));
            }

            System.out.println("SELECT * FROM patient WHERE ID = ?");
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
    public ArrayList<Patient> selectByFilters(int id, Gender gender, Date birthDate) {
        ArrayList<Patient> patients = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            if(id != 0) {
                if(gender != null) {
                    if (birthDate != null) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ? AND GENDER = ? AND BIRTHDATE = ?");
                        preparedStatement.setDate(3, birthDate);
                    } else preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ? AND GENDER = ?");

                    preparedStatement.setString(2, gender.toString());
                } else preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ?");

                preparedStatement.setInt(1, id);
            } else if(gender != null) {
                if (birthDate != null) {
                    preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE GENDER = ? AND BIRTHDATE = ?");
                    preparedStatement.setDate(2, birthDate);
                } else  preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE GENDER = ?");

                preparedStatement.setString(1, gender.toString());
            } else if (birthDate != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE BIRTHDATE = ?");
                preparedStatement.setDate(1, birthDate);
            } else selectAll();

            resultSet = preparedStatement.executeQuery();

            patients = this.addToList(resultSet);

            System.out.println("SELECT * FROM patient WHERE ID ^ GENDER ^ BIRTHDATE");
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

        return patients;
    }

    @Override
    public ArrayList<Patient> selectAll() {
        ArrayList<Patient> patients = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM patient");

            patients = this.addToList(resultSet);

            System.out.println("SELECT * FROM patient");
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return patients;
    }

    private ArrayList<Patient> addToList(ResultSet resultSet) {
        ArrayList<Patient> patients = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("ID"));
                patient.setInitials(resultSet.getString("INITIALS"));
                patient.setGender(resultSet.getString("GENDER"));
                patient.setBirthDate(resultSet.getDate("BIRTHDATE"));
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
            preparedStatement = connection.prepareStatement("UPDATE patient SET " + "initials = ?, gender = ?, birthDate = ? WHERE id = ?");
            preparedStatement.setString(1, patient.getInitials());
            preparedStatement.setString(2, patient.getGender());
            preparedStatement.setDate(3, patient.getBirthDate());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            System.out.println("UPDATE patient SET " + "initials = ?, gender = ?, birthDate = ? WHERE id = ?");
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
}
