package games.pondo.api.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

//record = special java class made for holding data, list the fields in the header
// and java does the rest for you. A contructor, getts, equals, hascode, etc.
public record SubmitScoreRequest (
    //validations for the fields submitted by the client
    @NotNull
    UUID deviceId,

    //not blank used for strings. (not null and not only spaces)
    @NotBlank 
    @Size(max = 20, message = "game title can be at most 20 characters")
    String game,

    @NotNull 
    @Positive(message = "puzzle number must be positive")
    Integer puzzle,

    @NotNull  
    @Min(value = 0, message = "Lowest possible score is 0") 
    @Max (value = 400, message= "Highest possible score is 400")
    Integer totalScore,

    //use list here so each element can be validating using list methods
    @NotNull 
    @Size(min = 4, max = 5)
    List<@Min(0) @Max(100) Integer> roundScores,

    @NotNull 
    @Size(min = 4, max = 5)
    Boolean[] hintsUsed
) {}
