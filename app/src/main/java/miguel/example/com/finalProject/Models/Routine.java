package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 04/12/2017.
 */

public class Routine {
    private String activity;
    private String date;
    private String time;
    private String baseActivity;
    public Routine() {
    }

    public Routine(String activity, String date, String time, String baseActivity) {
        this.activity = activity;
        this.date = date;
        this.time = time;
        this.baseActivity = baseActivity;
    }

    public String getBaseActivity() {
        return baseActivity;
    }

    public void setBaseActivity(String baseActivity) {
        this.baseActivity = baseActivity;
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

    public String getDateTime(){
        return date +" a las "+ time;
    }
}
