package Analysis;

import Ecosystem.Ecosystem;
import Ecosystem.Animal;
import Ecosystem.Plant;
import org.example.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimulationEcosystem {
    private Ecosystem ecosystem;
    private PredictionEcosystem predictionEcosystem;
    private Logger logger;
    private int dayCounter;

    public SimulationEcosystem(Ecosystem ecosystem, String logFilePath) {
        this.ecosystem = ecosystem;
        this.predictionEcosystem = new PredictionEcosystem();
        this.logger = new Logger(logFilePath);
        this.dayCounter = 1;  // Начинаем отсчет дней с 1
    }

    public void simulateDay() {
        logger.logInfo("Day " + dayCounter + " begins.");

        updateEnvironment();

        ecosystem.getAnimalList().forEach(this::processAnimalActions);
        ecosystem.getPlantList().forEach(this::processPlantActions);

        checkWhoIsDeadAnimal(ecosystem.getAnimalList());
        checkWhoIsDeadPlant(ecosystem.getPlantList());

        logSurvivingEntities();

        dayCounter++;
    }

    private void updateEnvironment() {
        double temperatureRandom = ThreadLocalRandom.current().nextDouble(-0.5, 0.5) + ecosystem.getEnvironment().getTemperature();
        ecosystem.getEnvironment().setTemperature(temperatureRandom);

        int humidityRandom = ThreadLocalRandom.current().nextInt(-1, 2) + ecosystem.getEnvironment().getHumidity();
        ecosystem.getEnvironment().setHumidity(Math.max(0, Math.min(100, humidityRandom)));

        int sunlightRandom = ThreadLocalRandom.current().nextInt(-1, 2) + ecosystem.getEnvironment().getSunlight();
        ecosystem.getEnvironment().setSunlight(Math.max(0, Math.min(100, sunlightRandom)));

        randomEvent();
    }

    private void randomEvent() {
        double random = ThreadLocalRandom.current().nextDouble();
        String eventMessage;

        if (random >= 0.0 && random <= 0.2) {
            int rainIncrease = ThreadLocalRandom.current().nextInt(10000, 10000000);
            ecosystem.getEnvironment().setHumidity(Math.min(100, ecosystem.getEnvironment().getHumidity() + 5));
            ecosystem.getEnvironment().setWater(ecosystem.getEnvironment().getWater() + rainIncrease);
            eventMessage = "Rain event occurred. Water increased by " + rainIncrease;
        } else if (random > 0.2 && random < 0.23) {
            double heatIncrease = ThreadLocalRandom.current().nextDouble(2, 5);
            int humidityDecrease = ThreadLocalRandom.current().nextInt(5, 11);
            ecosystem.getEnvironment().setTemperature(ecosystem.getEnvironment().getTemperature() + heatIncrease);
            ecosystem.getEnvironment().setHumidity(Math.max(0, ecosystem.getEnvironment().getHumidity() - humidityDecrease));
            eventMessage = "Heatwave event occurred. Temperature increased by " + heatIncrease + ", Humidity decreased by " + humidityDecrease;
        } else if (random > 0.23 && random < 0.3) {
            int floodIncreaseWater = ThreadLocalRandom.current().nextInt(100000, 100000000);
            int floodIncreaseHumidity = ThreadLocalRandom.current().nextInt(5, 15);
            ecosystem.getEnvironment().setWater(ecosystem.getEnvironment().getWater() + floodIncreaseWater);
            ecosystem.getEnvironment().setHumidity(Math.min(100, ecosystem.getEnvironment().getHumidity() + floodIncreaseHumidity));
            eventMessage = "Flood event occurred. Water increased by " + floodIncreaseWater + ", Humidity increased by " + floodIncreaseHumidity;
        } else {
            eventMessage = "No special event today.";
        }

        logger.logInfo(eventMessage);
    }

    private void processAnimalActions(Animal animal) {
        double survivalRate = predictionEcosystem.calculateAnimalSurvivalRate(animal, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());
        int amount = (int) Math.min(0.25 * animal.getPopulation(), 10);

        if (survivalRate == 0.0)
            animal.setPopulation(0);

        if (survivalRate < 1.0) {
            amount = (int) (5 - (0.2 * animal.getPopulation()));
            animal.decreasePopulation(amount);
        } else {
            if (animal.getTypeFood().equals("carnivore")) {
                Animal prey = getRandomPrey(animal, ecosystem.getAnimalList());
                if (prey != null) {
                    prey.decreasePopulation(amount);
                    int change = (int) ((amount * 0.15) + (prey.getPopulation() * 0.2));
                    animal.updatePopulation(change);
                    animal.setHungry(0);
                } else
                    animal.setHungry(animal.getHungry() + 10);
            } else {
                if (!ecosystem.getPlantList().isEmpty()) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(ecosystem.getPlantList().size());
                    Plant randomPlant = ecosystem.getPlantList().get(randomIndex);
                    amount = (int) Math.min(0.1 * animal.getPopulation(), 5);
                    randomPlant.decreasePopulation(amount);

                    int change = (int) Math.min(0.25 * animal.getPopulation(), 10);
                    animal.updatePopulation(change);
                } else {
                    animal.setHungry(animal.getHungry() + 10);
                }
            }
            drink(animal);
        }
    }

    private void checkWhoIsDeadAnimal(List<Animal> animalList) {
        List<Animal> deadAnimals = new ArrayList<>();

        for (Animal animal : animalList) {
            if (animal.getPopulation() == 0 || animal.getThirst() > 100 || animal.getHungry() > 100) {
                logger.logWarning("Animal extinct: " + animal.getName());
                deadAnimals.add(animal);
            }
        }

        for (Animal deadAnimal : deadAnimals) {
            ecosystem.deleteAnimal(deadAnimal);
        }
    }

    private void processPlantActions(Plant plant) {
        double survivalRate = predictionEcosystem.calculatePlantSurvivalRate(plant, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());

        if (survivalRate == 0.0)
            plant.setPopulation(0);
        else if (survivalRate < 1.0) {
            int amount = (int) Math.min(0.1 * plant.getPopulation(), 5);
            plant.decreasePopulation(amount);
        } else {
            if (ecosystem.getEnvironment().getWater() > 0 && ecosystem.getEnvironment().getWater() > plant.getPopulation()) {
                plant.setwaterNeed(0);
                int newWaterLevel = ecosystem.getEnvironment().getWater() - plant.getPopulation();
                ecosystem.getEnvironment().setWater(Math.max(newWaterLevel, 0));
                int regrowthAmount = (int) (0.1 * plant.getPopulation());
                plant.updatePopulation(regrowthAmount);
            } else {
                plant.setwaterNeed(plant.getwaterNeed() + 5);
            }
        }
    }

    private void checkWhoIsDeadPlant(List<Plant> plantList) {
        List<Plant> deadPlants = new ArrayList<>();

        for (Plant plant : plantList) {
            if (plant.getPopulation() == 0 || plant.getwaterNeed() > 80) {
                logger.logWarning("Plant extinct: " + plant.getName());
                deadPlants.add(plant);
            }
        }

        for (Plant deadPlant : deadPlants) {
            ecosystem.deletePlant(deadPlant);
        }
    }

    private Animal getRandomPrey(Animal predator, List<Animal> allAnimals) {
        List<Animal> preyList = allAnimals.stream()
                .filter(predator::canEat)
                .collect(Collectors.toList());

        if (!preyList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(preyList.size());
            return preyList.get(randomIndex);
        }

        return null;
    }

    private void logSurvivingEntities() {
        String survivingAnimals = ecosystem.getAnimalList().stream()
                .map(animal -> animal.getName() + " (population: " + animal.getPopulation() + ")")
                .collect(Collectors.joining(", "));
        String survivingPlants = ecosystem.getPlantList().stream()
                .map(plant -> plant.getName() + " (population: " + plant.getPopulation() + ")")
                .collect(Collectors.joining(", "));

        logger.logInfo("Surviving animals: " + survivingAnimals);
        logger.logInfo("Surviving plants: " + survivingPlants);
        logger.logInfo("Current Environment: Temperature - " + ecosystem.getEnvironment().getTemperature() +
                ", Humidity - " + ecosystem.getEnvironment().getHumidity() +
                ", Water - " + ecosystem.getEnvironment().getWater());
    }
    private void drink(Animal animal) {
        if (ecosystem.getEnvironment().getWater() > 0 && ecosystem.getEnvironment().getWater() > animal.getPopulation()) {
            animal.setThirst(0);
            int newWaterLevel = ecosystem.getEnvironment().getWater() - animal.getPopulation();
            ecosystem.getEnvironment().setWater(Math.max(newWaterLevel, 0));
        } else {
            animal.setThirst(animal.getThirst() + 5);
        }
    }
}
