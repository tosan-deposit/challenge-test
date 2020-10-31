package com.tbagroup.service;

import com.tbagroup.TrackApplication;
import com.tbagroup.domain.CraneDto;
import com.tbagroup.domain.TaskDto;
import com.tbagroup.domain.TrackDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class CraneServiceImpl implements CraneService {

    private TaskService taskService;
    private TrackDto track;
    private ExecutorService executor;

    public CraneServiceImpl(TrackDto track) {
        TrackApplication.LOGGER.debug("initialize crane with all of stuff...");
        this.track = track;
        this.taskService = new TaskServiceImpl(track.getLength());
    }

    @Override
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void startWork() {
        setupCranes();
        executor = Executors.newFixedThreadPool(track.getCraneCount());
        for(CraneDto dto : track.getRelatedCranes()){
            executor.execute(new CraneMovement(dto,track));
        }
    }

    @Override
    public void stopWork() {
        if(executor != null && !executor.isShutdown())
            executor.shutdownNow();
    }

    private void setupCranes() {
        for(int i = 0 ; i < track.getCraneCount() ; i++){
            CraneDto crane = new CraneDto(track.getLeftParking());
            crane.addTasks(new PriorityBlockingQueue<>());
            track.addCrane(crane);
            taskService.produceTask(crane);
        }
    }

    public class CraneMovement implements Runnable {

        private TrackDto track;
        private CraneDto crane;

        public CraneMovement(CraneDto crane, TrackDto track) {
            this.crane = crane;
            this.track = track;
        }

        @Override
        public void run() {
            while (true) {
                TaskDto poll = crane.getTasks().poll();
                if(poll != null) {
                    TrackApplication.LOGGER.debug("processing task {}", poll);
                    move(poll);
                }
            }
        }

        private void move(TaskDto poll) {
//            if()
        }

    }

}
