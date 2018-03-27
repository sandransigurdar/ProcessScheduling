package com.ru.usty.scheduling;

import java.util.Comparator;

public class SPNClass implements Comparator<ProcessData> {

    Scheduler SPNs;

    public SPNClass(Scheduler sche){
        SPNs = sche;
    }


    @Override
    public int compare(ProcessData t1, ProcessData t2) {


        long first = SPNs.processExecution.getProcessInfo(t1.processID).totalServiceTime;
        long second =SPNs.processExecution.getProcessInfo(t2.processID).totalServiceTime;

        if (first < second) {
            return -1;
        }
        if (first > second) {
            return 1;
        }
        return 0;
    }
}
