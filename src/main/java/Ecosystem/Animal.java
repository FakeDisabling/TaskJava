package Ecosystem;

public class Animal extends Species {
    private int age;
    private String typeFood;
    private int power;
    private int hungry;
    private int thirst;

    public Animal(String name, int population, double minTemperatureTolerance, double maxTemperatureTolerance, int age, String typeFood, int power, int hungry, int thirst) {
        super(name, population, minTemperatureTolerance, maxTemperatureTolerance);
        this.age = age;
        this.typeFood = typeFood;
        this.power = power;
        this.hungry = hungry;
        this.thirst = thirst;
    }

    public Animal(String name, int population, double minTemperatureTolerance, double maxTemperatureTolerance) {
        super(name, population, minTemperatureTolerance, maxTemperatureTolerance);
    }
    public int getAge() {
        return age;
    }

    public String getTypeFood() {
        return typeFood;
    }

    public int getPower() {
        return power;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTypeFood(String typeFood) {
        this.typeFood = typeFood;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public int getHungry() {
        return hungry;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }
    public Boolean canEat(Animal otheranimal)
    {
        return this.typeFood.equals("carnivore") && this.power > otheranimal.getPower();
    }

    public Boolean canEat(Plant plant)
    {
        return this.typeFood.equals("herbivore");
    }
    @Override
    public String toString() {
        return "Animal {" +
                " Name: " + getName() + "," +
                " Population: " + getPopulation() + "," +
                " Min Temperature: " + getMinTemperatureTolerance() + "," +
                " Max Temperature: " + getMaxTemperatureTolerance() + "," +
                " Age: " + age + "," +
                " Type of Food: " + typeFood + "," +
                " Power: " + power + "," +
                " Hunger Level: " + hungry + "," +
                " Thirst Level: " + thirst +
                "}";
    }

}
