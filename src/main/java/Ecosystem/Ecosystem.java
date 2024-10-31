    package Ecosystem;

    import java.util.ArrayList;
    import java.util.List;

    public class Ecosystem {
        private List<Animal> animalList;
        private List<Plant> plantList;
        private Environment environment;
        public Ecosystem() {
            this.animalList = new ArrayList<>();
            this.plantList = new ArrayList<>();
            this.environment = new Environment();
        }
        public void addAnimal(Animal animal)
        {
            animalList.add(animal);
        }
        public void addPlant(Plant plant)
        {
            plantList.add(plant);
        }
        public void updateAnimal(Animal updatedAnimal) {
            for (Animal animal : animalList) {
                if (animal.getName().equals(updatedAnimal.getName())) {
                    animal.setPopulation(updatedAnimal.getPopulation());
                    animal.setAge(updatedAnimal.getAge());
                    animal.setTypeFood(updatedAnimal.getTypeFood());
                    animal.setPower(updatedAnimal.getPower());
                    animal.setHungry(updatedAnimal.getHungry());
                    return;
                }
            }
            System.out.println("Animal not found: " + updatedAnimal.getName());
        }

        public void updatePlant(Plant updatedPlant) {
            for (Plant plant : plantList) {
                if (plant.getName().equals(updatedPlant.getName())) {
                    plant.setPopulation(updatedPlant.getPopulation());
                    plant.setAge(updatedPlant.getAge());
                    plant.setwaterNeed(updatedPlant.getwaterNeed());
                    plant.setlightNeed(updatedPlant.getlightNeed());
                    return;
                }
            }
            System.out.println("Plant not found: " + updatedPlant.getName());
        }
        public void deleteAnimal(Animal animal)
        {
            animalList.remove(animal);
        }
        public void deletePlant(Plant plant)
        {
            plantList.remove(plant);
        }

        public List<Animal> getAnimalList() {
            return animalList;
        }

        public void setAnimalList(List<Animal> animalList) {
            this.animalList = animalList;
        }

        public List<Plant> getPlantList() {
            return plantList;
        }

        public void setPlantList(List<Plant> plantList) {
            this.plantList = plantList;
        }

        public Environment getEnvironment() {
            return environment;
        }

        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
