package com.teamlinking.chains.eventbus;

import com.teamlinking.chains.common.Constants;

/**
 * Created by admin on 16/1/22.
 */
public class UploadEvent {

    private String mediaId;
    private Constants.FileType fileType;
    private Constants.OwnerType ownerType;
    private Long ownerId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Constants.FileType getFileType() {
        return fileType;
    }

    public void setFileType(Constants.FileType fileType) {
        this.fileType = fileType;
    }

    public Constants.OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Constants.OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
