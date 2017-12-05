package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 04/12/2017.
 */

public class Routine {
    private String activity;
    private String date;
    private String time;

    public Routine() {
    }

    public Routine(String activity, String date, String time) {
        this.activity = activity;
        this.date = date;
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
