package test;

import java.util.ArrayList;

public class Input {
    int value;
    int isCovered ;
    ArrayList<Integer> coveredBy = new ArrayList<>();

    /***
     * We use this class after determining the prime implicants
     * we make an arraylist of this object containing the minterms(user inputs)
     * we use it to determine the essential prime implicants and as well as help in petrik 's method
     */
    Input(int value)
    {
        this.value = value ;
        isCovered =  0;
    }

}
