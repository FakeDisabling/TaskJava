package File;

import Ecosystem.Animal;
import Ecosystem.Plant;
import Ecosystem.Ecosystem;

import java.io.*;

public class FileManager {
    private static final String SIMULATION_FOLDER = "simulations";

    public static void saveEcosystemData(String simulationId, Ecosystem ecosystem) {
        String filePath = SIMULATION_FOLDER + "/" + simulationId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("[Environment]");
            writer.newLine();
            writer.write("Temperature=" + ecosystem.getEnvironment().getTemperature());
            writer.newLine();
            writer.write("Humidity=" + ecosystem.getEnvironment().getHumidity());
            writer.newLine();
            writer.write("Water=" + ecosystem.getEnvironment().getWater());
            writer.newLine();
            writer.write("Sunlight=" + ecosystem.getEnvironment().getSunlight());
            writer.newLine();
            writer.newLine();

            writer.write("[Animals]");
            writer.newLine();
            for (Animal animal : ecosystem.getAnimalList()) {
                writer.write(animal.getName() + "," + animal.getPopulation() + "," +
                        animal.getAge() + "," + animal.getTypeFood() + "," +
                        animal.getPower() + "," + animal.getHungry() + "," +
                        animal.getThirst() + "," +
                        animal.getMinTemperatureTolerance() + "," + animal.getMaxTemperatureTolerance());
                writer.newLine();
            }
            writer.newLine();

            writer.write("[Plants]");
            writer.newLine();
            for (Plant plant : ecosystem.getPlantList()) {
                writer.write(plant.getName() + "," + plant.getPopulation() + "," +
                        plant.getAge() + "," + plant.getwaterNeed() + "," +
                        plant.getlightNeed() + "," + plant.getMinTemperatureTolerance() + ","
                + plant.getMaxTemperatureTolerance());
                writer.newLine();
            }
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Ecosystem loadEcosystemData(String simulationId) {
        String filePath = SIMULATION_FOLDER + "/" + simulationId;
        Ecosystem ecosystem = new Ecosystem();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String section = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("[Environment]")) {
                    section = "environment";
                } else if (line.equals("[Animals]")) {
                    section = "animals";
                } else if (line.equals("[Plants]")) {
                    section = "plants";
                } else if (!line.isEmpty()) {
                    switch (section) {
                        case "environment":
                            String[] envParts = line.split("=");
                            if (envParts.length == 2) {
                                switch (envParts[0].trim()) {
                                    case "Temperature":
                                        ecosystem.getEnvironment().setTemperature(Double.parseDouble(envParts[1].trim()));
                                        break;
                                    case "Humidity":
                                        ecosystem.getEnvironment().setHumidity(Integer.parseInt(envParts[1].trim()));
                                        break;
                                    case "Water":
                                        ecosystem.getEnvironment().setWater(Integer.parseInt(envParts[1].trim()));
                                        break;
                                    case "Sunlight":
                                        ecosystem.getEnvironment().setSunlight(Integer.parseInt(envParts[1].trim()));
                                        break;
                                }
                            }
                            break;

                        case "animals":
                            String[] animalParts = line.split(",");
                            if (animalParts.length == 9) {
                                String name = animalParts[0].trim();
                                int population = Integer.parseInt(animalParts[1].trim());
                                int age = Integer.parseInt(animalParts[2].trim());
                                String typeFood = animalParts[3].trim();
                                int power = Integer.parseInt(animalParts[4].trim());
                                int hungry = Integer.parseInt(animalParts[5].trim());
                                int thirst = Integer.parseInt(animalParts[6].trim());
                                double mintemp = Double.parseDouble(animalParts[7].trim());
                                double maxtemp = Double.parseDouble(animalParts[8].trim());
                                Animal animal = new Animal(name, population, mintemp, maxtemp, age, typeFood, power, hungry, thirst);
                                ecosystem.addAnimal(animal);
                            }
                            break;

                        case "plants":
                            String[] plantParts = line.split(",");
                            if (plantParts.length == 7) {
                                String name = plantParts[0].trim();
                                int population = Integer.parseInt(plantParts[1].trim());
                                int age = Integer.parseInt(plantParts[2].trim());
                                int waterNeed = Integer.parseInt(plantParts[3].trim());
                                int lightNeed = Integer.parseInt(plantParts[4].trim());
                                double mintemp = Double.parseDouble(plantParts[5].trim());
                                double maxtemp = Double.parseDouble(plantParts[6].trim());
                                Plant plant = new Plant(name, population, mintemp, maxtemp, age, waterNeed, lightNeed);
                                ecosystem.addPlant(plant);
                            }
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ecosystem;
    }

    public static void logSimulationStep(String simulationId, String logMessage) {
        String logFilePath = SIMULATION_FOLDER + "/" + simulationId + "_log.txt";
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFilePath, true))) {
            logWriter.write(logMessage);
            logWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
