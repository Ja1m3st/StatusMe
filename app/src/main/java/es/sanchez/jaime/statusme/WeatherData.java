package es.sanchez.jaime.statusme;

public class WeatherData {
    private Main main;
    private String name;

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public static class Main {
        private double temp;

        public double getTemp() {
            return temp;
        }
    }
}