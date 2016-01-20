package com.teamlinking.chains.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 16/1/20.
 */
public class NodeVO {

    private Long id;

    private Date nodeTime;

    private Integer nodeType;

    private String content;

    private String picUrl;
    //音频id
    private String audioId;
    //音频地址
    private String audioUrl;
    //音频的长度,单位s
    private Double audioDuration;
    //音视频上传状态
    private Byte audioLoadState;
    //视频id
    private String videoId;
    //音频地址
    private String videoUrl;
    //视频的长度,单位s
    private Double videoDuration;
    //音视频上传状态
    private Byte videoLoadState;

    private String locationLab;
    //纬度
    private Double latitude;
    //经度
    private Double longitude;

    public String getNodeTimeStr(){
        if (nodeTime == null){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(nodeTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getNodeTime() {
        return nodeTime;
    }

    public void setNodeTime(Date nodeTime) {
        this.nodeTime = nodeTime;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Double getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(Double audioDuration) {
        this.audioDuration = audioDuration;
    }

    public Byte getAudioLoadState() {
        return audioLoadState;
    }

    public void setAudioLoadState(Byte audioLoadState) {
        this.audioLoadState = audioLoadState;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Double getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Double videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Byte getVideoLoadState() {
        return videoLoadState;
    }

    public void setVideoLoadState(Byte videoLoadState) {
        this.videoLoadState = videoLoadState;
    }

    public String getLocationLab() {
        return locationLab;
    }

    public void setLocationLab(String locationLab) {
        this.locationLab = locationLab;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
