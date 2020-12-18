package gameClient;

/**
 * This class represents a number range from low to high.
 */
public class numRange
{
    private int low;
    private int high;

    /**
     * Basic constructor that gets the minimum range value and the maximum range value.
     * @param low
     * @param high
     */
    public numRange(int low, int high){
        this.low = low;
        this.high = high;
    }

    /**
     * @param number
     * @return true iff the number is in this range.
     */
    public boolean contains(int number){
        return (number >= low && number <= high);
    }
}