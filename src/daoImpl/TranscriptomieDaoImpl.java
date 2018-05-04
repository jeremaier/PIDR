package src.daoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.TranscriptomieDao;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.SQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranscriptomieDaoImpl extends DaoImpl implements TranscriptomieDao {
    private static Connection connection;

    public TranscriptomieDaoImpl(Connection connection) {
        TranscriptomieDaoImpl.connection = connection;
    }

    @Override
    public void insert(TranscriptomicAnalysis transcr) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = TranscriptomieDaoImpl.connection.prepareStatement("INSERT INTO analyse_transcriptomique (ID, ID_SITE_CUTANE, FICHIER_BRUT, RNA, ARNC, CY3, CONCENTRATION, RENDEMENT, ACTIVITE_SPECIFIQUE, CRITERE_EXCLUSION, NUM_SERIE, NUM_EMPLACEMENT, QUALITY_REPORT)" + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, transcr, 1);
            preparedStatement.executeUpdate();

            System.out.println("INSERT INTO analyse_transcriptomique (ID, ID_SITE_CUTANE, FICHIER_BRUT, RNA, ARNC, CY3, CONCENTRATION, RENDEMENT, ACTIVITE_SPECIFIQUE, CRITERE_EXCLUSION, NUM_SERIE, NUL_EMPLACEMENT, QUALITY_REPORT)");
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

    @Override
    public TranscriptomicAnalysis selectById(int id) {
        TranscriptomicAnalysis transcr = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = TranscriptomieDaoImpl.connection.prepareStatement("SELECT * FROM analyse_transcriptomique WHERE ID = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                transcr = this.addToTranscriptomie(resultSet);

            System.out.println("SELECT * FROM analyse_transcriptomique WHERE ID ORDER BY ID");
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

        return transcr;
    }

    @Override
    public TranscriptomicAnalysis selectBySite(int id) {
        TranscriptomicAnalysis transcr = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = TranscriptomieDaoImpl.connection.prepareStatement("SELECT * FROM analyse_transcriptomique WHERE ID_SITE_CUTANE = ? ORDER BY ID");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                transcr = this.addToTranscriptomie(resultSet);

            System.out.println("SELECT * FROM analyse_transcriptomique WHERE ID_SITE_CUTANE ORDER BY ID");
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

        return transcr;
    }

    @Override
    public List<TranscriptomicAnalysis> selectAll() {
        ObservableList<TranscriptomicAnalysis> transcr = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = TranscriptomieDaoImpl.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM analyse_transcriptomique ORDER BY ID");

            while (resultSet.next())
                transcr.add(this.addToTranscriptomie(resultSet));

            System.out.println("SELECT * FROM site_cutane ORDER BY ID");
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
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return transcr;
    }

    @Override
    public void update(TranscriptomicAnalysis transcr, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = TranscriptomieDaoImpl.connection.prepareStatement("UPDATE analyse_transcriptomique SET " + "ID_SITE_CUTANE = ?, FICHIER_BRUT = ?, FICHIER_CUT = ?, RNA = ?, ARNC = ?, CY3 = ?, CONCENTRATION =?, RENDEMENT = ?, ACTIVITE_SPECIFIQUE = ?, CRITERE_EXCLUSION = ?, NUM_SERIE = ?, NUM_EMPLACEMENT = ?, NUM_EMPLACEMENT = ?, QUALITY_REPORT =? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, transcr, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE analyse_transcriptomique SET" + "ID_SITE_CUTANE = ?, FICHIER_BRUT = ?, FICHIER_CUT = ?, RNA = ?, ARNC = ?, CY3 = ?, CONCENTRATION =?, RENDEMENT = ?, ACTIVITE_SPECIFIQUE = ?, CRITERE_EXCLUSION = ?, NUM_SERIE = ?, NUM_EMPLACEMENT = ?, NUM_EMPLACEMENT = ?, QUALITY_REPORT =? WHERE ID = ?");
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

    private TranscriptomicAnalysis addToTranscriptomie(ResultSet resultSet) throws SQLException {
        TranscriptomicAnalysis transcriptomicAnalysis = new TranscriptomicAnalysis();

        transcriptomicAnalysis.setId(resultSet.getInt("ID"));
        transcriptomicAnalysis.setIdCutaneousSite(resultSet.getInt("ID_SITE_CUTANE"));
        transcriptomicAnalysis.setFichierBrut(resultSet.getString("FICHIER_BRUT"));
        transcriptomicAnalysis.setRIN(resultSet.getInt("RIN"));
        transcriptomicAnalysis.setConcentration(resultSet.getDouble("CONCENTRATION"));
        transcriptomicAnalysis.setARNC(resultSet.getDouble("ARNC"));
        transcriptomicAnalysis.setCyanine(resultSet.getDouble("CY3"));
        transcriptomicAnalysis.setSpecificActivity(resultSet.getString("ACTIVITE_SPECIFIQUE"));
        transcriptomicAnalysis.setExclusionCriteria(resultSet.getString("CRITERE_EXCLUSION"));
        transcriptomicAnalysis.setSerialNumber(resultSet.getInt("NUM_SERIE"));
        transcriptomicAnalysis.setLamellaLocation(resultSet.getInt("NUM_EMPLACEMENT"));
        transcriptomicAnalysis.setQualityReport(resultSet.getString("QUALITY_REPORT"));

        return transcriptomicAnalysis;
    }

    public static void delete(int id) {
        TranscriptomieDaoImpl.delete(SQLConnection.getConnection(), "analyse_transcriptomique", id);
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((TranscriptomicAnalysis) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((TranscriptomicAnalysis) object).getIdCutaneousSite());
        preparedStatement.setString(indexDebut + 2, ((TranscriptomicAnalysis) object).getFichierBrut());
        preparedStatement.setInt(indexDebut + 3, ((TranscriptomicAnalysis) object).getRIN());
        preparedStatement.setDouble(indexDebut + 4, ((TranscriptomicAnalysis) object).getConcentration());
        preparedStatement.setDouble(indexDebut + 5, ((TranscriptomicAnalysis) object).getARNC());
        preparedStatement.setDouble(indexDebut + 6, ((TranscriptomicAnalysis) object).getCyanine());
        preparedStatement.setDouble(indexDebut + 7, ((TranscriptomicAnalysis) object).getYield());
        preparedStatement.setString(indexDebut + 8, ((TranscriptomicAnalysis) object).getSpecificActivity());
        preparedStatement.setString(indexDebut + 9, ((TranscriptomicAnalysis) object).getExclusionCriteria());
        preparedStatement.setInt(indexDebut + 10, ((TranscriptomicAnalysis) object).getSerialNumber());
        preparedStatement.setInt(indexDebut + 11, ((TranscriptomicAnalysis) object).getLamellaLocation());
        preparedStatement.setString(indexDebut + 12, ((TranscriptomicAnalysis) object).getQualityReport());

        return preparedStatement;
    }

    public static ArrayList<TranscriptomicAnalysis> removeLamellas(String id) {
        ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = SQLConnection.getConnection().prepareStatement("SELECT ID, ID_SITE_CUTANE, FICHIER_BRUT FROM analyse_transcriptomique WHERE ID_SITE_CUTANE = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TranscriptomicAnalysis transcriptomicAnalysis = new TranscriptomicAnalysis();

                transcriptomicAnalysis.setId(resultSet.getInt("ID"));
                transcriptomicAnalysis.setIdCutaneousSite(resultSet.getInt("ID_SITE_CUTANE"));
                transcriptomicAnalysis.setFichierBrut(resultSet.getString("FICHIER_BRUT"));

                transcriptomicAnalyses.add(transcriptomicAnalysis);
            }

            System.out.println("SELECT ID, ID_SITE_CUTANE, FICHIER_BRUT FROM analyse_transcriptomique WHERE ID_SITE_CUTANE = ?");
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

        return transcriptomicAnalyses;
    }
}