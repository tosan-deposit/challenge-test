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

    private static TaskDto createDummyTask(Integer parkPosition
            , Integer maxLength) {
        TaskDto taskDto = null;
        Random random = new Random();
        int last = random.ints(1, maxLength).findFirst().getAsInt() + 1;
        TrackApplication.LOGGER.debug("last {}", last);
        int first = random.ints(1, last).findFirst().getAsInt();
        TrackApplication.LOGGER.debug("first {}", first);
        if(parkPosition > maxLength){
            taskDto = new TaskDto(last ,first);
        }else {
            taskDto = new TaskDto(first, last);
        }
        return taskDto;
    }

    public class TaskProducer implements Runnable {

        private CraneDto crane;

        public TaskProducer(CraneDto crane) {
            this.crane = crane;
        }

        @Override
        public void run() {
            while (true) {
                TaskDto dto = TaskServiceImpl.createDummyTask(crane.getParkPosition(),maxLength);
                crane.getTasks().offer(dto);
                TrackApplication.LOGGER.info("task {} is  generated for crane {}", dto, crane.getId());
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
