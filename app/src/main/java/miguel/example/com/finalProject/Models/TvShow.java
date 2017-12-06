package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 05/12/2017.
 */

public class TvShow {
    private String name;
    private String[] genres;
    private Image image;
    private String language;
    private String officialSite;
    private String summary;
    private String url;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public Image getImage() {
        return image;
    }

    public String getLanguage() {
        return language;
    }

    public String getOfficialSite() {
        return officialSite;
    }

    public String getSummary() {
        return summary;
    }

    public String getFormattedGenre (){
        StringBuilder genre = new StringBuilder();
        for (int i = 0; i< genres.length; i++){
            genre.append(genres[i]+ ", ");
        }
        genre.delete(genre.length()-2, genre.length());
        return genre.toString();
    }

    public class Image {
        private String medium;
        private String original;

        public String getMedium() {
            return medium;
        }

        public String getOriginal() {
            return original;
        }
    }
}
