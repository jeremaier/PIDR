package src.daoImpl;

import src.dao.InclusionDao;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.Diag;

import java.sql.*;
import java.util.ArrayList;

public class InclusionDaoImpl extends daoImpl implements InclusionDao {
    private Connection connection;

    public InclusionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Inclusion inclusion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion);
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
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
    public Inclusion selectById(int id) {
        Inclusion inclusion = new Inclusion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ?");
            preparedStatement.setInt(1, inclusion.getId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                inclusion = this.addToPatient(new Inclusion(), resultSet);

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

        return inclusion;
    }

    @Override
    public ArrayList<Inclusion> selectByFilters(int id, Date dateInclusion, int numAnaPat, String initials, Diag diag) {
        ArrayList<Inclusion> inclusions = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            if(id != 0) {
                if(dateInclusion != null) {
                    if (numAnaPat != 0) {
                        if (initials != null) {
                            if (diag.toString() != null) {
                                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? AND DATE_INCLUSION = ? AND NUM_ANAPATH = ? AND INITIALS = ? AND DIAG = ?");
                                preparedStatement.setString(5, diag.toString());
                            } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? AND DATE_INCLUSION = ? AND NUM_ANAPATH = ? AND INITIALS = ?");

                            preparedStatement.setString(4, initials);
                        } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? AND DATE_INCLUSION = ? AND NUM_ANAPATH = ?");

                        preparedStatement.setInt(3, numAnaPat);
                    } else preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ? AND DATE_INCLUSION = ?");

                    preparedStatement.setDate(2, dateInclusion);
                } else preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE ID = ?");

                preparedStatement.setInt(1, id);
            } else if(dateInclusion != null) {
                if (numAnaPat != 0) {
                    if (initials != null) {
                        if (diag.toString() != null) {
                            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ? AND NUM_ANAPATH = ? AND INITIALS = ? AND DIAG = ?");
                            preparedStatement.setString(4, diag.toString());
                        } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ? AND NUM_ANAPATH = ? AND INITIALS = ?");

                        preparedStatement.setString(3, initials);
                    } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ? AND NUM_ANAPATH = ?");

                    preparedStatement.setInt(2, numAnaPat);
                } else preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE DATE_INCLUSION = ?");
            } else if (numAnaPat != 0) {
                if (initials != null) {
                    if (diag.toString() != null) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ? AND INITIALS = ? AND DIAG = ?");
                        preparedStatement.setString(3, diag.toString());
                    } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ? AND INITIALS = ?");

                    preparedStatement.setString(2, initials);
                } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ?");

                preparedStatement.setInt(1, numAnaPat);
            } else if (initials != null) {
                if (diag.toString() != null) {
                    preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE INITIALS = ? AND DIAG = ?");
                    preparedStatement.setString(2, diag.toString());
                } else preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE INITIALS = ?");

                preparedStatement.setString(1, initials);
            } else if (diag.toString() != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DIAG = ?");
                preparedStatement.setString(1, diag.toString());
            } else selectAll();

            resultSet = preparedStatement.executeQuery();
            inclusions = this.addToList(resultSet);

            System.out.println("SELECT * FROM patient WHERE ID ^ ID_PATIENT ^ REFERENCE1 ^ DATE_INCLUSION ^ NUM_ANAPATH");
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

        return inclusions;
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

            System.out.println("SELECT * FROM inclusion");
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
            while (resultSet.next())
                inclusions.add(this.addToPatient(new Inclusion(), resultSet));

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

    private Inclusion addToPatient(Inclusion inclusion, ResultSet resultSet) throws SQLException {
        inclusion.setId(resultSet.getInt("ID"));
        inclusion.setIdPatient(resultSet.getInt("ID_PATIENT"));
        inclusion.setReference1(resultSet.getBlob("REFERENCE1"));
        inclusion.setReference2(resultSet.getBlob("REFERENCE2"));
        inclusion.setDateInclusion(resultSet.getDate("DATE_INCLUSION"));
        inclusion.setNumAnaPath(resultSet.getInt("NUM_ANAPATH"));

        return inclusion;
    }

    public void delete(int id) {
        this.delete(connection, "inclusion", id);
    }

    @Override
    public void update(Inclusion inclusion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID = ?, ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion);
            preparedStatement.setInt(7, id);

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

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object) throws SQLException {
        preparedStatement.setInt(1, ((Inclusion) object).getId());
        preparedStatement.setInt(2, ((Inclusion) object).getIdPatient());
        preparedStatement.setDate(3, ((Inclusion) object).getDateInclusion());
        preparedStatement.setBlob(4, ((Inclusion) object).getReference1());
        preparedStatement.setBlob(5, ((Inclusion) object).getReference2());
        preparedStatement.setInt(6, ((Inclusion) object).getNumAnaPat());

        return preparedStatement;
    }
}