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
public class Station 
{
    //private final  double TRANSMISSION_SUCCESS_PROB = 0.5;
    private final String name;
    private final int ID;
    private final ArrayList<Packet> buffer;
    private final String waveLength;
    private boolean tryingToTransmit;
    
    public Station(int ID,String name,String waveLength,double arrivingProb) 
    {
       this.ID = ID;
       this.name = name;
       this.buffer = new ArrayList<>();
       this.waveLength = waveLength;
       this.tryingToTransmit = false;
    }
    public String getName()
    {
        return name;
    }
    public int getID()
    {
        return ID;
    }
   public ArrayList<Packet> getBuffer()
    {
       return this.buffer;
    }
    public int transmit(int value)
    {
        if((!(this.buffer.isEmpty())) && (value == 1))//if the buffer is empty we have nothing to transmit
        {
           this.tryingToTransmit = true;
           return 1;
          
        }
        return 0;
    }
    
    public void addToBuffer(Packet p)
    {
        this.buffer.add(p);
    }
    public String getWaveLength()
    {
        return waveLength;
    }
    
   /*public boolean transmitting()
   {
       return this.tryingToTransmit;
   }*/
    

}
