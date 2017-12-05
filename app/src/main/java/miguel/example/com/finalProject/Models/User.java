package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 28/11/2017.
 */

public class User {

    public String birthDate;
    public String name;
    public String email;
    public String genre;

    public User(){

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

}
