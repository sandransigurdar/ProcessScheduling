package com.ru.usty.scheduling;


import java.util.LinkedList;

public class TimeCalculations {
    public void AddToList(long timeToAdd) {

    }

    static public long AverageTime (LinkedList<Long> list) {
        long time = 0;
        if(list.size() <= 0) {
        	return 0;
        }
        for(Long  t: list) {
        	time += t;
        }
        return time/list.size();
    }
}
