package games.pondo.api.models;

public enum Game {
    SLICE(4, 100),
    DOWNSTREAM(5, 4);

    private final int totalRounds;
    private final int maxRoundScore;

    Game(int totalRounds, int maxRoundScore) {
        this.totalRounds = totalRounds;
        this.maxRoundScore = maxRoundScore;
    }

    public int getTotalRounds() { return this.totalRounds; } 
    public int getMaxRoundScore() { return this.maxRoundScore; } 
} 