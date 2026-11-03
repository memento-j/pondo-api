package games.pondo.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import games.pondo.api.dtos.PuzzleScoreResponse;
import games.pondo.api.dtos.SubmitScoreRequest;
import games.pondo.api.services.ScoreService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping("/api/v1/scores")
public class ScoreController {
    private final ScoreService scoreService;

    ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public PuzzleScoreResponse getPuzzleScores(@RequestParam String game, @RequestParam int puzzle) {
        return scoreService.getPuzzleScores(game, puzzle);
    }

    @PostMapping
    //without @valid the dto annotations for validation will not run
    public PuzzleScoreResponse submitScore(@Valid @RequestBody SubmitScoreRequest scoreRequest) {        
        return scoreService.submitPuzzleScore(scoreRequest);
    }

}
