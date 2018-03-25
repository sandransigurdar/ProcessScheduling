package com.ru.usty.scheduling;

import org.omg.CORBA.INTERNAL;

import java.util.Comparator;

public class HHRNClass implements Comparator<Integer> {

    Scheduler HHRNs;

    public HHRNClass(Scheduler sche){
        this.HHRNs = sche;
    }

    @Override
    public int compare(Integer t1, Integer t2) {

        long first = (HHRNs.processExecution.getProcessInfo(t1).elapsedWaitingTime + HHRNs.processExecution.getProcessInfo(t1).totalServiceTime) / HHRNs.processExecution.getProcessInfo(t1).totalServiceTime;
        long second = (HHRNs.processExecution.getProcessInfo(t2).elapsedWaitingTime + HHRNs.processExecution.getProcessInfo(t2).totalServiceTime) / HHRNs.processExecution.getProcessInfo(t2).totalServiceTime;

        if (first < second) {
            return -1;
        }
        if (first > second) {
            return 1;
        }

        return 0;
    }
}
