/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runSimulation;

/**
 *
 * @author giannis
 */
public class Packet 
{
   private final int ID;
   private final String name;
   private final int arrivalTimeSlot;
   private int transmissionTimeSlot;
   
   public Packet(int ID,String name,int timeSlot)
   {
       this.ID = ID;
       this.name = name;
       this.arrivalTimeSlot = timeSlot;
       
   }
   public int getID()
   {
       return ID;
   }
   public String getName()
   {
       return name;
       
   }
   public int getTimeSlot()
   {
       return arrivalTimeSlot;
   }
   public void setTransmissionTimeSlot(int timeSlot)
   {
       this.transmissionTimeSlot = timeSlot;
   }
   public int getTransmissionTimeSlot()
   {
       return this.transmissionTimeSlot;
   }
   public int getDelay()//the difference between packet enqueuing timeslot and succesful transmision time slot
   {
       return this.transmissionTimeSlot - this.arrivalTimeSlot;
   }
}
