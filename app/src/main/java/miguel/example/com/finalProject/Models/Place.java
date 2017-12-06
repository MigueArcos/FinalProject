package miguel.example.com.finalProject.Models;

/**
 * Created by 79812 on 02/12/2017.
 */

public class Place {
    private Geometry geometry;
    private String icon;
    private String name;
    private String vicinity;
    private String[] types;

    public String[] getTypes() {
        return types;
    }
    public boolean checkIfPlaceIsAnEstablishment(){
        boolean isEstablishment = false;
        for (String type: types){
            if (type.equalsIgnoreCase("establishment")){
                return true;
            }
        }
        return isEstablishment;
    }
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public class Location {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
    }
}
