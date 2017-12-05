package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 05/12/2017.
 */

public class Score {
    private int wonGames;
    private int lostGames;

    public Score() {
    }

    public Score(int wonGames, int lostGames) {
        this.wonGames = wonGames;
        this.lostGames = lostGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }
}
