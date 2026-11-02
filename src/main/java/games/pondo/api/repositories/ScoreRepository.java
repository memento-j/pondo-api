package games.pondo.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import games.pondo.api.dtos.RoundScoreCount;
import games.pondo.api.dtos.ScoreCount;
import games.pondo.api.models.Score;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
    //for idempotency, if a post request fails after already saving, we check
    //if the score has already been added so we dont get an error adding the same thing twice
    boolean existsByGameAndPuzzleAndDeviceId(String game, Integer puzzle, UUID deviceId);

    //:game and :puzzle are what is passed in as arguments
    //get all scores and their counts 
    @Query(value = """
        SELECT total_score AS score, COUNT(*) AS count
        FROM scores
        WHERE game = :game AND puzzle = :puzzle
        GROUP BY total_score
        ORDER BY total_score
        """, nativeQuery = true)
    List<ScoreCount> totalDistribution(String game, Integer puzzle);

    //unnest splits the array of round scores into seperate rows
    //with ordinality adds a second column that numbers each row
    //pts = round points idx = index of the current round
    @Query(value = """
        SELECT r.idx AS round, r.pts AS score, COUNT(*) AS count
        FROM scores s,
             unnest(s.round_scores) WITH ORDINALITY AS r(pts, idx)
        WHERE s.game = :game AND s.puzzle = :puzzle
        GROUP BY r.idx, r.pts
        ORDER BY r.idx, r.pts
        """, nativeQuery = true)
    List<RoundScoreCount> roundDistribution(String game, Integer puzzle);
}
