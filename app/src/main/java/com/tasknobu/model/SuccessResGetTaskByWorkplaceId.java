package com.tasknobu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetTaskByWorkplaceId implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("task_id")
        @Expose
        public String taskId;
        @SerializedName("work_place_id")
        @Expose
        public String workPlaceId;
        @SerializedName("task_short_desc")
        @Expose
        public String taskShortDesc;
        @SerializedName("task_long_desc")
        @Expose
        public String taskLongDesc;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("work_places_details")
        @Expose
        public List<WorkPlacesDetail> workPlacesDetails = null;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getWorkPlaceId() {
            return workPlaceId;
        }

        public void setWorkPlaceId(String workPlaceId) {
            this.workPlaceId = workPlaceId;
        }

        public String getTaskShortDesc() {
            return taskShortDesc;
        }

        public void setTaskShortDesc(String taskShortDesc) {
            this.taskShortDesc = taskShortDesc;
        }

        public String getTaskLongDesc() {
            return taskLongDesc;
        }

        public void setTaskLongDesc(String taskLongDesc) {
            this.taskLongDesc = taskLongDesc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public List<WorkPlacesDetail> getWorkPlacesDetails() {
            return workPlacesDetails;
        }

        public void setWorkPlacesDetails(List<WorkPlacesDetail> workPlacesDetails) {
            this.workPlacesDetails = workPlacesDetails;
        }

    }

    public class WorkPlacesDetail {

        @SerializedName("work_place_id")
        @Expose
        public String workPlaceId;
        @SerializedName("work_place_description_short")
        @Expose
        public String workPlaceDescriptionShort;
        @SerializedName("work_place_description_long")
        @Expose
        public String workPlaceDescriptionLong;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getWorkPlaceId() {
            return workPlaceId;
        }

        public void setWorkPlaceId(String workPlaceId) {
            this.workPlaceId = workPlaceId;
        }

        public String getWorkPlaceDescriptionShort() {
            return workPlaceDescriptionShort;
        }

        public void setWorkPlaceDescriptionShort(String workPlaceDescriptionShort) {
            this.workPlaceDescriptionShort = workPlaceDescriptionShort;
        }

        public String getWorkPlaceDescriptionLong() {
            return workPlaceDescriptionLong;
        }

        public void setWorkPlaceDescriptionLong(String workPlaceDescriptionLong) {
            this.workPlaceDescriptionLong = workPlaceDescriptionLong;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}
