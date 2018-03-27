package com.ru.usty.scheduling;

import java.util.Comparator;

public class SRTClass implements Comparator<ProcessData> {

    Scheduler SRTs;

    public SRTClass(Scheduler sche) {
        SRTs = sche;
    }


    @Override
    public int compare(ProcessData t1, ProcessData t2) {

        long first = SRTs.processExecution.getProcessInfo(t1.processID).totalServiceTime - SRTs.processExecution.getProcessInfo(t1.processID).elapsedExecutionTime;
        long second = SRTs.processExecution.getProcessInfo(t2.processID).totalServiceTime - SRTs.processExecution.getProcessInfo(t2.processID).elapsedExecutionTime;

        if (first < second) {
            return -1;
        }
        if (first > second) {
            return 1;
        }
        return 0;
    }
}
