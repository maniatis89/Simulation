/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runSimulation;

import java.util.ArrayList;

/**
 *
 * @author giannis
 */
public class Simulation 
{
    private final static double TRANSMISSION_SUCCESS_PROB = 0.5; 
    private final static int NUM_OF_STATIONS = 8;
    private static double arrivalProb;
    private static ArrayList<Station> stations;
    private static double timeSlots;
    private static ArrayList<Packet> transmittedPackets;
    private static ArrayList<Packet> enqueuedPackets;
    private static ArrayList<Packet> totalPackets;//either enqueued or not
    private static int numOfCollisions;
   
   /**
       This class run defines the algorithm of the simulation it does the following:
       1)Create packets
       2)Enqueue the packets according to a binomial distributed number generating function 
       with input argument the arrival probability
       3)Find Collisions 
       4)generate binomial distributed number with probability TRANSMISSION_SUCCESS_PROBABILITY = 0.5
     * @param inputProb
     * @param numOfTimeSlots
    */
   public Simulation(double inputProb,int numOfTimeSlots)
    {
        stations = new ArrayList<>();
        transmittedPackets = new ArrayList<>();
        enqueuedPackets = new ArrayList<>();
        totalPackets = new ArrayList<>();
        numOfCollisions = 0;
        Simulation.arrivalProb = inputProb;
        Simulation.timeSlots = numOfTimeSlots;
        createStations(arrivalProb); //define station probs through poisson distribution
        simulationAlgorithm();
    }
     /**
      * Creates two binomial distributed numbers (0 or 1)
      * one for p = 0.1 to 1.0 for arrival probability
      * and one for p = 0.5 the successful transmission probability 
      * If one station tries transmitting checks first if transmission try occurs from second
      * station at the same wavelength.If collision occurs the packets stay at each station queues for 
      * future transmitting
      * 
      */
    public final void simulationAlgorithm()
    { 
        for(Integer slot = 1; slot<=Simulation.timeSlots; slot++)
        {
              ArrayList<Integer> arrivals = calcArrivalBinomial();
              putPacketsToBuffers(arrivals,slot);//put packets accoriding to arrivals number
              ArrayList<Integer> succesfullTransmissionBinomial = Simulation.calcTransmissionBinomial();
              for(Integer i=0; i<Simulation.NUM_OF_STATIONS; i+=2)
              {
                  Station s1 = stations.get(i);
                  Station s2 = stations.get(i+1);
                  int val1 = succesfullTransmissionBinomial.get(i);//binomial generated 0 or 1 with p = 0.5
                  int val2 = succesfullTransmissionBinomial.get(i+1);
                  if(s1.transmit(val1) == 1)
                  {
                      if(s2.transmit(val2) == 1)
                      {
                         
                         numOfCollisions++;
                         //System.out.println("COLLISION");
                      }
                      else 
                      {
                         Simulation.transmittedPackets.add(s1.getBuffer().get(0));//put the first packet of queue in transmitted packets
                         s1.getBuffer().get(0).setTransmissionTimeSlot(slot);
                         s1.getBuffer().remove(0);// remove it from stations queue
                      }
                     
                   }
                  else if(s2.transmit(val2) == 1)
                  {
                      Simulation.transmittedPackets.add(s2.getBuffer().get(0));
                      s2.getBuffer().get(0).setTransmissionTimeSlot(slot);
                      s2.getBuffer().remove(0);
                  }
              }
             
        }
   }
    /**
     * CreateStations creates 4 pairs of stations and assigns 4 different wavelengths in each pair
     * @param arrivingProb 
     */           
    public final void createStations(double arrivingProb)
    {
        Integer m = 1;
        for(Integer i = 0; i<NUM_OF_STATIONS; i+=2)
        {
            Station station1 = new Station(i,"Station_"+i.toString(),"λ"+m.toString(),arrivingProb);
            Integer j = i+1;
           
            Station station2 = new Station(j,"Station_"+j.toString(),"λ"+m.toString(),arrivingProb);
            stations.add(station1);
            stations.add(station2);
            m++;
        }
    }
  /**
   * Creates a 0 or 1 with probability p
   * 1)generates random uniform distributed number 
   * 2)checks if p belongs to (0,p)
   * 3)if it belongs return 1 else returns 0;
   * @param p
   * @return 
   */
   public static int getBinomial( double p) //
   {
      int x = 0;
      if(Math.random() < p)
         x++;
      return x;
   }
    
 
  /**
   * generates binomial distributed number for every station
   * if number is 1 we have an new arrival else no packet arrived
   * @return 
   */  
  public static ArrayList<Integer> calcArrivalBinomial()
  {
      ArrayList<Integer> arrivalBinomial = new ArrayList<>();
      for(int i = 1; i<=8; i++)
      {
          arrivalBinomial.add(getBinomial(Simulation.arrivalProb));
      }
      return arrivalBinomial;
  }
 
 
  public static double calcTotalDelay()
  {
      double total = 0;
      for(Packet p:Simulation.transmittedPackets)
      {
         total += p.getDelay();
      }
      
      return total;
  }
  
  public   static double calcAverageDelay()
  {
      return calcTotalDelay()/Simulation.transmittedPackets.size();
  }
  
  public  static double getThroughput()
  {
      double t = Simulation.transmittedPackets.size()/Simulation.timeSlots;
      return t;
  }
  /**
   * put the arrived packets to buffer after consulting the generated array list created by
   * calcArrivalBinomial
   * @param arrivalBinomial
   * @param timeSlot 
   */
  public static void putPacketsToBuffers(ArrayList<Integer> arrivalBinomial,Integer timeSlot)
  {
      for(Integer i = 0; i<8; i++)
      {
          if(arrivalBinomial.get(i) == 1)
          {
              Packet packet = new Packet(i,"P"+timeSlot.toString()+" of "+stations.get(i).getName(),timeSlot);
              if(stations.get(i).getBuffer().size()<5)//checks if the buffer of every station is full
              {
                 //Packet packet = new Packet(i,"P"+timeSlot.toString()+" of "+stations.get(i).getName(),timeSlot);
                 enqueuedPackets.add(packet);
                 totalPackets.add(packet);
                 stations.get(i).getBuffer().add(packet);
              }
              else
              {
                totalPackets.add(packet);
              }
            
          }
      }
  }
  public static ArrayList<Integer> calcTransmissionBinomial()
  {
      ArrayList<Integer> transmissionBinomial = new ArrayList<>();
      for(int i = 1; i<=8; i++)
      {
          transmissionBinomial.add(getBinomial(Simulation.TRANSMISSION_SUCCESS_PROB));
      }
      return transmissionBinomial;
  }
  public static int getCollisionsNum()
  {
      return numOfCollisions;
  }
  public static double getPercentage()
  {
      return (double)Simulation.transmittedPackets.size()/Simulation.totalPackets.size();
  }
 
    
}
