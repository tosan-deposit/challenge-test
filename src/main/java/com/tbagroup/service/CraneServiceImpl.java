package com.tbagroup.service;

import com.tbagroup.TrackApplication;
import com.tbagroup.domain.CraneDto;
import com.tbagroup.domain.TrackDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class CraneServiceImpl implements CraneService {

    private TaskService taskService;
    private TrackDto track;
    private ExecutorService executor;
    private MoveService moveService;

    public CraneServiceImpl(TrackDto track) {
        this.track = track;
        this.taskService = new TaskServiceImpl(track.getLength());
        this.moveService = new MoveServiceImpl(track);
    }

    @Override
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void setMoveService(MoveService moveService) {
        this.moveService = moveService;
    }

    @Override
    public void startWork() {
        setupCranes();
        executor = Executors.newFixedThreadPool(track.getCraneCount());
        for (CraneDto dto : track.getRelatedCranes()) {
            executor.execute(new CraneMovement(dto, track));
        }
    }

    @Override
    public void stopWork() {
        if (executor != null && !executor.isShutdown())
            executor.shutdownNow();
    }

    private void setupCranes() {
        int park = track.getLeftParking();
        for (int i = 0; i < track.getCraneCount(); i++) {
            CraneDto crane = new CraneDto(park);
            crane.addTasks(new PriorityBlockingQueue<>());
            track.addCrane(crane);
            taskService.produceTask(crane);
            TrackApplication.LOGGER.info("crane {} initialize in parking {}..."
                    , crane.getId(), crane.getPosition());
            park = track.getRightParking();
        }
    }

    public class CraneMovement implements Runnable {

        private volatile TrackDto track;
        private volatile CraneDto crane;

        public CraneMovement(CraneDto crane, TrackDto track) {
            this.crane = crane;
            this.track = track;
        }

        @Override
        public void run() {
            while (true) {
                moveService.move(crane);
            }
        }

    }

}
