package com.ru.usty.scheduling;

import java.util.*;
import java.util.concurrent.Semaphore;

import com.ru.usty.scheduling.process.Process;
import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;
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
    long FCFSavgResponse;
    long RRavgResponse;
    long RRavgTurnaround;
    long FCFSavgTurnaround;
    int processCounter;
    LinkedList<Long> responseTimes;
    LinkedList<Long> turnaroundTimes;
    Semaphore mutex;

   

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
        mutex = new Semaphore(1);

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
            runTimeStarts = System.currentTimeMillis();
            responseTimes = new LinkedList<Long>();
            turnaroundTimes = new LinkedList<Long>();

			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			responseTimes = new LinkedList<Long>();
			turnaroundTimes = new LinkedList<Long>();
			processCounter = 0;
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
			                Thread.sleep(8);

			            } catch (InterruptedException e) {

			            }
			            try {
							mutex.acquire();
							if (!processQueue.isEmpty()) {
				            	ProcessData currProcess = processQueue.removeFirst();
				                processQueue.add(currProcess);
				                ProcessData process = processQueue.getFirst();
				                if (process.stopTime == 0) {
				                    process.setStopTime();
				                    responseTimes.add(process.stopTime);
				                }
				                processExecution.switchToProcess(process.processID);
				            }
							mutex.release();
						} catch (InterruptedException e) {
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
            responseTimes= new LinkedList<Long>();
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			comp2 = new SRTClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp2);
            responseTimes = new LinkedList<Long>();
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			comp = new HHRNClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp);
            responseTimes = new LinkedList<Long>();


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
                            procStartTime = System.currentTimeMillis();
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
			ProcessData process = new ProcessData(processID);
			process.setStartTime();

			if(processQueue.isEmpty() && currProcess == -1) {
			    process.setStopTime();
			    responseTimes.add(process.stopTime);
				processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
			    currProcess = processID;
			}
			processQueue.add(process);
			break;
		case RR:	//Round robin
			try {
				mutex.acquire();
				ProcessData rrprocess = new ProcessData(processID);
				rrprocess.setStartTime();
				
				if(currProcess == -1) {
				    rrprocess.setStopTime();
				    responseTimes.add(rrprocess.stopTime);
				    processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
				    currProcess = processID;
				    procStartTime = System.currentTimeMillis();
				}
				processQueue.add(rrprocess);
				mutex.release();
			} catch (InterruptedException e) {	}
			

			break;
		case SPN:	//Shortest process next
			System.out.println("processAddfall:: Shortest process next");
			ProcessData spnprocess = new ProcessData(processID);
			spnprocess.setStartTime();


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
		/**
		 * Add scheduling code here
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			ProcessData oldProcess = processQueue.removeFirst();
			oldProcess.setFinishTime();
			turnaroundTimes.add(oldProcess.finishTime);

			if (!processQueue.isEmpty()) {
			    ProcessData process = processQueue.getFirst();
			    if (process.stopTime == 0) {
			        process.setStopTime();
			        responseTimes.add(process.stopTime);
			    }
			    processExecution.switchToProcess(process.processID);
			}
			else {
			    currProcess = -1;
			    FCFSavgResponse = TimeCalculations.AverageTime(responseTimes);
			    FCFSavgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
			    System.out.println("FCFS average response time: " + FCFSavgResponse);
			    System.out.println("FCFS average turnaround time: " + FCFSavgTurnaround);
			}
			break;
		case RR:	//Round robin
			try {
				mutex.acquire();
				processCounter++;
				ProcessData oldRRProcess = processQueue.remove();
				oldRRProcess.setFinishTime();
				turnaroundTimes.add(oldRRProcess.finishTime);

				if (!processQueue.isEmpty()) {
					ProcessData process = processQueue.getFirst();
				    if (process.stopTime == 0) {
				        process.setStopTime();
				        responseTimes.add(process.stopTime);
				    }
				    processExecution.switchToProcess(process.processID);
				    procStartTime = System.currentTimeMillis();
				}
				else {
				    currProcess = -1;
				    if (processCounter == 15) {
					    RRavgResponse = TimeCalculations.AverageTime(responseTimes);
					    RRavgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
					    System.out.println("Round robin average response time:"+RRavgResponse);
					    System.out.println("Round robin average turnaround time:"+RRavgTurnaround);
				    }
				}
				mutex.release();
			} catch (InterruptedException e) {	}
			
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
