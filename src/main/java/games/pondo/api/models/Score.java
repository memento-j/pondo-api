package games.pondo.api.models;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table (
    name = "scores",
    uniqueConstraints = @UniqueConstraint(columnNames = {"game", "puzzle", "device_id"})
)
public class Score {
    //creates primary key
    @Id 
    //generated value is only for the @id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //incase i add signup in the future
    private UUID userId;
    
    //comes from the local storage on the frontend
    @Column(nullable = false)
    private UUID deviceId;

    @Column(nullable = false, length = 20)
    private String game;

    //which day puzzle it is ex: Slice #13: game = slice, puzzle = 13
    @Column(nullable = false)
    private Integer puzzle;

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private List<Integer> roundScores;

    @Column(nullable = false)
    private List<Boolean> hintsUsed;

    //created by the server
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    //default contstructor
    public Score() {
    }

    //id, createdat, and userid are filled in automatically
    public Score(UUID deviceId, String game, Integer puzzle, Integer totalScore, List<Integer> roundScores, List<Boolean> hintsUsed) {;
        this.deviceId = deviceId;
        this.game = game;
        this.puzzle = puzzle;
        this.totalScore = totalScore;
        this.roundScores = roundScores;
        this.hintsUsed = hintsUsed;
    }


    //getters (no setters since submitted scores cant be changed)

    public UUID getId() {
        return this.id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UUID getDeviceId() {
        return this.deviceId;
    }

    public String getGame() {
        return this.game;
    }

    public Integer getPuzzle() {
        return this.puzzle;
    }

    public Integer getTotalScore() {
        return this.totalScore;
    }

    public List<Integer> getRoundScores() {
        return this.roundScores;
    }

    public List<Boolean> getHintsUsed() {
        return this.hintsUsed;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }



    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;
        Score that = (Score) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}