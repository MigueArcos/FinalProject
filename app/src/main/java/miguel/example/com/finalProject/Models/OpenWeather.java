package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 05/12/2017.
 */

public class OpenWeather {
    private String name;
    private Weather[] weather;
    private Main main;

    public String getCityName() {
        return "Ciudad: " + name;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public class Weather{
        private String main;
        private String description;
        private String icon;

        public String getMain() {
            return main;
        }

        public String getFormattedDescription() {
            return description.substring(0, 1).toUpperCase() + description.substring(1);
        }

        public String getIcon() {
            return icon;
        }

        public String getIconURL(){
            return "http://openweathermap.org/img/w/"+icon+".png";
        }
    }
    public class Main{
        private double temp;
        private double temp_min;
        private double temp_max;
        private double pressure;
        private double humidity;

        public double getTemp() {
            return temp;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public double getPressure() {
            return pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public String getFormattedTemp(){
            return "Temperatura: "+temp+"°C";
        }
        public String getFormattedMinTemp(){
            return "Temperatura mínima: "+temp_min+"°C";
        }
        public String getFormattedMaxTemp(){
            return "Temperatura máxima: "+temp_max+"°C";
        }
    }
}
