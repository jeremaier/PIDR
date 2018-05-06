package src.dao;

import src.table.Inclusion;
import src.utils.Diag;

import java.sql.Date;
import java.util.List;

public interface InclusionDao {
    void insert(Inclusion inclusion);

    Inclusion selectById(int id);

    List<Inclusion> selectByFilters(int id, Date dateInclusion, int numAnaPat, String initiales, Diag diag);

    List<Inclusion> selectAll();

    void update(Inclusion inclusion, int id);

    /*static void updateDiag(String diag, int id) {
        PreparedStatement preparedStatement = null;

        try {
            String diagQuery = InclusionDaoImpl.selectDiag(id);
            boolean diagPresent = false;

            if (diagQuery != null) {
                if (!diagQuery.equals("")) {
                    for (String diags : diagQuery.split(" - ")) {
                        if (diags.equals(diag))
                            diagPresent = true;
                    }
                }
            } else diagQuery = "";

            if (!diagPresent) {
                preparedStatement = SQLConnection.getConnection().prepareStatement("UPDATE inclusion SET " + "DIAG = ? WHERE ID = ?");
                preparedStatement.setString(1, diagQuery.concat(" - ").concat(diag));
                preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
                System.out.println("UPDATE inclusion SET " + "DIAG = ? WHERE ID = ?");
            }
        } catch (MySQLNonTransientConnectionException e) {
            FileManager.openAlert("La InclusionDaoImpl.connection avec le serveur est interrompue");
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
    }*/
}
