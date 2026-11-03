package games.pondo.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import games.pondo.api.dtos.PuzzleScoreResponse;
import games.pondo.api.dtos.RoundScoreCount;
import games.pondo.api.dtos.ScoreCount;
import games.pondo.api.dtos.SubmitScoreRequest;
import games.pondo.api.models.Score;
import games.pondo.api.repositories.ScoreRepository;

//business logic
@Service 
public class ScoreService {
    //gives this class (score service) access to the score repository
    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    //now the methods that provide the business logic for the score service

    //the overall scores for the current puzzle 
    //(the total scores along with the scores for each round)
    public PuzzleScoreResponse getPuzzleScores(String game, Integer puzzle) {
        List<ScoreCount> totalScores = scoreRepository.totalDistribution(game, puzzle);
        List<RoundScoreCount> roundScores = scoreRepository.roundDistribution(game, puzzle);

        return new PuzzleScoreResponse(totalScores, roundScores);
    }

    //when submitting a score, also return the puzzle scores so they can 
    //immediately be displayed without making another api call
    public PuzzleScoreResponse submitPuzzleScore(SubmitScoreRequest scoreReq) {
        //check if the device already has scores added for this puzzle. 
        //(since game, puzzle, deviceid must be unique)
        if(!scoreRepository.existsByGameAndPuzzleAndDeviceId(scoreReq.game(), scoreReq.puzzle(), scoreReq.deviceId())) {
            //if it's not in there, add the score
            scoreRepository.save(
                new Score(
                    scoreReq.deviceId(),
                    scoreReq.game(),
                    scoreReq.puzzle(),
                    scoreReq.totalScore(),
                    scoreReq.roundScores(),
                    scoreReq.hintsUsed()
                )
            );
        }

        //after that check, return all the scores
        return getPuzzleScores(scoreReq.game(), scoreReq.puzzle());
    }
}
