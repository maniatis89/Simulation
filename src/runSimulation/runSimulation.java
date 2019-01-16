/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runSimulation;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author giannis
 */
public class runSimulation {

    /**
     */
     public runSimulation(){}
    
    public static void main(String[] args) {
        int numberOfPackets;
        int numberOfTimeSlots = 500;
        Simulation sim;
        PrintWriter results;
        //PrintWriter printResults;
        try
        {
           double[] inputProbs = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
           results =  new PrintWriter("C:\\Users\\giannis\\Desktop\\javaProgramms\\Simulation\\results.txt ");
           
           for(Double d:inputProbs)
           {
               
               System.out.println("Results for arrival probability: "+d);
               sim = new Simulation(d,500);
               results.println("throughPut: "+Simulation.getThroughput());
               results.println("Average Delay: "+Simulation.calcAverageDelay());
               System.out.println("The number of Collisions: "+Simulation.getCollisionsNum());
               System.out.println("percentage of successful transmission: "+Simulation.getPercentage());
               System.out.println("throughput: "+Simulation.getThroughput());
               System.out.println("Average Delay: "+Simulation.calcAverageDelay()+"\n");
           }
        }catch(IOException e)
        {
            System.out.println("can't create file");
        }
      
        
        
}
        
}
    

