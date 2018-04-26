package src.daoImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.dao.TranscriptomieDao;
import src.table.TranscriptomicAnalysis;

import java.sql.*;
import java.util.List;

public class TranscriptomieDaompl extends daoImpl implements TranscriptomieDao {
    private Connection connection;

    public TranscriptomieDaompl (Connection connection) { this.connection = connection;}


    @Override
    public void insert(TranscriptomicAnalysis transcr) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO analyse_transcriptomique (ID, ID_SITE_CUTANE, FICHIER_BRUT, FICHIER_CUT, RNA, ARNC, CY3, CONCENTRATION, RENDEMENT, ACTIVITE_SPECIFIQUE, CRITERE_EXCLUSION, NUM_SERIE, NUM_EMPLACEMENT, QUALITY_REPORT)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = this.setPreparedStatement(preparedStatement, transcr, 1);
            preparedStatement.executeUpdate();
            System.out.println("INSERT INTO analyse_transcriptomique (ID, ID_SITE_CUTANE, FICHIER_BRUT, FICHIER_CUT, RNA, ARNC, CY3, CONCENTRATION, RENDEMENT, ACTIVITE_SPECIFIQUE, CRITERE_EXCLUSION, NUM_SERIE, NUL_EMPLACEMENT, QUALITY_REPORT)\" + \"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
        TranscriptomicAnalysis transcr = new TranscriptomicAnalysis();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM analyse_transcriptomique WHERE ID = ?");
            preparedStatement.setInt(1, transcr.getId());
            resultSet = preparedStatement.executeQuery();
            transcr = this.addToTranscriptomie(transcr, resultSet);
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

        return transcr;
    }

    @Override
    public List<TranscriptomicAnalysis> selectAll() {
        ObservableList<TranscriptomicAnalysis> transcr = FXCollections.observableArrayList();
        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM analyse_transcriptomique");
            transcr = this.addToObservableList(transcr, resultSet);

            System.out.println("SELECT * FROM site_cutane");
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

        return transcr;
    }

    @Override
    public void update(TranscriptomicAnalysis transcr, int id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE analyse_transcriptomique SET " + "ID_SITE_CUTANE = ?, FICHIER_BRUT = ?, FICHIER_CUT = ?, RNA = ?, ARNC = ?, CY3 = ?, CONCENTRATION =?, RENDEMENT = ?, ACTIVITE_SPECIFIQUE = ?, CRITERE_EXCLUSION = ?, NUM_SERIE = ?, NUM_EMPLACEMENT = ?, NUM_EMPLACEMENT = ?, QUALITY_REPORT =? WHERE ID = ?");
            preparedStatement = this.setPreparedStatement(preparedStatement, transcr, 0);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            System.out.println("UPDATE analyse_transcriptomique SET" + "ID_SITE_CUTANE = ?, FICHIER_BRUT = ?, FICHIER_CUT = ?, RNA = ?, ARNC = ?, CY3 = ?, CONCENTRATION =?, RENDEMENT = ?, ACTIVITE_SPECIFIQUE = ?, CRITERE_EXCLUSION = ?, NUM_SERIE = ?, NUM_EMPLACEMENT = ?, NUM_EMPLACEMENT = ?, QUALITY_REPORT =? WHERE ID = ?");
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

    public void delete(int id){
        this.delete(connection,"analyse_transcriptomique", id);
    }

    private ObservableList<TranscriptomicAnalysis> addToObservableList(ObservableList<TranscriptomicAnalysis> transcr, ResultSet resultSet) {
        try {
            while (resultSet.next())
                transcr.add(this.addToTranscriptomie(new TranscriptomicAnalysis(), resultSet));

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

        return transcr;
    }

    private TranscriptomicAnalysis addToTranscriptomie(TranscriptomicAnalysis transcr, ResultSet resultSet) throws SQLException {

        transcr.setId(resultSet.getInt("ID"));
        transcr.setIdCutaneousSite(resultSet.getInt("ID_SITE_CUTANE"));
        transcr.setFichierBrut( resultSet.getString("FICHIER_BRUT"));
        transcr.setFichierCut(resultSet.getString("FICHIER_CUT"));
        transcr.setRIN(resultSet.getInt("RIN"));
        transcr.setConcentration(resultSet.getDouble("CONCENTRATION"));
        transcr.setARNC(resultSet.getDouble("ARNC"));
        transcr.setCyanine(resultSet.getDouble("CY3"));
        transcr.setSpecificActivity(resultSet.getString("ACTIVITE_SPECIFIQUE"));
        transcr.setExclusionCriteria(resultSet.getString("CRITERE_EXCLUSION"));
        transcr.setSerialNumber(resultSet.getInt("NUM_SERIE"));
        transcr.setLamellaLocation(resultSet.getInt("NUM_EMPLACEMENT"));
        transcr.setQualityReport(resultSet.getString("QUALITY_REPORT"));
        return transcr;
    }

    protected PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Object object, int indexDebut) throws SQLException {
        if (indexDebut == 1)
            preparedStatement.setInt(indexDebut, ((TranscriptomicAnalysis) object).getId());

        preparedStatement.setInt(indexDebut + 1, ((TranscriptomicAnalysis) object).getId());
        preparedStatement.setInt(indexDebut + 2, ((TranscriptomicAnalysis) object).getIdCutaneousSite());
        preparedStatement.setString(indexDebut + 3, ((TranscriptomicAnalysis) object).getFichierBrut());
        preparedStatement.setString(indexDebut + 4, ((TranscriptomicAnalysis) object).getFichierCut());
        preparedStatement.setInt(indexDebut + 5, ((TranscriptomicAnalysis) object).getRIN());
        preparedStatement.setDouble(indexDebut + 6, ((TranscriptomicAnalysis) object).getConcentration());
        preparedStatement.setDouble(indexDebut + 7, ((TranscriptomicAnalysis) object).getARNC());
        preparedStatement.setDouble(indexDebut + 8, ((TranscriptomicAnalysis) object).getCyanine());
        preparedStatement.setDouble(indexDebut + 9, ((TranscriptomicAnalysis) object).getYield());
        preparedStatement.setString(indexDebut + 10, ((TranscriptomicAnalysis) object).getSpecificActivity());
        preparedStatement.setString(indexDebut + 11, ((TranscriptomicAnalysis) object).getExclusionCriteria());
        preparedStatement.setInt(indexDebut + 12, ((TranscriptomicAnalysis) object).getSerialNumber());
        preparedStatement.setInt(indexDebut + 13, ((TranscriptomicAnalysis) object).getLamellaLocation());
        preparedStatement.setString(indexDebut + 14, ((TranscriptomicAnalysis) object).getQualityReport());


        return preparedStatement;
    }


}
