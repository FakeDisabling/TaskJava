package Ecosystem;

public class Plant extends Species {
    private int age;
    private int waterNeed;
    private int lightNeed;
    public Plant(String name, int population, double minTemperatureTolerance, double maxTemperatureTolerance, int age, int waterNeed, int lightNeed) {
        super(name, population, minTemperatureTolerance, maxTemperatureTolerance);
        this.age = age;
        this.waterNeed = waterNeed;
        this.lightNeed = lightNeed;
    }

    public Plant(String name, int population, double minTemperatureTolerance, double maxTemperatureTolerance) {
        super(name, population, minTemperatureTolerance, maxTemperatureTolerance);
    }
    public int getAge() {
        return age;
    }

    public int getwaterNeed() {
        return waterNeed;
    }

    public int getlightNeed() {
        return lightNeed;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public void setwaterNeed(int waterNeed) {
        this.waterNeed = waterNeed;
    }

    public void setlightNeed(int lightNeed) {
        this.lightNeed = lightNeed;
    }

    @Override
    public String toString() {
        return "Plant {" +
                " Name: " + getName() + "," +
                " Population: " + getPopulation() + "," +
                " Max Temperature: " + getMaxTemperatureTolerance() + "," +
                " Min Temperature: " + getMinTemperatureTolerance() + "," +
                " Age: " + age + "," +
                " Water Need: " + waterNeed + "," +
                " Light Need: " + lightNeed +
                " }";
    }
}
