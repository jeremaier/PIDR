package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.InclusionDao;
import src.table.Inclusion;
import src.utils.Diag;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InclusionDaoImpl extends DaoImpl implements InclusionDao {
    private static Connection connection;

    public InclusionDaoImpl(Connection connection) {
        InclusionDaoImpl.connection = connection;
    }

    /*public static String selectDiag(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String diagQuery = null;

        try {
            preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT DIAG FROM inclusion WHERE ID = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                diagQuery = resultSet.getString("DIAG");

            System.out.println("SELECT DIAG FROM inclusion WHERE ID = ?");

            return diagQuery;
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }

        return null;
    }*/

    public static void delete(int id) {
        InclusionDaoImpl.delete(SQLConnection.getConnection(), "inclusion", id);
    }

    public static java.sql.Date stringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        if(date != null) {
            try {
                java.util.Date parsed = format.parse(date);
                return new java.sql.Date(parsed.getTime());
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
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

            for (Inclusion inclusion : inclusions2) {
                if (inclusion.getId().equals(inclusions1.get(i).getId())) {
                    found = true;
                    inclusions2.remove(inclusion);
                    break;
                }
            }

            if (!found)
                inclusions1.remove(i);
        }
    }

    /*public static void removeDiag(Diag diag, int id) {
        if (diag != null) {
            if (!diag.toString().equals("")) {
                PreparedStatement preparedStatement = null;

                try {
                    String diagQuery = InclusionDaoImpl.selectDiag(id);
                    String newDiag = "";

                    if (diagQuery != null) {
                        if (!diagQuery.equals("")) {
                            String[] diagSplit = diagQuery.split(" - ");

                            for (String diags : diagSplit) {
                                if (!diags.equals(diag.toString()))
                                    newDiag.concat(diags).concat(" - ");
                            }
                        }
                    }

                    if (newDiag.length() > 0)
                        newDiag = newDiag.substring(0, newDiag.length() - 3);

                    preparedStatement = InclusionDaoImpl.connection.prepareStatement("UPDATE inclusion SET " + "DIAG = ? WHERE ID = ?");
                    preparedStatement.setString(1, newDiag);
                    preparedStatement.setInt(2, id);

                    preparedStatement.executeUpdate();
                    System.out.println("UPDATE inclusion SET " + "DIAG = ? WHERE ID = ?");
                } catch (MySQLNonTransientConnectionException e) {
                    FileManager.openAlert("La connection avec le serveur est interrompue");
                    e.printStackTrace();
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
                }
            }
        }
    }*/

    @Override
    public void insert(Inclusion inclusion) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = InclusionDaoImpl.connection.prepareStatement("INSERT INTO inclusion (ID, ID_PATIENT, REFERENCE1, REFERENCE2, DATE_INCLUSION, NUM_ANAPATH)" + "VALUES (?, ?, ?, ?, ?, ?)");
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
            preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                inclusion = this.addToInclusion(resultSet);

            System.out.println("SELECT * FROM inclusion WHERE ID ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }

        return inclusion;
    }

    @Override
    public ObservableList<Inclusion> selectAll() {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = InclusionDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inclusion ORDER BY ID");
            this.addToObservableList(inclusions, resultSet);
            this.setDiags(inclusions);

            System.out.println("SELECT * FROM inclusion ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }

        return inclusions;
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

    @Override
    public void update(Inclusion inclusion, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = InclusionDaoImpl.connection.prepareStatement("UPDATE inclusion SET " + "ID_PATIENT = ?, REFERENCE1 = ?, REFERENCE2 = ?, DATE_INCLUSION = ?, NUM_ANAPATH = ? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, inclusion, 0);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            System.out.println("UPDATE inclusion SET ID_PATIENT = ?, DATE_INCLUSION = ?, REFERENCE1 = ?, REFERENCE2 = ?, NUM_ANAPATH = ? WHERE ID = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }
    }

    private void setDiags(ObservableList<Inclusion> inclusions) {
        ArrayList<String> diags = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            for (Inclusion inclusion : inclusions) {
                preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT site_cutane.DIAGNOSTIC FROM site_cutane JOIN lesion WHERE site_cutane.ID_LESION = lesion.ID AND lesion.ID_INCLUSION = ?");
                preparedStatement.setInt(1, Integer.parseInt(inclusion.getId()));
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String diag;

                    if (!diags.contains(diag = resultSet.getString("DIAGNOSTIC")))
                        diags.add(diag);
                }

                int diagsNumber;

                if ((diagsNumber = diags.size()) > 0) {
                    if (diags.size() > 1) {
                        StringBuilder concatDiags = new StringBuilder(diags.get(0));

                        for (int i = 1; i < diagsNumber; i++)
                            concatDiags.append("\n").append(diags.get(i));

                        inclusion.setDiag(concatDiags.toString());
                    } else inclusion.setDiag(diags.get(0));
                }
            }

            System.out.println("SELECT site_cutane.DIAGNOSTIC FROM site_cutane JOIN lesion WHERE site_cutane.ID_LESION = lesion.ID AND lesion.ID_INCLUSION = ?");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if(indexDebut == 1)
            preparedStatement.setInt(indexDebut, Integer.parseInt(((Inclusion) object).getId()));

        preparedStatement.setInt(indexDebut + 1, ((Inclusion) object).getIdPatient());
        preparedStatement.setString(indexDebut + 2, ((Inclusion) object).getReference1());
        preparedStatement.setString(indexDebut + 3, ((Inclusion) object).getReference2());
        preparedStatement.setDate(indexDebut + 4, InclusionDaoImpl.stringToDate(((Inclusion) object).getDateInclusion()));
        preparedStatement.setInt(indexDebut + 5, ((Inclusion) object).getNumAnaPat());

        return preparedStatement;
    }

    @Override
    public ObservableList<Inclusion> selectByFilters(int id, java.sql.Date dateInclusion, int numAnaPat, String idPatient, Diag diag) {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (id != 0) {
                preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion WHERE ID = ? ORDER BY ID");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                this.addToObservableList(inclusions, resultSet);
            }

            if (dateInclusion != null) {
                preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion WHERE DATE_INCLUSION = ? ORDER BY ID");
                preparedStatement.setDate(1, dateInclusion);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if (numAnaPat != 0) {
                preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion WHERE NUM_ANAPATH = ? ORDER BY ID");
                preparedStatement.setInt(1, numAnaPat);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if (!idPatient.equals("")) {
                preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion JOIN patient WHERE ID_PATIENT = ? ORDER BY inclusion.ID");
                preparedStatement.setString(1, idPatient);
                resultSet = preparedStatement.executeQuery();
                this.refreshList(inclusions, resultSet);
            }

            if (diag != null) {
                if (!diag.equals(Diag.NULL)) {
                    preparedStatement = InclusionDaoImpl.connection.prepareStatement("SELECT * FROM inclusion JOIN lesion ON inclusion.ID = lesion.ID_INCLUSION WHERE DIAGNOSTIC LIKE ? ORDER BY inclusion.ID");
                    preparedStatement.setString(1, "%" + diag.toString() + "%");
                    resultSet = preparedStatement.executeQuery();
                    this.refreshList(inclusions, resultSet);
                }
            }

            if (resultSet == null)
                return this.selectAll();

            System.out.println("SELECT * FROM inclusion WHERE ID ^ DATE_INCLUSION ^ NUM_ANAPATH ^ INITIALES ^ DIAG ORDER BY ID");
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La connection avec le serveur est interrompue");
            e.printStackTrace();
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
        }

        return inclusions;
    }
}