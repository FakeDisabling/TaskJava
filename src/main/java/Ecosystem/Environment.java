package Ecosystem;

public class Environment {
    private double temperature;
    private int humidity;
    private int water;
    private int sunlight;

    public Environment(double temperature, int humidity, int water, int sunlight) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.water = water;
        this.sunlight = sunlight;
    }
    public Environment() {

    }
    public double getTemperature() {
        return temperature;
    }

    public int getSunlight() {
        return sunlight;
    }

    public void setSunlight(int sunlight) {
        this.sunlight = sunlight;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    @Override
    public String toString() {
        return "Environment {" +
                " Temperature: " + temperature + "," +
                " Humidity: " + humidity + "," +
                " Water: " + water + "," +
                " Sunlight: " + sunlight +
                " }";
    }
}
