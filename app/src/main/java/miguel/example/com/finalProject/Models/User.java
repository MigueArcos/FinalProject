package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 28/11/2017.
 */

public class User {

    private String birthDate;
    private String name;
    private String email;
    private String genre;
    private Score gamesScore;
    public User(){

    }

    public Score getGamesScore() {
        return gamesScore;
    }

    public void setGamesScore(Score gamesScore) {
        this.gamesScore = gamesScore;
    }

    public User(String birthDate, String name, String email, String genre) {
        this.birthDate = birthDate;
        this.name = name;
        this.email = email;
        this.genre = genre;
    }


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static class Score {
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

}
