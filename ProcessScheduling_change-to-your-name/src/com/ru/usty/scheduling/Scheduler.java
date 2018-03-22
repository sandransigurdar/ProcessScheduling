package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;
import sun.awt.image.ImageWatched;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
    int quantum;
    private Thread newThread = null;
	LinkedList<ProcessData> processQueue;

	/**
	 * Add any objects and variables here (if needed)
	 */


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

		/**
		 * Add general initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, final int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		
		processQueue = new LinkedList<ProcessData>();

		//processQueue.remove(5); //Remove-a staki� 5
		
		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
            newThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(quantum);


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
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

		/**
		 * Add general scheduling or initialization code here (if needed)
		 */

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processAdded(final int processID) {
		//Eigum ad halda afram ad fikta i thessu, vidbot:
		/**
		 * Add scheduling code here
		 */
		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("processAddfall: First-come-first-served");
			if (processQueue.isEmpty()) {
			    processExecution.switchToProcess(processID);
            }
            processQueue.add(new ProcessData(processID));
			
			break;
		case RR:	//Round robin
			System.out.println("processAddfall:: Round robin, quantum = " + quantum);
			/*if (newThread != null && newThread.isAlive()) {
			    newThread.interrupt();
            }*/
            /*if (processQueue.isEmpty()) {
                processExecution.switchToProcess(processID);
            }*/
            processQueue.add(new ProcessData(processID));








			break;
		case SPN:	//Shortest process next
			System.out.println("processAddfall:: Shortest process next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("processAddfall:: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processAddfall:: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
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


			processQueue.removeFirst();
			if (!processQueue.isEmpty()) {
			    processExecution.switchToProcess(processQueue.getFirst().processID);
            }

			break;
		case RR:	//Round robin
			System.out.println("processFinishedFall:: Round robin, quantum = " + quantum);

			if (!processQueue.isEmpty()) {
                processQueue.removeFirst();
            }

			break;
		case SPN:	//Shortest process next
			System.out.println("processFinishedFall:: Shortest process next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("processFinishedFall:: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processFinishedFall:: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("processFinishedFall:: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

	}
	
	//Gaetum turft ad gera nytt interruption fall tegar vid erum komin med timer
}
