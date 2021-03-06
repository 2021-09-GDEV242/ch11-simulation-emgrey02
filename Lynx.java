
import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a lynx.
 * Lynxes age, move, eat rabbits, and die.
 * 
 * Lynxes compete with Foxes to prey on Rabbits. This will reduce the
 * populations of Lynxes and Foxes from what they could have been as
 * a lone predator. This competition benefits the Rabbit population, and
 * jeopardizes the survival of the lynx or fox population
 * 
 * @author Emma Grey
 * @version 2021.11.24
 */
public class Lynx extends Animal
{
    // Characteristics shared by all lynx (class variables).
    
    // The age at which a lynx can start to breed.
    private static final int BREEDING_AGE = 14;
    // The age to which a lynx can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a lynx breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a lynx can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 6;
    
    // Individual characteristics (instance fields).
    
    // The lynx's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a lynx. A lynx can be created as a new born (age zero
     * and not hungry) or with a random age.
     * 
     * @param randomAge If true, the lynx will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lynx(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        foodLevel = RABBIT_FOOD_VALUE;
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Animal> newLynxes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newLynxes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Get the age of the lynx.
     * @return the age of the lynx
     */
    public int getAge() {
        return super.getAge();
    }
    
    /**
     * Set the current age of the lynx.
     */
    public void setAge(int currentAge) {
        super.setAge(currentAge);
    }
    
    /**
     * Get the max age of the lynx
     * @return the max age of the lynx
     */
    public int getMaxAge() {
        return MAX_AGE;
    }
    
    /**
     * Make this lynx more hungry. This could result in the lynx's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this lynx is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLynxes A list to return newly born foxes.
     */
    private void giveBirth(List<Animal> newLynxes)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = super.breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lynx young = new Lynx(false, field, loc);
            newLynxes.add(young);
        }
    }
    
    /**
     * Get the breeding probability of the lynx.
     * @return the breeding probability of the lynx
     */
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Get the max litter size of the lynx.
     * @return the max litter size of the lynx
     */
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * @return The age at which the lynx starts to breed.
     */
    public int getBreedingAge() {
        return BREEDING_AGE;
    }
}