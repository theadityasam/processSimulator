package simulator;

import algorithm.FCFS;
import algorithm.MyAlgorithm;
import algorithm.Priority1;
import algorithm.Priority2;
import algorithm.RR;
import algorithm.SJF;
import algorithm.SRTF;
import items.Job;
import items.Queue;

/**
 * @author Aditya Samantaray
 * This class is responsible for initiating, selecting the algorithm 
 * and doing the step work for the simulation
 */
public class Simulation {
 
    private static MyAlgorithm myAlgorithm; // object used for polymorphism 
    public static int Time;   // current time of the simulation
    public static String AlgorithmType = "FCFS";  // default algroithm type
    public static int Quantum = 2;  // quantum time for round robin algorithm
    public static boolean Finished = false; // show that the simulation is finished
    public static boolean Stopped = true;  // show that the simulation is stopped
    
    /**
     * reset the simulation
     */
    public static void reset()
    {
       Time = 0;  // reset the simulation time
       Finished = false;  // simulation is not finished
    }
    
    /**
     * @return the current ready queue of the working algorithm
     */
    public static Queue getReadyQueue()
    {
        return myAlgorithm.getReadyQueue();
    }
    
    /**
     * let the selected algorithm finish a step
     * @return the current job worked by the algorithm
     */
    public static Job workStep()
    {
        Job job;
        if(Time == 0) {selectAlgorithm();}  // select and init the algorithm
        job = myAlgorithm.nextStep(Time); 
        if(myAlgorithm.isFinished()){Finished = true;} 
        return job;
    }
    
    /**
     * select and initiate the selected algorithm
     */
    private static void selectAlgorithm()
    {
        switch (AlgorithmType) {
            case "FCFS":
                myAlgorithm = new FCFS(MainQueue.get());
                break;
            case "SJF":
                myAlgorithm = new SJF(MainQueue.get());
                break;
            case "Priority1":
                myAlgorithm = new Priority1(MainQueue.get());
                break;
            case "SRTF":
                myAlgorithm = new SRTF(MainQueue.get());
                break;
            case "Priority2":
                myAlgorithm = new Priority2(MainQueue.get());
                break;
            case "RR":
                myAlgorithm = new RR(MainQueue.get(), Quantum);
                break;
        }
    }
}
