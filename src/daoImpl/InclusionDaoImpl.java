package src.daoImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.InclusionDao;
import src.table.Inclusion;
import src.utils.Diag;

import java.sql.*;

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
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion, 1);
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
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
    public Inclusion selectById(int id) {
        Inclusion inclusion = new Inclusion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ?");
            preparedStatement.setInt(1, inclusion.getId());
            resultSet = preparedStatement.executeQuery();
            inclusion = this.addToInclusion(inclusion, resultSet);
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

        return inclusion;
    }

    @Override
    public ObservableList<Inclusion> selectByFilters(int id, Date dateInclusion, int numAnaPat, String initiales, Diag diag) {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        ObservableList<Inclusion> inclusions2;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if(id != 0) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                inclusions = this.addToObservableList(inclusions, resultSet);
            }

            if(dateInclusion != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ?");
                preparedStatement.setDate(1, dateInclusion);
                resultSet = preparedStatement.executeQuery();

                if(inclusions != null) {
                    inclusions2 = this.addToObservableList(inclusions, resultSet);
                    inclusions2.retainAll(this.addToObservableList(inclusions, resultSet));
                    inclusions = inclusions2;
                } else inclusions = this.addToObservableList(inclusions, resultSet);
            }

            if(numAnaPat != 0) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ?");
                preparedStatement.setInt(1, numAnaPat);
                resultSet = preparedStatement.executeQuery();

                if(inclusions != null) {
                    inclusions2 = this.addToObservableList(inclusions, resultSet);
                    inclusions2.retainAll(this.addToObservableList(inclusions, resultSet));
                    inclusions = inclusions2;
                } else inclusions = this.addToObservableList(inclusions, resultSet);
            }

            if(initiales != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion JOIN patient ON inclusion.ID_PATIENT = patient.ID WHERE INITIALES = ?");
                preparedStatement.setString(1, initiales);
                resultSet = preparedStatement.executeQuery();

                if(inclusions != null) {
                    inclusions2 = this.addToObservableList(inclusions, resultSet);
                    inclusions2.retainAll(this.addToObservableList(inclusions, resultSet));
                    inclusions = inclusions2;
                } else inclusions = this.addToObservableList(inclusions, resultSet);
            }

            if(diag.toString() != null) {
                preparedStatement = connection.prepareStatement("SELECT * FROM inclusion JOIN lesion ON inclusion.ID = lesion.ID_INCLUSION WHERE DIAGNOSTIC = ?");
                preparedStatement.setString(1, initiales);
                resultSet = preparedStatement.executeQuery();

                if(inclusions != null) {
                    inclusions2 = this.addToObservableList(inclusions, resultSet);
                    inclusions2.retainAll(this.addToObservableList(inclusions, resultSet));
                    inclusions = inclusions2;
                } else inclusions = this.addToObservableList(inclusions, resultSet);
            }

            if(resultSet == null)
                return this.selectAll();

            System.out.println("SELECT * FROM inclusion WHERE ID ^ DATE_INCLUSION ^ NUM_ANAPATH ^ INITIALES ^ DIAG");
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

        return inclusions;
    }

    @Override
    public ObservableList<Inclusion> selectAll() {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inclusion");
            inclusions = this.addToObservableList(inclusions, resultSet);

            System.out.println("SELECT * FROM inclusion");
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

        return inclusions;
    }

    @Override
    public void update(int patientId, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID_PATIENT = ? WHERE ID = ?");
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID_PATIENT = ? WHERE ID = ?");
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
    public void update(Inclusion inclusion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion, 0);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
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

    private ObservableList<Inclusion> addToObservableList(ObservableList<Inclusion> inclusions, ResultSet resultSet) {
        try {
            while(resultSet.next())
                inclusions.add(this.addToInclusion(new Inclusion(), resultSet));

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

        return inclusions;
    }

    private Inclusion addToInclusion(Inclusion inclusion, ResultSet resultSet) throws SQLException {
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

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((Inclusion) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((Inclusion) object).getIdPatient());
        preparedStatement.setDate(indexDebut + 2, ((Inclusion) object).getDateInclusion());
        preparedStatement.setBlob(indexDebut + 3, ((Inclusion) object).getReference1());
        preparedStatement.setBlob(indexDebut + 4, ((Inclusion) object).getReference2());
        preparedStatement.setInt(indexDebut + 5, ((Inclusion) object).getNumAnaPat());

        return preparedStatement;
    }
}