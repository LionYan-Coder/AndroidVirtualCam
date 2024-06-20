package com.lion.virtualcamserver.Vo;

import com.lion.virtualcamserver.enums.ClientType;

public class ClientMessage {
    private String clientId;
    private ClientType clientType;
    private String brand;
    private String model;
    private String sdk;

    public String getBrand() {
        return brand;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }


    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "clientType=" + clientType +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", sdk='" + sdk + '\'' +
                '}';
    }
}
