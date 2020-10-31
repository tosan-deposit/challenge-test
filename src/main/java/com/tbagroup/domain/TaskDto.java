package com.tbagroup.domain;

import java.util.Objects;
import java.util.UUID;

public class TaskDto implements Comparable<TaskDto> {

    private final UUID id;
    private final String type;
    private Integer startPosition;
    private Integer endPosition;
    private Integer priority;

    public TaskDto(Integer startPosition, Integer endPosition) {
        this.id = UUID.randomUUID();
        this.type = "Container";
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.priority = 0;
    }

    public TaskDto(String type, Integer startPosition, int endPosition) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.priority = 5;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(id, taskDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(TaskDto o) {
        return this.priority.compareTo(o.priority);
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", priority=" + priority +
                '}';
    }
}
