package com.ru.usty.scheduling;

import java.util.*;

import com.ru.usty.scheduling.process.Process;
import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;
import sun.awt.image.ImageWatched;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
    int quantum;
    private Thread newThread = null;
	LinkedList<ProcessData> processQueue;
	PriorityQueue<Integer> prioQueue;
	HHRNClass comp;
	SPNClass comp1;
	SRTClass comp2;
	long procStartTime;
	int currProcess;
    int newprocess;


    //Time measurements
    long runTimeStarts;
    long runTimeEnds;
    LinkedList<Long> times;




	/**
	 * Add any objects and variables here (if needed)
	 */


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, final int quantum) {

        currProcess = -1;

		this.policy = policy;
		this.quantum = quantum;

		processQueue = new LinkedList<ProcessData>();
        prioQueue = new PriorityQueue<Integer>();
        times = new LinkedList<Long>();

        //Feedback lists
        

		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
            runTimeStarts = System.currentTimeMillis();

			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
            newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(quantum);
                            long currTime = System.currentTimeMillis();
                            while((currTime-procStartTime) < quantum) {
                                Thread.sleep(quantum - (currTime-procStartTime));
                                currTime = System.currentTimeMillis();
                            }
                            System.out.println("Erum i done thraedi");

                        } catch (InterruptedException e) {

                        }
                        if (!processQueue.isEmpty()) {
                            int currProcess = processQueue.getFirst().processID;
                            processQueue.removeFirst();
                            processQueue.add(new ProcessData(currProcess));
                            processExecution.switchToProcess(processQueue.getFirst().processID);
                        }
                    }
                }
            });
            newThread.start();
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			comp1 = new SPNClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp1);
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			comp2 = new SRTClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp2);
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			comp = new HHRNClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp);


			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
            newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(quantum);
                            long currTime = System.currentTimeMillis();
                            while((currTime-procStartTime) < quantum) {
                                Thread.sleep(quantum - (currTime-procStartTime));
                                currTime = System.currentTimeMillis();
                            }
                            System.out.println("Erum i done thraedi");

                        } catch (InterruptedException e) {

                        }
                        if (!processQueue.isEmpty()) {
                            int currProcess = processQueue.getFirst().processID;
                            processQueue.removeFirst();
                            processQueue.add(new ProcessData(currProcess));
                            processExecution.switchToProcess(processQueue.getFirst().processID);
                        }
                    }
                }
            });
            newThread.start();
			break;
		}
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */

	public void processAdded(int processID) {
		//processExecution.switchToProcess(processID);
		//System.out.println(processExecution.getProcessInfo(processID).totalServiceTime);
		//Eigum ad halda afram ad fikta i thessu, vidbot:

		/**
		 * Add scheduling code here
		 */
		switch(policy) {
		case FCFS:	//First-come-first-served

			System.out.println("processAddfall: FCFS");
			ProcessData process = new ProcessData(processID);
			process.setStartTime();

			if(processQueue.isEmpty() && currProcess == -1) {
			    process.setStopTime();
			    times.add(process.stopTime);
				processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
                currProcess = processID;
			}
			else {
                processQueue.add(process);
            }


			break;
		case RR:	//Round robin
			System.out.println("processAddfall:: Round robin, quantum = " + quantum);


           /* if (!processQueue.isEmpty()) {
                if (newThread != null && newThread.isAlive()) {
                    newThread.interrupt();
                }

                if (processQueue.isEmpty()) {
                    processQueue.add(new ProcessData(processID));
                }
            }*/


            if(currProcess == -1) {
                processQueue.add(new ProcessData(processID));
                processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
                currProcess = processID;
                procStartTime = System.currentTimeMillis();
            }
            else {
                processQueue.add(new ProcessData(processID));
            }

			break;
		case SPN:	//Shortest process next
			System.out.println("processAddfall:: Shortest process next");


			if (currProcess == -1) {
			    processExecution.switchToProcess(processID);
			    currProcess = processID;
            }
            else {
			    prioQueue.add(processID);
            }


			break;
		case SRT:	//Shortest remaining time
			System.out.println("processAddfall:: Shortest remaining time");


            if (prioQueue.isEmpty() && currProcess == -1) {
                prioQueue.add(processID);
                processExecution.switchToProcess(processID);
                currProcess = processID;
            }
            else {
                prioQueue.add(processID);
                if (prioQueue.peek() != currProcess) {
                    processExecution.switchToProcess(prioQueue.peek());
                }
            }

			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processAddfall:: Highest response ratio next");

            if (currProcess == -1) {
                processExecution.switchToProcess(processID);
                currProcess = processID;
            }
            else {
                prioQueue.add(processID);
            }


			break;
		case FB:	//Feedback
			System.out.println("processAddfall:: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {

		System.out.println("FINISH!");
		/**
		 * Add scheduling code here
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("processFinishedFall: First-come-first-served");


            if (!processQueue.isEmpty()) {
                ProcessData process = processQueue.remove();
                if (process.stopTime == 0) {
                    process.setStopTime();
                    times.add(process.stopTime);
                    System.out.println(process.stopTime);
                }
                processExecution.switchToProcess(process.processID);
            }
            else {
                runTimeEnds = System.currentTimeMillis() - runTimeStarts;
                System.out.println(runTimeEnds);
                currProcess = -1;
            }

			break;
		case RR:	//Round robin
			System.out.println("processFinishedFall:: Round robin, quantum = " + quantum);

            if (!processQueue.isEmpty()) {
                currProcess = processQueue.remove().processID;
                processExecution.switchToProcess(currProcess);
                procStartTime = System.currentTimeMillis();
            }
            else {
                currProcess = -1;
            }

            break;
		case SPN:	//Shortest process next
			System.out.println("processFinishedFall:: Shortest process next");

			if (!prioQueue.isEmpty()) {
                newprocess = prioQueue.remove();
                processExecution.switchToProcess(newprocess);
            }
            else {
			    currProcess = -1;
            }



			break;
		case SRT:	//Shortest remaining time
			System.out.println("processFinishedFall:: Shortest remaining time");

            prioQueue.remove();
            if (!prioQueue.isEmpty()) {
                processExecution.switchToProcess(prioQueue.peek());
            }
            else {
                currProcess = -1;
            }

			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processFinishedFall:: Highest response ratio next");


            if (!prioQueue.isEmpty()) {
                newprocess = prioQueue.remove();
                processExecution.switchToProcess(newprocess);
            }
            else {
                currProcess = -1;
            }

			break;
		case FB:	//Feedback
			System.out.println("processFinishedFall:: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

	}

}
