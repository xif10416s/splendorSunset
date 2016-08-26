package com.fxi.sunset.task.data;

/**
 * Created by seki on 16/8/24.
 */
public final class TaskRecord {
	private Integer day;
	private String time;
	private Long taskId;

	public TaskRecord(Integer day, String time, Long taskId) {
		this.day = day;
		this.taskId = taskId;
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
}
