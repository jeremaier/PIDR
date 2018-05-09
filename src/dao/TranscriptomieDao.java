package src.dao;
import src.table.TranscriptomicAnalysis;
import java.util.List;

public interface TranscriptomieDao {

    void insert(TranscriptomicAnalysis Transcriptomie);

    TranscriptomicAnalysis selectById(int id);

    TranscriptomicAnalysis selectBySite(int id);

    List<TranscriptomicAnalysis> selectAll();

    void update(TranscriptomicAnalysis transcriptomie, int id);

    int getLast();

    List<Integer> idList();



}
