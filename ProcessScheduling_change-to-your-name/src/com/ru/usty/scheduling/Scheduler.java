package com.ru.usty.scheduling;

import java.util.*;
import java.util.concurrent.Semaphore;


import com.ru.usty.scheduling.process.ProcessExecution;
public class Scheduler {
	ProcessExecution processExecution;
	Policy policy;
    int quantum;
    private Thread newThread = null;
	LinkedList<ProcessData> processQueue;
	PriorityQueue<ProcessData> prioQueue;
	HHRNClass comp;
	SPNClass comp1;
	SRTClass comp2;
	long procStartTime;
	int currProcess;
    ProcessData newProcess;
    ProcessData oldProcess;
    ProcessData currentProcess;


    //Time measurements
    long runTimeStarts;
    long runTimeEnds;
    long avgResponse;
    long avgTurnaround;
    int processCounter;
    LinkedList<Long> responseTimes;
    LinkedList<Long> turnaroundTimes;
    Semaphore mutex;

    //Feedback Lists
    LinkedList<LinkedList> listOfLists;
    LinkedList<ProcessData> listOne;
    LinkedList<ProcessData> listTwo;
    LinkedList<ProcessData> listThree;
    LinkedList<ProcessData> listFour;
    LinkedList<ProcessData> listFive;
    LinkedList<ProcessData> listSix;
    LinkedList<ProcessData> listSeven;
    boolean listIsEmpty = false;
    int listPosition = 0;
    int indexOfCurrent;


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
        responseTimes = new LinkedList<Long>();
        turnaroundTimes = new LinkedList<Long>();
        oldProcess = null;
        newProcess = null;
        avgResponse = 0;
        avgTurnaround = 0;
        processCounter = 0;

		
        

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
            runTimeStarts = System.currentTimeMillis();
            processQueue = new LinkedList<ProcessData>();
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			processQueue = new LinkedList<ProcessData>();
			mutex = new Semaphore(1);
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
			prioQueue = new PriorityQueue<ProcessData>(10,comp1);
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			comp2 = new SRTClass(this);
			prioQueue = new PriorityQueue<ProcessData>(10,comp2);
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			comp = new HHRNClass(this);
			prioQueue = new PriorityQueue<ProcessData>(10,comp);
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			listOfLists = new LinkedList<LinkedList>();
			listOne = new LinkedList<ProcessData>();
            listTwo = new LinkedList<ProcessData>();
            listThree = new LinkedList<ProcessData>();
            listFour = new LinkedList<ProcessData>();
            listFive = new LinkedList<ProcessData>();
            listSix = new LinkedList<ProcessData>();
            listSeven = new LinkedList<ProcessData>();

            listOfLists.add(listOne);
            listOfLists.add(listTwo);
            listOfLists.add(listThree);
            listOfLists.add(listFour);
            listOfLists.add(listFive);
            listOfLists.add(listSix);
            listOfLists.add(listSeven);
            indexOfCurrent = 0;
            mutex = new Semaphore(1);
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

                        } catch (InterruptedException e) {

                        }
                        try {
                            mutex.acquire();
                            if (currProcess != -1) {
                            	indexOfCurrent++;
                            	if(indexOfCurrent == 7) {
                            		indexOfCurrent = 0;
                            	}
                            	listOfLists.get(indexOfCurrent).add(currentProcess);
                            	currentProcess = null;
                            	int newIndex = 0;
	                            while(currentProcess == null) {
                            		LinkedList<ProcessData> currentList = listOfLists.get(newIndex);
	                            	if(!currentList.isEmpty()) {
	                            		currentProcess = currentList.removeFirst();
	                            		indexOfCurrent = newIndex;
	                                    processExecution.switchToProcess(currentProcess.processID);
	                                    if (currentProcess.stopTime == 0) {
	                                        currentProcess.setStopTime();
	                                        responseTimes.add(currentProcess.stopTime);
                                        }
	                            	}
	                            	else {
	                            		newIndex++;
	                            	}
	                            }
                            }
                            mutex.release();
                        } catch (InterruptedException e) {

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
		switch(policy) {
		case FCFS:	//First-come-first-served
			newProcess = new ProcessData(processID);
			newProcess.setStartTime();

			if(processQueue.isEmpty() && currProcess == -1) {
				newProcess.setStopTime();
			    responseTimes.add(newProcess.stopTime);
				processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
			    currProcess = processID;
			}
			processQueue.add(newProcess);
			break;
		case RR:	//Round robin
			try {
				mutex.acquire();
				newProcess = new ProcessData(processID);
				newProcess.setStartTime();
				
				if(currProcess == -1) {
					newProcess.setStopTime();
				    responseTimes.add(newProcess.stopTime);
				    processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
				    currProcess = processID;
				    procStartTime = System.currentTimeMillis();
				}
				processQueue.add(newProcess);
				mutex.release();
			} catch (InterruptedException e) {	}
			

			break;
		case SPN:	//Shortest process next
			newProcess = new ProcessData(processID);
			newProcess.setStartTime();

			if (currProcess == -1) {
				newProcess.setStopTime();
				responseTimes.add(newProcess.stopTime);
			    processExecution.switchToProcess(processID);
			    currProcess = processID;
			    currentProcess = newProcess;
            }
			else {
				prioQueue.add(newProcess);
			}

			break;
		case SRT:	//Shortest remaining time
        	newProcess = new ProcessData(processID);
        	newProcess.setStartTime();

            if (prioQueue.isEmpty() && currProcess == -1) {
				newProcess.setStopTime();
				responseTimes.add(newProcess.stopTime);
                prioQueue.add(newProcess);
                processExecution.switchToProcess(processID);
                currProcess = processID;
                currentProcess = newProcess;
            }
            else {
                prioQueue.add(newProcess);
                if (prioQueue.peek() != currentProcess) {
                	if(newProcess.stopTime != 0) {
        				newProcess.setStopTime();
        				responseTimes.add(newProcess.stopTime);
                	}
                    processExecution.switchToProcess(prioQueue.peek().processID);
                }
            }

			break;
		case HRRN:	//Highest response ratio next
			newProcess = new ProcessData(processID);
        	newProcess.setStartTime();

            if (currProcess == -1) {
            	newProcess.setStopTime();
            	responseTimes.add(newProcess.stopTime);
                processExecution.switchToProcess(processID);
                currProcess = processID;
                currentProcess = newProcess;
            }
            else {
                prioQueue.add(newProcess);
            }

			break;
		case FB:	//Feedback
			System.out.println("processAddfall:: Feedback, quantum = " + quantum);
            try {
                mutex.acquire();
                newProcess = new ProcessData(processID);
                newProcess.setStartTime();
                if (currProcess == -1) {
                    if (newProcess.stopTime == 0) {
                        newProcess.setStopTime();
                        responseTimes.add(newProcess.stopTime);
                    }
                	currentProcess = newProcess;
                	indexOfCurrent = 0;
                    processExecution.switchToProcess(processID);
                    currProcess = processID;
                    procStartTime = System.currentTimeMillis();
                }
                else {
                    listOfLists.get(0).add(newProcess);
                }
                mutex.release();
            } catch (InterruptedException e) {

            }

            break;
		}

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		switch(policy) {
		case FCFS:	//First-come-first-served
			processCounter++;
			oldProcess = processQueue.removeFirst();
			oldProcess.setFinishTime();
			turnaroundTimes.add(oldProcess.finishTime);

			if (!processQueue.isEmpty()) {
			    newProcess = processQueue.getFirst();
			    if (newProcess.stopTime == 0) {
			    	newProcess.setStopTime();
			        responseTimes.add(newProcess.stopTime);
			    }
			    processExecution.switchToProcess(newProcess.processID);
			}
			else {
			    currProcess = -1;
			    if (processCounter == 15) {
				    avgResponse = TimeCalculations.AverageTime(responseTimes);
				    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
				    System.out.println("FCFS average response time:"+avgResponse);
				    System.out.println("FCFS average turnaround time:"+avgTurnaround);
			    }
			}
			break;
		case RR:	//Round robin
			try {
				mutex.acquire();
				processCounter++;
				oldProcess = processQueue.remove();
				oldProcess.setFinishTime();
				turnaroundTimes.add(oldProcess.finishTime);

				if (!processQueue.isEmpty()) {
					newProcess = processQueue.getFirst();
				    if (newProcess.stopTime == 0) {
				    	newProcess.setStopTime();
				        responseTimes.add(newProcess.stopTime);
				    }
				    processExecution.switchToProcess(newProcess.processID);
				    procStartTime = System.currentTimeMillis();
				}
				else {
				    currProcess = -1;
				    if (processCounter == 15) {
					    avgResponse = TimeCalculations.AverageTime(responseTimes);
					    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
					    System.out.println("Round robin average response time:"+avgResponse);
					    System.out.println("Round robin average turnaround time:"+avgTurnaround);
				    }
				}
				mutex.release();
			} catch (InterruptedException e) {	}
			
            break;
		case SPN:	//Shortest process next
			processCounter++;
			currentProcess.setFinishTime();
			turnaroundTimes.add(currentProcess.finishTime);

			if (!prioQueue.isEmpty()) {
				newProcess = prioQueue.remove();
				newProcess.setStopTime();
		        responseTimes.add(newProcess.stopTime);
		        currentProcess = newProcess;
                processExecution.switchToProcess(newProcess.processID);
            }
            else {
			    currProcess = -1;
			    if (processCounter == 15) {
				    avgResponse = TimeCalculations.AverageTime(responseTimes);
				    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
				    System.out.println("SPN average response time:"+avgResponse);
				    System.out.println("SPN average turnaround time:"+avgTurnaround);
			    }
            }
			break;
		case SRT:	//Shortest remaining time
			processCounter++;
			oldProcess = prioQueue.remove();
			oldProcess.setFinishTime();
			turnaroundTimes.add(oldProcess.finishTime);
			
            if (!prioQueue.isEmpty()) {
            	newProcess = prioQueue.peek();
            	if (newProcess.stopTime == 0) {
            		newProcess.setStopTime();
            		responseTimes.add(newProcess.stopTime);
            	}
                processExecution.switchToProcess(newProcess.processID);
            }
            else {
                currProcess = -1;
                if (processCounter == 15) {
				    avgResponse = TimeCalculations.AverageTime(responseTimes);
				    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
				    System.out.println("SRT average response time:"+avgResponse);
				    System.out.println("SRT average turnaround time:"+avgTurnaround);
			    }
            }

			break;
		case HRRN:	//Highest response ratio next
			processCounter++;
			currentProcess.setFinishTime();
			turnaroundTimes.add(currentProcess.finishTime);

            if (!prioQueue.isEmpty()) {
                newProcess = prioQueue.remove();
                newProcess.setStopTime();
                responseTimes.add(newProcess.stopTime);
                processExecution.switchToProcess(newProcess.processID);
                currentProcess = newProcess;
            }
            else {
                currProcess = -1;
                if (processCounter == 15) {
				    avgResponse = TimeCalculations.AverageTime(responseTimes);
				    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
				    System.out.println("HRRN average response time:"+avgResponse);
				    System.out.println("HRRN average turnaround time:"+avgTurnaround);
			    }
            }

			break;
		case FB:	//Feedback
			System.out.println("processFinishedFall:: Feedback, quantum = " + quantum);
            try {
                mutex.acquire();
                processCounter++;
                currentProcess.setFinishTime();
                turnaroundTimes.add(currentProcess.finishTime);
                
                if(processCounter == 15) {
                    avgResponse = TimeCalculations.AverageTime(responseTimes);
                    avgTurnaround = TimeCalculations.AverageTime(turnaroundTimes);
                    System.out.println("FB average response time:"+avgResponse);
                    System.out.println("FB average turnaround time:"+avgTurnaround);
                }
                mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

			break;
		}

	}

}
