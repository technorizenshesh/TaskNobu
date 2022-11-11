package com.tasknobu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetTask implements Serializable {

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

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("task_title")
        @Expose
        public String taskTitle;
        @SerializedName("task_title_sp")
        @Expose
        public String taskTitleSp;
        @SerializedName("work_destination")
        @Expose
        public String workDestination;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("task_lat")
        @Expose
        public String taskLat;
        @SerializedName("task_lon")
        @Expose
        public String taskLon;
        @SerializedName("task_date")
        @Expose
        public String taskDate;
        @SerializedName("task_time")
        @Expose
        public String taskTime;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("task_status")
        @Expose
        public String taskStatus;
        @SerializedName("task_details")
        @Expose
        public List<TaskDetail> taskDetails = null;
        @SerializedName("task_questionnaires")
        @Expose
        public List<TaskQuestionnaire> taskQuestionnaires = null;
        @SerializedName("task_image_by_worker")
        @Expose
        public List<TaskImageByWorker> taskImageByWorker = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTaskTitle() {
            return taskTitle;
        }

        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        public String getTaskTitleSp() {
            return taskTitleSp;
        }

        public void setTaskTitleSp(String taskTitleSp) {
            this.taskTitleSp = taskTitleSp;
        }

        public String getWorkDestination() {
            return workDestination;
        }

        public void setWorkDestination(String workDestination) {
            this.workDestination = workDestination;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTaskLat() {
            return taskLat;
        }

        public void setTaskLat(String taskLat) {
            this.taskLat = taskLat;
        }

        public String getTaskLon() {
            return taskLon;
        }

        public void setTaskLon(String taskLon) {
            this.taskLon = taskLon;
        }

        public String getTaskDate() {
            return taskDate;
        }

        public void setTaskDate(String taskDate) {
            this.taskDate = taskDate;
        }

        public String getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(String taskTime) {
            this.taskTime = taskTime;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public List<TaskDetail> getTaskDetails() {
            return taskDetails;
        }

        public void setTaskDetails(List<TaskDetail> taskDetails) {
            this.taskDetails = taskDetails;
        }

        public List<TaskQuestionnaire> getTaskQuestionnaires() {
            return taskQuestionnaires;
        }

        public void setTaskQuestionnaires(List<TaskQuestionnaire> taskQuestionnaires) {
            this.taskQuestionnaires = taskQuestionnaires;
        }

        public List<TaskImageByWorker> getTaskImageByWorker() {
            return taskImageByWorker;
        }

        public void setTaskImageByWorker(List<TaskImageByWorker> taskImageByWorker) {
            this.taskImageByWorker = taskImageByWorker;
        }

    }

    public class TaskDetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("task_id")
        @Expose
        public String taskId;
        @SerializedName("task_description")
        @Expose
        public String taskDescription;
        @SerializedName("task_description_sp")
        @Expose
        public String taskDescriptionSp;
        @SerializedName("checked")
        @Expose
        public String checked;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskDescription() {
            return taskDescription;
        }

        public void setTaskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public String getTaskDescriptionSp() {
            return taskDescriptionSp;
        }

        public void setTaskDescriptionSp(String taskDescriptionSp) {
            this.taskDescriptionSp = taskDescriptionSp;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
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

    public class TaskImageByWorker {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("task_id")
        @Expose
        public String taskId;
        @SerializedName("questionnaires_id")
        @Expose
        public String questionnairesId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWorkerId() {
            return workerId;
        }

        public void setWorkerId(String workerId) {
            this.workerId = workerId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getQuestionnairesId() {
            return questionnairesId;
        }

        public void setQuestionnairesId(String questionnairesId) {
            this.questionnairesId = questionnairesId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

    public class TaskQuestionnaire {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("task_id")
        @Expose
        public String taskId;
        @SerializedName("question")
        @Expose
        public String question;
        @SerializedName("question_sp")
        @Expose
        public String questionSp;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("questionnaires_answer")
        @Expose
        public String questionnairesAnswer;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestionSp() {
            return questionSp;
        }

        public void setQuestionSp(String questionSp) {
            this.questionSp = questionSp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getQuestionnairesAnswer() {
            return questionnairesAnswer;
        }

        public void setQuestionnairesAnswer(String questionnairesAnswer) {
            this.questionnairesAnswer = questionnairesAnswer;
        }

    }

}
