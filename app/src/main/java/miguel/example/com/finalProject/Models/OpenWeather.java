package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 05/12/2017.
 */

public class OpenWeather {
    private String name;
    private Weather[] weather;
    private Main main;

    public String getName() {
        return name;
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

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
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
    }
}
