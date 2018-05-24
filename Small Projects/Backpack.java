
public class Backpack 
{
/*
 *Optimize takes 3 parameters: 2 arrays, one for the veights of the objects and the other for the values of the objects 
 * and one integer for the weight capacity of the bag
 */
  public static void Optimize(int[] weights, int[] values, int capacity)
    {
    //Building the value table
        int[][] valueTable = new int[weights.length][capacity+1];
        //Because the previous item is checked in the main loop, the first item needs to be done now
        for(int currentCap = 1; currentCap < valueTable[0].length; currentCap++)
        {
          if(weights[0] <= currentCap)
          {
            valueTable[0][currentCap] = values[0];
          }
        }
        //each item in the value table 
        for (int item = 1; item < valueTable.length; item++) 
        {
          //for each capacity value in value table 
            for (int currentCap = 1; currentCap < valueTable[item].length; currentCap++) 
            {
                if(weights[item] > currentCap)
                {
                    valueTable[item][currentCap] = valueTable[item-1][currentCap];
                }
                else
                {
                  if( values[item] + valueTable[item-1][currentCap-weights[item]] > valueTable[item-1][currentCap] )
                  {
                     valueTable[item][currentCap] =  values[item] + valueTable[item-1][currentCap-weights[item]];
                  }
                  else
                  {
                     valueTable[item][currentCap] = valueTable[item-1][currentCap];
                  }
                }
            }
        }
        //traverse the table to find optimal solution
        int availableWeight = capacity;
        int[] itemSet= new int[capacity+1];
        int setI = 0;
        int maxValue = valueTable[weights.length-1][capacity];
        int cItem = weights.length-1;
        
        while (availableWeight > 0) 
        {
          if(cItem <= 0)
          {
            if (valueTable[cItem][availableWeight] > 0)
            {
              itemSet[setI] = cItem;
              setI++;
              availableWeight -= weights[cItem];
              cItem --;
            }
            else
            {
              break;
            }
          }
          else
          {
            if(valueTable[cItem][availableWeight] ==  valueTable[cItem-1][availableWeight])
            {
              cItem --;
            }
            else
            {
              itemSet[setI] = cItem;
              setI++;
              availableWeight -= weights[cItem];
              cItem --;
            } 
          }
        }
        //print out value table 
        for (int i = 0; i < valueTable.length; i++) 
        {
            for (int j = 0; j < valueTable[i].length; j++) 
            {
                System.out.print("[" + valueTable[i][j] + "} ");
            }
            System.out.print("\n");
        }
        //print out solution
        System.out.println("The maximum value that can be carried is " + maxValue);
        System.out.println("Item items in the optimal set(where lighter items are favored over heavy) is :");
      for (int i = 0;i<=setI-1;i++)
      {
        System.out.print("[" + weights[itemSet[i]] + "(" + values[itemSet[i]] + ")" + "] ");
      }
        System.out.print("\n");
        
    }

    public static void main(String[] Args)
    {
        int[] weights =  {1,2,2,3,4,4,5,6,7};
        int[] values = {3,5,10,11,14,20,25,31,33};
        Backpack.Optimize(weights,values,10);
    }

}
