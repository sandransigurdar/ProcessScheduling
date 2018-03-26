package com.ru.usty.scheduling;

import java.util.Comparator;

public class SPNClass implements Comparator<Integer> {

    Scheduler SPNs;

    public SPNClass(Scheduler sche){
        SPNs = sche;
    }


    @Override
    public int compare(Integer t1, Integer t2) {


        long first = SPNs.processExecution.getProcessInfo(t1).totalServiceTime;
        long second =SPNs.processExecution.getProcessInfo(t2).totalServiceTime;

        if (first < second) {
            return -1;
        }
        if (first > second) {
            return 1;
        }
        return 0;
    }
}
