package com.tbagroup.service;

import com.tbagroup.TrackApplication;
import com.tbagroup.domain.CraneDto;
import com.tbagroup.domain.TaskDto;

import java.util.Random;

public class TaskServiceImpl implements TaskService {

    private Integer maxLength;

    public TaskServiceImpl(Integer maxLength) {
        this.maxLength = maxLength;

    }

    @Override
    public void produceTask(CraneDto crane) {
        Thread thread = new Thread(new TaskProducer(crane));
        thread.setDaemon(true);
        thread.start();
    }

    private static TaskDto createDummyTask(Integer maxLength) {
        Random random = new Random();
        int last = random.ints(1, maxLength).findFirst().getAsInt() + 1;
        TrackApplication.LOGGER.debug("last {}", last);
        int first = random.ints(1, last).findFirst().getAsInt();
        TrackApplication.LOGGER.debug("first {}", first);
        return new TaskDto(first, last);
    }

    public class TaskProducer implements Runnable {

        private CraneDto crane;

        public TaskProducer(CraneDto crane) {
            this.crane = crane;
        }

        @Override
        public void run() {
            while (true) {
                TaskDto dto = TaskServiceImpl.createDummyTask(maxLength);
                boolean offer = crane.getTasks().offer(dto);
                TrackApplication.LOGGER.debug("is task {} generated ? {}", dto, offer);
                try {
//                    wait(60000);
//                    notify();
//                    notify();
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
