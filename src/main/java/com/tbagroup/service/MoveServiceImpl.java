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
    public synchronized void move(CraneDto crane) {
        TaskDto peek = crane.getTasks().peek();
        if (peek != null) {
            handler(peek, crane);
            crane.getTasks().poll();
            TrackApplication.LOGGER.info("task {} is done", peek.getId());
        }
    }

    private void handler(TaskDto peek, CraneDto crane) {
        CraneDto other = track.getOtherCrane(crane);
        if (other != null) {
            TaskDto motion = null;
            int position = peek.getEndPosition();
            if((peek.getEndPosition() >= other.getPosition()
                && crane.getParkPosition() == 0)){
                position ++;
                if(position == other.getParkPosition() - 1 ){
                    position = other.getParkPosition();
                }

            }else if (peek.getEndPosition() <= other.getPosition()
                    && crane.getParkPosition() != 0){
                position --;
                if(position == other.getParkPosition() + 1 ){
                    position = other.getParkPosition();
                }
            }
            if(other.isInPark()){
                position = other.getParkPosition();
            }
            motion = new TaskDto(other.getId(),"Crane-Move",other.getPosition(),position);
            doIt(motion,other);
        }
        doIt(peek, crane);
    }

    private void doIt(TaskDto poll, CraneDto crane) {
        if(poll.getStartPosition() == poll.getEndPosition()){
            TrackApplication.LOGGER.info("no movement for crane {} type {} from {} , to {} "
                    , poll.getId(), poll.getType() ,poll.getStartPosition(), poll.getEndPosition());
        }else {
            TrackApplication.LOGGER.info("move container {} type {} from {} , to {} "
                    , poll.getId(), poll.getType(), poll.getStartPosition(), poll.getEndPosition());
            crane.setPosition(poll.getEndPosition());
        }
        TrackApplication.LOGGER.info("current crane {} position [{}-{}] "
                , crane.getId(),crane.getParkPosition(),crane.getPosition());
    }

}
