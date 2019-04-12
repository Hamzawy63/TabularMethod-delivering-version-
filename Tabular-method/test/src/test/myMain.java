package test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 * if the user entered an invalid number it will be ignored
 * if the user entered the same value in minterms and don't care it will be dealed  with as don't care
 */

public class myMain {
    public static int numberOfVariables = 4; ///GLOBAL VARIABLE
    public static int[] complement = new int[numberOfVariables+2];
    
    public static boolean isValidPair(int a, int b)
    {
        int n = Math.abs(a-b);
        int temp = n & (n-1);
        if(a==b)
            return false;
        return temp == 0;

    }
    public static int  power(int n, int power) ///done
    {
        int result = 1;
        for(int i =0 ; i<power;i++ )
        {
            result *= n ;
        }
        return result;
    }
    public static boolean isValidNumber(int number,int numberOfVariables) ///done
    {
        return (number >= 0) && number < power(2, numberOfVariables);


    }
    public static void scanNumberOfVariables() ///done
    {
        System.out.println("Enter number of variables");
        Scanner sc = new Scanner(System.in);
         numberOfVariables = sc.nextInt();  ///numberOfVariables is a  global variable
        if(numberOfVariables <=0)
        {
            System.out.println("INVALID ENTRY");
            scanNumberOfVariables();
        }

    }
    public static void scanForMinterm(ArrayList<minterm> arr)
    {
        /**
         * scan the number                                              ****DONE***
         * if the number is -1 end scanning                             ****DONE***
         * else take the number and check that it is not out of range   ****DONE***
         * save the inputs in the arrayList                             ****DONE***
         * begin to scan for the do not care
         */
        System.out.println("Enter the minterms ");
        Scanner sc = new Scanner(System.in);
        int tmp; ///DO NOT DECLARE THE VRIABLE IN DO FUNCTION TO BE VISIBLE TO WHILE STATEMENT
        do {
             tmp= sc.nextInt();
            if(isValidNumber(tmp,numberOfVariables)==true)
            {
                arr.add(new minterm(tmp));
                ///IF THE USER ENTERED INVALID NUMBER IT WILL BE IGNORED
            }

        }while(tmp!=-1);





    }
    public static void scanForDoNotCare(ArrayList<minterm> arr)
    {
        /**
         * scan the number
         * if the number is -1 end scanning
         * else take the number and check that it is not out of range
         * save the inputs in the arrayList
         */
        System.out.println("Enter the don't care ");
        Scanner sc = new Scanner(System.in);
        int tmp; ///DO NOT DECLARE THE VRIABLE IN DO FUNCTION TO BE VISIBLE TO WHILE STATEMENT
        do {
            tmp= sc.nextInt();
            if(isValidNumber(tmp,numberOfVariables)==true)
            {
                arr.add(new minterm(tmp,1));
                ///IF THE USER ENTERED INVALID NUMBER IT WILL BE IGNORED
            }

        }while(tmp!=-1);
    }
    public static void makeProgramLoop(ArrayList<minterm> arr) ///the arraylist will be parameter
    {
        minterm temp1,temp2;
        int sizeLoopTurn = 0;
        int i,j,k,startOfLoop = 0;
        boolean isUserEnteredALlMinterms = true;
        boolean isComparisonOver = false;
        /*First of all we need to determine if the user entered all minterms*/
        


         /// WE MAKE THIS VARIABLE AS THE SIZE OF THE ARRAYLIST WILL INCREASE BY THE TIME IN THE NESTED LOOP BELOW SO WE DO NOT WANT TO HAVE AN INFINITE LOOP.
        ///loop to create fucking prime implicants
        for( k =0 ;k<numberOfVariables-1;k++,sizeLoopTurn++) {
            int isThisTheFirstSuccessfullComparison = 0;
            int  endOfLoopCounter = arr.size();
            int loopCounter = startOfLoop;  ///LOOP COUNTER CHANGES DURING THE LOOP SOO WE CAN NOT USE IT DIRECTLY
            for ( i = loopCounter; i < endOfLoopCounter; i++) {
                minterm tmp1 = arr.get(i);
                for ( j = loopCounter; j < endOfLoopCounter; j++) {
                    minterm tmp2 = arr.get(j);
                    /// IF THE ARE VALID GROUP THEM
                    if      (isValidPair(tmp1.value, tmp2.value)       &&
                            (tmp2.noOfOnes - tmp1.noOfOnes) == 1       &&
                            (tmp2.value > tmp1.value)                  &&
                            tmp1.paranthesis.equals(tmp2.paranthesis)  &&
                            tmp1.paranthesis.size() == sizeLoopTurn ) {
                        ///
                        isComparisonOver = false;
                        /// MARK THOSE TWO MINTERMS AS DEALED WITH TO BE DISTIGUISHED IN STAGE OF CHOOSING PRIME IMPLICANTS
                        tmp1.previouslyDealedWith = true;
                        tmp2.previouslyDealedWith = true;
                        ///MAKE THE NEW MINTERM
                        minterm tmp3 = new minterm(tmp1.value);
                        arr.add(tmp3);
                        tmp3.paranthesis.addAll(tmp1.paranthesis);
                        tmp3.paranthesis.add(tmp2.value - tmp1.value);
                        ///IF IT IS THE FIRST TIME SO WE SHOULD MARK THE INDEX OF THE ELEMENT THAT WILL BE CREATED FOR THE NXET LOOP (WHEN K+=1)
                        if(isThisTheFirstSuccessfullComparison ==0)
                        {
                            startOfLoop = arr.indexOf(tmp3);
                            isThisTheFirstSuccessfullComparison =1;

                        }

                    }
                }
            }

        }
        ///printing the array
        /**  REMOVED FOR PRESTIGE
        System.out.println("---array after determining prime implicants is (from program loop method) --");
        for (minterm l :arr) {
            System.out.print(l.value);
            System.out.println(l.paranthesis);
        }
        System.out.println("------------------------");
        **/
        /// DETERMINE ESSENTIALS
        /**
         *
         *  SEARCH FOR PRIME IMPLICANTS THAT COVERS ONLY ONE MINTERM
         *  IF FOUND REMOVE ALL MINTERMS COVERED BY THAT MINTERM
         *  IF THE ARRAY OF MINTERMS  IS NOT EMPTY YET MAKE THEN PETRIK METHOD
         **/
        //REMOVING DUBLICATING ELEMENTS AND AS WELL WE SORT FIRST THE PARANTHESIS ARRAY
        removeDublicates(arr);

        /* / NOW SINCE  WE HAVE OUR PRIME IMPLICANTS WE WILL DETERMINE THE MINTERMS COVERED BY EACH ONE */
        for ( i = 0; i < arr.size(); i++)
        {
            minterm tmp1 = arr.get(i);
            if(tmp1.previouslyDealedWith == false)
            {
                tmp1.coverFollowingMinterms =sumOfSubarray(tmp1.value,tmp1.paranthesis)  ;
            }
        }
        /* determining essentials*/
        removePullShit(arr); ///IN LAST STEP WE JUST WANT TO HAVE PRIME IMPLICANTS AND MINTNERMS AND THE REST IS REDUNDANT

    }
    public static ArrayList sumOfSubarray(int value,ArrayList<Integer> arr)
    {
        /**
         * IN STEP OF CHOOSING THE ESSENTIALS WE SHOULD KNOW MINTERMS COVERED BY EACH PRIME IMPLICANTS
         *  E.G 1(2,8) COVERS 1,3,9,11
         *  SO WE HAVE THIS FUNCTION TO ADD THE SUM OF  EACH SUB ARRAY TO THE VALUE AND APPEND IT TO AN ARRAY LIST
         *  TO SAVE IT LATER IN MINTERM.coverFollowingMinterms()
         */
        ArrayList<Integer > sumArray = new ArrayList<Integer>();
        int sum=0,n = arr.size();
        for(int i=0 ;i<(1<<n);i++)
        {
            sum = value;
            for(int j = 0 ;j<n;j++ )
            {
                if((i & (1 << j)) > 0)
                {
                    sum+=arr.get(j);
                }
            }
            sumArray.add(sum);

        }
        return  sumArray;
    }
    public static void removeDublicates(ArrayList<minterm> arr)
    {
    	
    	minterm garbage = new minterm(1<<numberOfVariables,0);
        arr.add(garbage); ///THIS IS INVALID INPUT WE PUT IT AT THE LAST TO BE A SIGN WHEN THE LOOP OF REMOVING DUBLICATES END
        
        /*
        THIS METHOD DO THE FOLLOWING
        1- SORT THE PARANTHESIS ARRAY LIST
        2 - REMOVING THE DUBLICATE PRIME IMPLICANTS
         */
        //FIRST SORT THE ARRAY

        for(int i=0 ;i<arr.size() ;i++)
        { ///NOTE THAT THE ALGORITHM SORTING IS NOT JUST FOR PRIME IMPLICANTS only IT IS FOR ALL MINTERMS
            minterm tmp = arr.get(i);
            Collections.sort(tmp.paranthesis);
        }
        /**
        System.out.println("----array after  Sorting from sorting method)------------------");
        for (minterm lol :arr)
        {
            System.out.print(lol.value);
            System.out.print(lol.paranthesis);
            System.out.print(lol.previouslyDealedWith);
            System.out.println(lol.paranthesis.size());
        }
        */
        /// check for the dublicates
        for(int i=0 ;i<arr.size() ;i++)  ///ARRAY SIZE CHANGES DURING LOOPING
        {
            minterm tmp1 = arr.get(i);
            if(tmp1.value ==(1<<numberOfVariables))
                break;
            for(int j=0 ;j<arr.size() ;j++)
            {
                minterm tmp2 = arr.get(j);
                if(tmp2.value ==1<<numberOfVariables)
                    break;

                if(tmp1.paranthesis.equals(tmp2.paranthesis) ==true &&(tmp1.value == tmp2.value)&&i!=j )
                {
                    arr.remove(j);
                   // System.out.print("NOW WE REMOVED "+arr.get(j).value+arr.get(j).paranthesis);
                   // System.out.println("it was removed by "+arr.get(i).value+arr.get(i).paranthesis);
                    j--;
                }



            }


        }
      /**  System.out.println("----array after  removing dublicates is (from function remove dublicate )------------------");
        for (minterm lol :arr)
        {
            System.out.print(lol.value);
            System.out.print(lol.paranthesis);
            System.out.print(lol.previouslyDealedWith);
            System.out.println(lol.paranthesis.size());
        }
        **/

    }
    public static void removePullShit(ArrayList<minterm> arr)
    {
        /**
         * WE JUST WANT FROM OUR ARRAYLIST THE FOLLOWING ELEMENTS
         * 1 - THE MINTERMS THAT ENTERED BY THE USER (AND NOT DON'T CARE)
         * 2 - THE PRIME IMPLICANTS         *
         */
        int n = arr.size()-1;
        for(int i = n ;i>=0 ;i-- )
        {
            minterm tmp = arr.get(i);
            if(tmp.previouslyDealedWith == true ||(tmp.doICare ==false))
            {
                if(tmp.paranthesis.size() !=0)
                arr.remove(tmp);
            }
        }
    }
    public static void mintermTable(ArrayList<minterm> arr,ArrayList<Input> input)
     {
        // PUT MINTERMS IN INPUT ARRAAYLIST
        int indexOfFirtPrimeImplicants = 0,k=0;
        int s = arr.get(k).paranthesis.size();

        for( k = 0 ;k!=arr.size() && arr.get(k).paranthesis.size()==0 ;k++ )
        {
            if(arr.get(k).doICare == true)
                input.add(new Input(arr.get(k).value));
            ///aA BUG FIX LINE
            /**
            if(arr.get(k).previouslyDealedWith==false)
            {
                arr.add(arr.get(k));
            }
             **/
        }
        indexOfFirtPrimeImplicants = k;

        //CHECK HOW MANY TIMES EACH MINTERM IS COVERED
        for(int i = 0 ;i<input.size() ;i++ )
        {
            Input tmp1 = input.get(i);
            for(int j = 0 ;j<arr.size();j++)
            {
                minterm tmp2 = arr.get(j);
               // if(tmp2.coverFollowingMinterms.customContains(tmp1.value));
                if((customContains(tmp2.coverFollowingMinterms,tmp1.value))&&tmp2.previouslyDealedWith==false)
                {

                    tmp1.isCovered++;
                    tmp1.coveredBy.add(j); ///INDEX OF THIS PI
                }
            }
            //determineEssential(arr,input);
        }
        //determineEssential(arr,input);



    }
    public static void determineEssential(ArrayList<minterm> arr,ArrayList<Input> input)
    {
        ///NOW  DETERMINE THE ESSENTIAL
        /**
         * 1 - DETERMINE EACH MINTERMS COVERED BY ONE PI.
         * 2 - REMOVE FROM INPUT ARR THE REST OF MINTERMS COVERED BY THAT ESSENTIAL PRIME IMPLICANTS
         * 3 - IF INPUT ARRAY BECOMES EMPTY THEN THE PROGRAM IS OVER
         * 4 - IF NOT THEN WE SHOULD SEARCH AGAIN FOR ESSENTIAL OTHERWISE WE GONNA USE PETRIK'S METHOD
         */
        boolean essetial = false;
        ArrayList<Integer> tempList = new ArrayList<>(); /*holds value  of essential pi */
        if(input.size() == 0 )
        {
            return;
        }
        for(int i = 0;i<input.size();i++)
        {
            Input tmp1 = input.get(i);
            minterm tmp2;
            if(tmp1.isCovered==1)
            {
                essetial = true;
                int n = tmp1.coveredBy.size() ;
                // int m = arr.get(tmp1.coveredBy.get(0));
                int l = tmp1.coveredBy.get(0);
                ///SO WE FOUND ESSENTIAL
                tmp2  = arr.get(tmp1.coveredBy.get(0)); ///no more than one element in the array
                tmp2.essential =true;
                tempList.add(tmp1.coveredBy.get(0));
                //edit complement array


            }
        }



        /**
         * first we loop in the "covered the following minterms " in essentials PI
         * then we store in elements in a temp array list
         * finally we loop in the input array list and romove those whose value are in the temp list
         */
         ArrayList<Integer> coveredMinterm = new ArrayList();
         for(minterm tmp1 : arr)
         {
             if(tmp1.essential == true)
             {
                 coveredMinterm.addAll(tmp1.coverFollowingMinterms);
             }
         }

         for(int i =0 ;i<input.size() ;i++ )
         {
             Input j = input.get(i);
             if(customContains(coveredMinterm,j.value))
             {
                 input.remove(i--);
             }
         }



     //   System.out.println("------------hello from covered minterms array when there exists essential");
      //   System.out.println(coveredMinterm);
       // System.out.println("---------");



        /*here we should determine if essential = false then we use petrik method */

    }
    public static ArrayList remainingPI(ArrayList<minterm> arr,ArrayList<Input> input)
    {
        ArrayList<minterm> remainingPI = new ArrayList();
        ArrayList<Integer> index = new ArrayList();

        for(Input i : input)
        {
            index.addAll(i.coveredBy);
        }


        /*NOW WE SHOULD REMOVE DUBLICATES*/
        Collections.sort(index);
        for(int i = 0 ;i<index.size()-1 ;i++ )
        {
            int tmp = index.get(i);
            if(tmp ==  index.get(i+1))
            {
                index.remove(i--);
            }
        }
        /*NOW WE SHOULD ADD THESE OBJECTS TO THE ARRAYLLIST  */
        for(int j = 0 ;j<index.size() ;j++ )
        {
            remainingPI.add(arr.get(index.get(j)));
        }
        return remainingPI;
    }
    public static  void makePetrik(ArrayList<minterm> arr,ArrayList<Input> input)
    {
        if(arr.size() ==0)
            return;
        /*arr here is a new list of  object have just the PI that covers remaining essentials  */
        /// first of all we should have an array of minterms
        ArrayList<Integer> remainingMinterms = new ArrayList();
        for(Input i :input)
        {
            remainingMinterms.add(i.value);
        }

        // 1 - FIRST WE CALC THE COST TO EVERY PRIME IMPLICANT AND MAKE CHARACTER REPRESENTATION AND REMOVE UNNECESSARY ELEMENTS FROM "COVER THE FOLLOWING MINTERMS  "ARRAY

           for(int i= 0 ;i<arr.size() ;i++)
           {
               minterm tmp1 = arr.get(i);
               charactarRepresentation(tmp1);
               for(int j =0 ;j<tmp1.coverFollowingMinterms.size() ;j++)
               {
                   int tmp2 = tmp1.coverFollowingMinterms.get(j);
                   if(!(customContains(remainingMinterms,tmp2)))
                   {
                       tmp1.coverFollowingMinterms.remove(j--);
                   }
               }
           }

        /// 2 - ADD SEQUENCES WHICH COVER ALL REMAINING MINTERMS TO THE ARRAYLIST

        int n = arr.size();
        for(int i = 0 ; i<(1<<n) ;i++) ///number of sequences
        {
            minterm newPI = new minterm(1<<numberOfVariables); ///invalid
            for(int j = 0 ;j<n ;j++)
            {
                if((i & (1 << j)) > 0)
                {
                    ///check if they cover covers all minterms
                    newPI.representation.addAll(arr.get(j).representation);
                    newPI.representation.add('+');
                    newPI.coverFollowingMinterms.addAll(arr.get(j).coverFollowingMinterms);

                }

            }
            arr.add(newPI);
        }
        /// 3 - loop to the arraylist to mark Expressions that cover all minterms
        int minCost=100;
        for(int i = 0 ;i<arr.size() ;i++)
        {
            minterm tmp  = arr.get(i);
            if(coverTheMinterms(tmp,input)&&tmp.value ==1<<(numberOfVariables))
            {
                tmp. essential = true;
                /// 4 - determine the cost of Each EXPRESSIONS
                calcCostFromExpression(tmp);
                /// 5 - choose the minimum cost expressions
                if(tmp.cost<minCost)
                {
                    minCost = tmp.cost;
                }
            }else
            {
               arr.remove(i--);
            }
        }
        
        for(int i = 0 ;i<arr.size() ;i++)
        {
            minterm tmp  = arr.get(i);
            if(!(tmp.cost ==minCost))
            {
                arr.remove(i--);
            }
            //if(tmp.value ==1<<(numberOfVariables))
            //{
            //	arr.remove(i);
           // }
        }




    }
    public  static boolean coverTheMinterms(minterm PI,ArrayList<Input> input)
    {
        for(Input j : input )
        {
            if(!(customContains(PI.coverFollowingMinterms,j.value)))
                return false;
        }
        return true;

    }
    public static void calcCostFromExpression(minterm essential )
    {
        int cost = 1 ;
        for(int i =0;i<essential.representation.size()-1;i++ )
        {
            char tmp = essential.representation.get(i);
            if(tmp == '+')
                continue;
            else if(tmp == '\'')
            {
               /// System.out.println("A7am "+ (i-1)+"  Length is "+essential.representation.size());
               // System.out.print(essential.value);
               // System.out.print(essential.paranthesis);
              //  System.out.println(essential.representation);

                char tmp2 = essential.representation.get(i-1);
                 // System.out.println("hey bro "+(tmp2-65));
               //   System.out.println(complement.length);
                if(complement[(int)(tmp2-65)] == 1)
                    cost-=1;
            }
            cost++;
        }
        essential.cost = cost;
    }
    public static void makeRowDominant(ArrayList<minterm> arr,ArrayList<Input> input)
    {
        /*REMOVE UNNECESSARY MINTERMS FROM PARANTHESIS */
        // 1 - WE SHOULD SAVE INDEXES OF PI THAT COVERS REMAINING MINTERMS FROM INPUT ARRAY
        ArrayList<Integer> indexes = new ArrayList<>(); ///indexes of pi covering remaining minteerms
        ArrayList<Integer> remainingMinterms = new ArrayList<>();
        for(Input i : input)
        {
            indexes.addAll(i.coveredBy);
            remainingMinterms.add(i.value);
        }
        // 2- WE ACCESS MINTERMS ARRAY WHICH WE HAVE THEIR INDEX
        for(int i :indexes)
        {
            minterm tmp = arr.get(i);
            for(int j = 0 ;j<tmp.paranthesis.size() ;j++ )
            {
                if (!(customContains(remainingMinterms,tmp.paranthesis.get(i))))
                    tmp.paranthesis.remove(i--);
            }
        }
    }
    public static  void finalAnswer(ArrayList<minterm> arr,ArrayList<minterm> petrik)
    {

        System.out.println("-----------------\nFinal answer is (in sop form)");
        boolean start = true; /// IS FOUND SO THAT NO '+' SIGN IS PRINTED AT THE FIRST
        for(minterm tmp :arr)
        {
            if(tmp.essential == true)
            {
           // charactarRepresentation(tmp)
        	if(!start)
            	 System.out.print("+");
            for(int i = 0 ;i<tmp.representation.size() ;i++)
            {
            	start = false;
                System.out.print(tmp.representation.get(i));
            }
           
            }
        }
        if(petrik.size() != 0)
        {
        	System.out.print('+');
        }

        for(minterm tmp :petrik)
        {
            for(int i = 0 ;i<tmp.representation.size()-1 ;i++) /// -1 to remove last "+"  sign
            {
                System.out.print(tmp.representation.get(i));

            }
            break;
        }


    }
    public static void  charactarRepresentation(minterm essential )
    {
        ArrayList<Integer> representaion = new ArrayList();
        for(int i =0 ;i<numberOfVariables ;i++ )
        {
            representaion.add(1<<(numberOfVariables-1-i));
        }
        int tmp = essential.value;
        for( int k =0 ;k<representaion.size() ;k++)
        {
            int j = representaion.get(k);

            if(!(customContains(essential.paranthesis,j)))
            {
            if (j<=tmp)
                {
                    tmp = tmp-j;
                    representaion.set(k,1);
                }
            else
                {
                    representaion.set(k,0);
                }
            }else
            {
                representaion.set(k,-1);
            }

        }
        //System.out.println("");
        for(int i = 0 ;i<representaion.size();i++)
        {
            int temp = representaion.get(i);
            if(temp ==1)
            {
                essential.representation.add((char) (65 + i));
          //      System.out.print((char) (65 + i));
                essential.cost++;
            }else if(temp==0)
            {
                essential.representation.add((char) (65 + i));
                essential.representation.add('\'');
                //System.out.print((char)(65+i));
                //System.out.print('\'');
                essential.cost+=2;
            }
        }
      //  System.out.println("");
       // System.out.println("cost = " + essential.cost);
     //   System.out.println(representaion);

    }
    public static boolean customContains(ArrayList<Integer> arr,int a)
    {
        for(int j : arr)
        {
            if((j)==a)
                return true;

        }
        return false;
    }

    public static void main(String[] args)
    {


        minterm temp;
        ArrayList<minterm> arr = new ArrayList<minterm>(); // Create an ArrayList object
        ArrayList<minterm> petrik = new ArrayList<minterm>(); // Create an ArrayList object
        ArrayList<Input> input = new ArrayList<Input>(); // Create an ArrayList object
        scanNumberOfVariables();
        scanForMinterm(arr);
        scanForDoNotCare(arr);
        removeDublicates(arr);  
       
        if(arr.size() ==((1<<numberOfVariables)+1))
        {
        	System.out.println("Your function is always one " );
        	return;
        }
        // determine if there is no minterms 
        boolean minterm = false;
        for(minterm tmp : arr)
        {
        	if(tmp.doICare == true)
        		minterm = true;
        }
        if(minterm == false)
        {
        	System.out.println("Your function in minimum form is zero " );
        	return;
        }
        makeProgramLoop(arr);

        ///printing the prime implicants
        System.out.println("----prime implicants are (from main)--------------------");
        for (minterm i :arr)
        {
        	
            if(i.previouslyDealedWith ==false &&i.value!=1<<(numberOfVariables)) {
                System.out.print(i.value);
                System.out.print(i.paranthesis+"----->");
                minterm tmp = new minterm(0);//new minterm(i.value);
                tmp.value = i.value;
                tmp.paranthesis = i.paranthesis;
                charactarRepresentation(tmp);
                for(char x : tmp.representation)
                	System.out.print(x);
                System.out.print("\n");
                
            }
        }
        ///minterms covered
     
        removePullShit(arr); //--------------------------------------
       
        mintermTable(arr,input);  /// IT CALLS ESSENTIALS BY THE WAY
       
        determineEssential(arr,input);
        System.out.println("-------------------Essentials are--------- ");
        for (minterm i :arr)
        {
            if(i.essential == true) {
                System.out.print(i.value);
                System.out.print(i.paranthesis);
                System.out.print("---------->");

                charactarRepresentation(i);
                for(char x : i.representation)
                	System.out.print(x);
                System.out.println("\n");


            }
        }
        System.out.println("-------------Remained Minterms are-----------");
        for(Input i:input)
        {
            System.out.print(i.value+ " " );
        }
        System.out.println("\n");
        if(input.size()==0)
        System.out.println("No Remained Minterms available\n\n");
        petrik = remainingPI(arr,input);
        makePetrik(petrik,input);


        System.out.println("-------------PETRIK TRY--------");
        
        for(minterm i :petrik)
        {
        	for(int j = 0 ;j<i.representation.size() ;j++)
        		{ 
        			char x =i.representation.get(j);
        			if(j!= i.representation.size()-1)
        				System.out.print(x);
        		}
        	System.out.print("\n");
        }
        if(petrik.size()==0)
        	 System.out.println("No petrik available for this function\n\n----------------------------");
        	
        finalAnswer(arr,petrik);





    }


}
