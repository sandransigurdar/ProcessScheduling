package com.ru.usty.scheduling;

import java.util.Comparator;

public class SRTClass implements Comparator<Integer> {

    Scheduler SRTs;

    public SRTClass(Scheduler sche) {
        SRTs = sche;
    }


    @Override
    public int compare(Integer t1, Integer t2) {

        long first = SRTs.processExecution.getProcessInfo(t1).totalServiceTime - SRTs.processExecution.getProcessInfo(t1).elapsedExecutionTime;
        long second = SRTs.processExecution.getProcessInfo(t2).totalServiceTime - SRTs.processExecution.getProcessInfo(t2).elapsedExecutionTime;

        if (first < second) {
            return -1;
        }
        if (first > second) {
            return 1;
        }
        return 0;
    }
}
