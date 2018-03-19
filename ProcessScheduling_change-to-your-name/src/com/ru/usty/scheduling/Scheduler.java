package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;

	//Queue<ProcessData> processQueue;
	LinkedList<ProcessData> processQueue;
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
	public void startScheduling(Policy policy, int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		
		processQueue = new LinkedList<ProcessData>();

		//processQueue.remove(5); //Remove-a stakið 5
		
		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */

			//ProcessData d = processQueue.remove();
			//processQueue.remove(new ProcessData(5)); //Remove=a stak nr 5
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
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
	public void processAdded(int processID) {
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
			if(processQueue.isEmpty()) {
				processExecution.switchToProcess(processID);// Vid munum nota thetta, en kannski a fleiri stodum
			}
			processQueue.add(new ProcessData(processID));
			
			break;
		case RR:	//Round robin
			System.out.println("processAddfall:: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
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
		
		
		/**
		 * Add scheduling code here
		 */
		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("processFinishedFall: First-come-first-served");
			/**
			 * Add your policy specific add code here (if needed)
			 */
			processQueue.removeFirst();
			if(!processQueue.isEmpty()) {
				processExecution.switchToProcess(processQueue.getFirst().processID);// Vid munum nota thetta, en kannski a fleiri stodum
			}
			break;
		case RR:	//Round robin
			System.out.println("processFinishedFall:: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
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
	
	//Gætum þurft að gera nýtt interruption fall þegar við erum komin með timer
}
