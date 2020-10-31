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
            Integer otherPosition   = other.getPosition();
            Integer currentPosition = crane.getPosition();
            int position = peek.getEndPosition();
            if((peek.getEndPosition() > other.getPosition()
                && crane.getParkPosition() == 0)){
                TrackApplication.LOGGER.info("crane {} peek {} position {}"
                        , crane,peek ,position);
                if(position == other.getParkPosition() - 1 ){
                    position = other.getParkPosition();
                }

            }else if (peek.getEndPosition() < other.getPosition()
                    && crane.getParkPosition() != 0){
                if(position == other.getParkPosition() + 1 ){
                    position = other.getParkPosition();
                }
            }
            motion = new TaskDto(other.getId(),"Crane-Move",other.getPosition(),position);
            TrackApplication.LOGGER.info("crane {} peek {} position {} motion {}"
                    , crane,peek ,position,motion);
            doIt(motion,other);
            doIt(peek,crane);
        }
        doIt(peek, crane);
    }

    private void doIt(TaskDto poll, CraneDto crane) {
        TrackApplication.LOGGER.info("move container {} type {} from {} , to {} "
                , poll.getId(), poll.getType() ,poll.getStartPosition(), poll.getEndPosition());
        crane.setPosition(poll.getEndPosition());
        TrackApplication.LOGGER.info("current crane position {} "
                , crane.getPosition());
    }

}
