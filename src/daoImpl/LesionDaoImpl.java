package src.daoImpl;

import src.dao.LesionDao;
import src.table.Lesion;

import java.sql.*;
import java.util.ArrayList;

public class LesionDaoImpl extends daoImpl implements LesionDao {
    private Connection connection;

    public LesionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Lesion lesion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO lesion (ID, ID_INCLUSION, PHOTO_SUR, PHOTO_HORS, PHOTO_FIXE, SITE_ANATOMIQUE, DIAGNOSTIC, AUTRE_DIAG)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?))");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM lesion WHERE ID = ?");
            preparedStatement.setInt(1, lesion.getId());
            resultSet = preparedStatement.executeQuery();
            lesion = this.addToLesion(lesion, resultSet);
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
    public ArrayList<Lesion> selectAll() {
        ArrayList<Lesion> lesions = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lesion");
            lesions = this.addToList(resultSet);

            System.out.println("SELECT * FROM lesion");
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

        return lesions;
    }

    private ArrayList<Lesion> addToList(ResultSet resultSet) {
        ArrayList<Lesion> lesions = new ArrayList<>();

        try {
            while (resultSet.next())
                lesions.add(this.addToLesion(new Lesion(), resultSet));

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

        return lesions;
    }

    private Lesion addToLesion(Lesion lesion, ResultSet resultSet) throws SQLException {
        lesion.setId(resultSet.getInt("ID"));
        lesion.setIdInclusion(resultSet.getInt("ID_INCLUSION"));
        lesion.setPhotoSur(resultSet.getBlob("PHOTO_SUR"));
        lesion.setPhotoHors(resultSet.getBlob("PHOTO_HORS"));
        lesion.setPhotoFixe(resultSet.getBlob("PHOTO_FIXE"));
        lesion.setSiteAnatomique(resultSet.getString("SITE_ANATOMIQUE"));
        lesion.setDiag(resultSet.getString("DIAGNOSTIC"));
        lesion.setAutreDiag(resultSet.getString("AUTRE_DIAG"));

        return lesion;
    }

    public void delete(int id) {
        this.delete(connection, "lesion", id);
    }

    @Override
    public void update(Lesion lesion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE inclusion SET " + "ID = ?, ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, lesion);
            preparedStatement.setInt(9, id);

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID = ?, ID_INCLUSION = ?, PHOTO_SUR = ?, PHOTO_HORS = ?, PHOTO_FIXE = ?, SITE_ANATOMIQUE = ?, DIAGNOSTIC = ?, AUTRE_DIAG = ? WHERE ID = ?");
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
        preparedStatement.setInt(1, ((Lesion) object).getId());
        preparedStatement.setInt(2, ((Lesion) object).getIdInclusion());
        preparedStatement.setBlob(3, ((Lesion) object).getPhotoSur());
        preparedStatement.setBlob(4, ((Lesion) object).getPhotoHors());
        preparedStatement.setBlob(5, ((Lesion) object).getPhotoFixe());
        preparedStatement.setString(6, ((Lesion) object).getSiteAnatomique());
        preparedStatement.setString(7, ((Lesion) object).getDiag().toString());
        preparedStatement.setString(8, ((Lesion) object).getAutreDiag());

        return preparedStatement;
    }
}