package games.pondo.api.dtos;

//used by spring to translate postgres rows to a proper java type
public interface ScoreCount {
    Integer getScore();
    Long getCount();
}