package com.tbagroup.domain;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class CraneDto {
    private final UUID id;
    private Boolean inPark;
    private Integer position;
    private BlockingQueue<TaskDto> tasks = new PriorityBlockingQueue<>();

    public CraneDto(Integer position) {
        this.id = UUID.randomUUID();
        this.inPark = true;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public boolean isInPark() {
        return inPark;
    }

    public void setInPark(boolean inPark) {
        this.inPark = inPark;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BlockingQueue<TaskDto> getTasks() {
        if(tasks == null)
            tasks = new PriorityBlockingQueue<>();
        return tasks;
    }

    public void addTasks(BlockingQueue<TaskDto> tasks){
        getTasks().addAll(tasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CraneDto craneDto = (CraneDto) o;
        return Objects.equals(id, craneDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CraneDto{" +
                "id=" + id +
                ", inPark=" + inPark +
                ", position=" + position +
                '}';
    }
}
