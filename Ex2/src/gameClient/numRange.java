package gameClient;

public class numRange
{
    private int low;
    private int high;

    public numRange(int low, int high){
        this.low = low;
        this.high = high;
    }

    public boolean contains(int number){
        return (number >= low && number <= high);
    }
}