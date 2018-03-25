package com.ru.usty.scheduling;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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
	long procStartTime;
	int currProcess;
    ProcessInfo info;
    int tala;



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
			//comp = new ShortestProcessNext();
			prioQueue = new PriorityQueue<Integer>(10,comp);
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			comp = new HHRNClass(this);
			prioQueue = new PriorityQueue<Integer>(10,comp);


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
			/**
			 * Add your policy specific add code here (if needed)
			 */

			if(processQueue.isEmpty() && currProcess == -1) {
				processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
                currProcess = processID;
			}
			else {
                processQueue.add(new ProcessData(processID));
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


			if (prioQueue.isEmpty()) {
			    processExecution.switchToProcess(processID);
            }
            while(prioQueue.size() != 0) {
			    prioQueue.add(processID);
            }


			break;
		case SRT:	//Shortest remaining time
			System.out.println("processAddfall:: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processAddfall:: Highest response ratio next");



            if (prioQueue.isEmpty() && currProcess == -1) {
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


			/*processQueue.removeFirst();
			if (!processQueue.isEmpty()) {
			    processExecution.switchToProcess(processQueue.getFirst().processID);

            }*/
            if (!processQueue.isEmpty()) {
                processExecution.switchToProcess(processQueue.remove().processID);
            }
            else {
                currProcess = -1;
            }

			break;
		case RR:	//Round robin
			System.out.println("processFinishedFall:: Round robin, quantum = " + quantum);

			/*if (!processQueue.isEmpty()) {
                processQueue.removeFirst();
            }*/

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




			break;
		case SRT:	//Shortest remaining time
			System.out.println("processFinishedFall:: Shortest remaining time");



			break;
		case HRRN:	//Highest response ratio next
			System.out.println("processFinishedFall:: Highest response ratio next");


            if (!processQueue.isEmpty() && currProcess != -1) {
                int newprocess = prioQueue.remove();
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

	//Gaetum turft ad gera nytt interruption fall tegar vid erum komin med timer
}
