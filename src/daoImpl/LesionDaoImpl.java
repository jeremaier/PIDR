package src.daoImpl;

import src.dao.InclusionDao;
import src.dao.LesionDao;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.Diag;

import java.sql.*;
import java.util.ArrayList;

public class LesionDaoImpl implements LesionDao {
    private Connection connection;

    public LesionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO lesion (ID, ID_INCLUSION, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, lesion.getId());
            preparedStatement.setInt(2, lesion.getIdPatient());
            preparedStatement.setBlob(3, lesion.getReference1());
            preparedStatement.setBlob(4, lesion.getReference2());
            preparedStatement.setDate(5, lesion.getDateInclusion());
            preparedStatement.setInt(6, lesion.getNumAnaPat());
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO lesion (id,idPatient,teflon,dateInclusion,numAnaPat)" + "VALUES (?,?, ?, ?, ?)");
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
    public Lesion selectById(int id) {
        Lesion lesion = new Lesion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ?");
            preparedStatement.setInt(1, inclusion.getId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                inclusion.setId(resultSet.getInt("ID"));
                inclusion.setIdPatient(resultSet.getInt("ID_PATIENT"));
                inclusion.setDateInclusion(resultSet.getDate("DATE_INCLUSION"));
                inclusion.setReference1(resultSet.getBlob("REFERENCE1"));
                inclusion.setReference2(resultSet.getBlob("REFERENCE2"));
                inclusion.setNumAnaPath(resultSet.getInt("NUM_ANAPATH"));
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

        return lesion;
    }

    @Override
    public ArrayList<Inclusion> selectAll() {
        ArrayList<Inclusion> inclusions = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inclusion");
            inclusions = this.addToList(resultSet);

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

        return inclusions;
    }

    private ArrayList<Inclusion> addToList(ResultSet resultSet) {
        ArrayList<Inclusion> inclusions = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Inclusion inclusion = new Inclusion();
                inclusion.setId(resultSet.getInt("ID"));
                inclusion.setIdPatient(resultSet.getInt("ID_PATIENT"));
                inclusion.setReference1(resultSet.getBlob("REFERENCE1"));
                inclusion.setReference2(resultSet.getBlob("REFERENCE2"));
                inclusion.setDateInclusion(resultSet.getDate("DATE_INCLUSION"));
                inclusion.setNumAnaPath(resultSet.getInt("NUM_ANAPATH"));
                inclusions.add(inclusion);
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

        return inclusions;
    }

    @Override
    public void delete(int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM inclusion WHERE ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("DELETE FROM inclusion WHERE id = ?");
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
    public void update(Inclusion inclusion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID = ?, ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
            preparedStatement.setInt(1, inclusion.getId());
            preparedStatement.setInt(2, inclusion.getIdPatient());
            preparedStatement.setDate(3, inclusion.getDateInclusion());
            preparedStatement.setBlob(4, inclusion.getReference1());
            preparedStatement.setBlob(5, inclusion.getReference2());
            preparedStatement.setInt(6, inclusion.getNumAnaPat());

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID = ?, ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
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