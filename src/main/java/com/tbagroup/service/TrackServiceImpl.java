package com.tbagroup.service;

import com.tbagroup.TrackApplication;
import com.tbagroup.domain.Configuration;
import com.tbagroup.domain.TrackDto;

public class TrackServiceImpl implements TrackService {

    private TrackDto track;
    private CraneService craneService;

    public TrackServiceImpl(Configuration configuration) {
        this.track = new TrackDto(configuration.getName(), configuration.getLength(), configuration.getCraneCount());
        this.craneService = new CraneServiceImpl(this.track);
        TrackApplication.LOGGER.info("Track {} initialize...",track.getName());
    }


    @Override
    public void setCraneService(CraneService craneService) {
        this.craneService = craneService;
    }

    @Override
    public void startWork() {
        craneService.startWork();
    }

    @Override
    public void stopWork() {
        craneService.stopWork();
    }


}
