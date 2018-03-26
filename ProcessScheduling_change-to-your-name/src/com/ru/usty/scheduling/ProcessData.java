package com.ru.usty.scheduling;

public class ProcessData {	
	
	int processID;
	
	public ProcessData(int processID) {
		this.processID = processID;
	}

	public void setStartTime () {startTime = System.currentTimeMillis();}

	public void setStopTime () { stopTime = System.currentTimeMillis() - startTime; }

	long startTime = 0;
	long stopTime = 0;
}
