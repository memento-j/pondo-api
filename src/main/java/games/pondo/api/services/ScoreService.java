package games.pondo.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import games.pondo.api.dtos.ScoreCount;
import games.pondo.api.dtos.RoundScoreCount;
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

    //the overall scores for the current puzzle (every round added up)
    public List<ScoreCount> getPuzzleScores(String game, Integer puzzle) {
        return null;
    }

    //when clicking on a specific round for that puzzle, this returns the data for that round
    //all rounds are returned and the client determines what is displayed so there is only one api call
    public List<RoundScoreCount> getRoundScores(String game, Integer puzzle) {
        return null;
    }

    //when submitting a score, also return the puzzle scores so they can 
    //immediately be displayed without making another api call
    public List<ScoreCount> addPuzzleScores() {
        return null;
    }
}
