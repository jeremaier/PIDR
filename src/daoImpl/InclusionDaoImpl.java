package src.daoImpl;

import src.dao.InclusionDao;
import src.table.Inclusion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InclusionDaoImpl implements InclusionDao {
    private Connection connection;

    public InclusionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Inclusion inclusion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO inclusion (id,idPatient,teflon,dateInclusion,numAnaPat)" + "VALUES (?,?, ?, ?, ?)");
            preparedStatement.setInt(0, inclusion.getId());
            preparedStatement.setInt(1, inclusion.getIdPatient());
            preparedStatement.setSQLXML(2, inclusion.getTeflon()); ////////////
            preparedStatement.setDate(3, inclusion.getDateInclusion());
            preparedStatement.setInt(4, inclusion.getNumAnaPat());
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO inclusion (id,idPatient,teflon,dateInclusion,numAnaPat)" + "VALUES (?,?, ?, ?, ?)");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM inclusion WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                inclusion.setId(resultSet.getInt("id"));
                inclusion.setFirstName(resultSet.getString("first_name"));
                inclusion.setLastName(resultSet.getString("last_name"));
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

        return inclusion;
    }

    @Override
    public List<Inclusion> selectAll() {
        List<Inclusion> inclusions = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inclusion");

            while (resultSet.next()) {
                Inclusion inclusion = new Inclusion();
                inclusion.setId(resultSet.getInt("id"));
                inclusion.setFirstName(resultSet.getString("first_name"));
                inclusion.setLastName(resultSet.getString("last_name"));
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

    @Override
    public void delete(int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM inclusion WHERE id = ?");
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
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "first_name = ?, last_name = ? WHERE id = ?");
            preparedStatement.setString(1, inclusion.getFirstName());
            preparedStatement.setString(2, inclusion.getLastName());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET " + "first_name = ?, last_name = ? WHERE id = ?");
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