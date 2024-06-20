package com.lion.virtualcamserver.Vo;

import com.lion.virtualcamserver.enums.CameraStatus;
import com.lion.virtualcamserver.enums.ClientType;

public class NotifyMessage {
    private ClientType clientType;
    private CameraStatus cameraStatus;

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public CameraStatus getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(CameraStatus cameraStatus) {
        this.cameraStatus = cameraStatus;
    }
}
