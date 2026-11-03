package games.pondo.api.dtos;

import java.util.List;

public record PuzzleScoreResponse(
    List<ScoreCount> totalScores,

    List<RoundScoreCount> roundScores
) {}
