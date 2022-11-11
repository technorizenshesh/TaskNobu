package com.tasknobu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,May,2022
 */
public class SuccessResWorkplaceData implements Serializable {

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

        @SerializedName("work_places_data_id")
        @Expose
        public String workPlacesDataId;
        @SerializedName("work_places_id")
        @Expose
        public String workPlacesId;
        @SerializedName("work_places_data_name")
        @Expose
        public String workPlacesDataName;
        @SerializedName("work_places_data_value")
        @Expose
        public String workPlacesDataValue;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("work_place_details")
        @Expose
        public List<WorkPlaceDetail> workPlaceDetails = null;

        public String getWorkPlacesDataId() {
            return workPlacesDataId;
        }

        public void setWorkPlacesDataId(String workPlacesDataId) {
            this.workPlacesDataId = workPlacesDataId;
        }

        public String getWorkPlacesId() {
            return workPlacesId;
        }

        public void setWorkPlacesId(String workPlacesId) {
            this.workPlacesId = workPlacesId;
        }

        public String getWorkPlacesDataName() {
            return workPlacesDataName;
        }

        public void setWorkPlacesDataName(String workPlacesDataName) {
            this.workPlacesDataName = workPlacesDataName;
        }

        public String getWorkPlacesDataValue() {
            return workPlacesDataValue;
        }

        public void setWorkPlacesDataValue(String workPlacesDataValue) {
            this.workPlacesDataValue = workPlacesDataValue;
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

        public List<WorkPlaceDetail> getWorkPlaceDetails() {
            return workPlaceDetails;
        }

        public void setWorkPlaceDetails(List<WorkPlaceDetail> workPlaceDetails) {
            this.workPlaceDetails = workPlaceDetails;
        }

    }

    public class WorkPlaceDetail {

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
