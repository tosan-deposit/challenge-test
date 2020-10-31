package com.tbagroup.service;

import com.tbagroup.TrackApplication;
import com.tbagroup.domain.CraneDto;
import com.tbagroup.domain.TaskDto;
import com.tbagroup.domain.TrackDto;

public class MoveServiceImpl implements MoveService {

    private TrackDto track;

    public MoveServiceImpl(TrackDto track) {
        this.track = track;
    }

    @Override
    public void move(CraneDto crane) {
        TaskDto peek = crane.getTasks().peek();
        if (peek != null) {
            TrackApplication.LOGGER.debug("processing task {}", peek);
            handleConflict(peek, crane);
            crane.getTasks().poll();
            TrackApplication.LOGGER.info("task {} is done", peek.getId());
        }
    }

    private void handleConflict(TaskDto peek, CraneDto crane) {
        CraneDto other = track.getOtherCrane(crane);
        if (other != null) {
            TaskDto motion = null;
            int position = peek.getEndPosition();
            if((peek.getEndPosition() > other.getPosition()
                && crane.getParkPosition() == 0)){
                if(position == other.getParkPosition() - 1 ){
                    position = other.getParkPosition();
                }

            }else if (peek.getEndPosition() < other.getPosition()
                    && crane.getParkPosition() != 0){
                if(position == other.getParkPosition() + 1 ){
                    position = other.getParkPosition();
                }
            }
            motion = new TaskDto("Action-Base",other.getPosition(),position);
            move(motion,other);
            move(peek,crane);
        }
        move(peek, crane);
    }

    private synchronized void moveCrane(CraneDto crane ,Integer endPosition) {

    }

    private synchronized void move(TaskDto poll, CraneDto crane) {
        TrackApplication.LOGGER.info("move container {} type {} from {} , to {} "
                , poll.getId(), poll.getType() ,poll.getStartPosition(), poll.getEndPosition());
        crane.setPosition(poll.getEndPosition());
        TrackApplication.LOGGER.info("current crane position {} "
                , crane.getPosition());
    }

}
