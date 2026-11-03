package games.pondo.api.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import games.pondo.api.dtos.PuzzleScoreResponse;
import games.pondo.api.dtos.RoundScoreCount;
import games.pondo.api.dtos.ScoreCount;
import games.pondo.api.dtos.SubmitScoreRequest;
import games.pondo.api.models.Game;
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
        String gameName = game.toUpperCase();
        List<ScoreCount> totalScores = scoreRepository.totalDistribution(gameName, puzzle);
        List<RoundScoreCount> roundScores = scoreRepository.roundDistribution(gameName, puzzle);

        return new PuzzleScoreResponse(totalScores, roundScores);
    }

    //when submitting a score, also return the puzzle scores so they can 
    //immediately be displayed without making another api call
    public PuzzleScoreResponse submitPuzzleScore(SubmitScoreRequest scoreReq) {
        //validate the score request to ensure its formatted properly
        Game currentGame = validateScoreRequest(scoreReq);
        String gameName = currentGame.name();

        //check if the device already has scores added for this puzzle. 
        //(since game, puzzle, deviceid must be unique)
        if(!scoreRepository.existsByGameAndPuzzleAndDeviceId(gameName, scoreReq.puzzle(), scoreReq.deviceId())) {
            try {
                //if it's not in there, add the score
                scoreRepository.save(
                    new Score(
                        scoreReq.deviceId(),
                        gameName,
                        scoreReq.puzzle(),
                        scoreReq.totalScore(),
                        scoreReq.roundScores(),
                        scoreReq.hintsUsed()
                    )
                );
            } catch(DataIntegrityViolationException e) {
                //database rejects write because a rule on the table is broken
                //ex: unique constraint, not null column is null, etc.
            }
        }

        //after that check, return all the scores
        return getPuzzleScores(gameName, scoreReq.puzzle());
    }

    //validates the score request against the game's rules, throws a 400 if anything is off
    //returns the matching Game so the caller doesn't have to look it up again
    private Game validateScoreRequest(SubmitScoreRequest scoreRequest) {
        //turn "slice" into Game.SLICE, unknown names are rejected
        Game game;
        try {
            game = Game.valueOf(scoreRequest.game().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw badRequest("unknown game: " + scoreRequest.game());
        }

        List<Integer> rounds = scoreRequest.roundScores();
        List<Boolean> hints = scoreRequest.hintsUsed();

        //right number of rounds for this game
        if (rounds.size() != game.getTotalRounds()) {
            throw badRequest("expected " + game.getTotalRounds() + " rounds, got " + rounds.size());
        }

        //one hint flag per round
        if (hints.size() != rounds.size()) {
            throw badRequest("hintsUsed must have one entry per round");
        }

        //each round within range, and add them up while we're at it
        int sum = 0;
        for (Integer round : rounds) {
            if (round == null || round < 0 || round > game.getMaxRoundScore()) {
                throw badRequest("round score out of range: " + round);
            }
            sum += round;
        }

        //total has to match the rounds
        if (sum != scoreRequest.totalScore()) {
            throw badRequest("totalScore does not match the sum of the rounds");
        }

        return game;
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
