package test;
import java.util.ArrayList;

public class minterm {
    int value ;
    ArrayList<Integer > paranthesis = new ArrayList<Integer>(); // Create an ArrayList object
    ArrayList<Integer > coverFollowingMinterms = new ArrayList<Integer>();
    ArrayList<Character > representation = new ArrayList<Character>();

    boolean doICare = true  ;
    boolean previouslyDealedWith = false;
    int noOfOnes = 0;
    Boolean essential = false;
    int cost = 1 ;

    minterm(int value) //SIMPLE FUCKING CONSTRUCTOR
    {
        this.value = value ;
        noOfOnes = countSetBits(value);
    }
    minterm(int value,int tmp )
    {
        this.value = value ;
        noOfOnes = countSetBits(value);
        this.doICare = false;
    }
    public static int countSetBits(int n)
    {
        int count = 0;
        while (n > 0)
        {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }


}
