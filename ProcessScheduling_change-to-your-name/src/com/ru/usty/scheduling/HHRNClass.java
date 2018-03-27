package com.ru.usty.scheduling;

import org.omg.CORBA.INTERNAL;

import java.util.Comparator;

public class HHRNClass implements Comparator<ProcessData> {

    Scheduler HHRNs;

    public HHRNClass(Scheduler sche){
        this.HHRNs = sche;
    }

    @Override
    public int compare(ProcessData t1, ProcessData t2) {
        long first = (HHRNs.processExecution.getProcessInfo(t1.processID).elapsedWaitingTime + HHRNs.processExecution.getProcessInfo(t1.processID).totalServiceTime) / HHRNs.processExecution.getProcessInfo(t1.processID).totalServiceTime;
        long second = (HHRNs.processExecution.getProcessInfo(t2.processID).elapsedWaitingTime + HHRNs.processExecution.getProcessInfo(t2.processID).totalServiceTime) / HHRNs.processExecution.getProcessInfo(t2.processID).totalServiceTime;

        if (first < second) {
            return 1;
        }
        if (first > second) {
            return -1;
        }

        return 0;
    }
}
