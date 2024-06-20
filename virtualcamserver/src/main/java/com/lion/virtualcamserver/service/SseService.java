package com.lion.virtualcamserver.service;

import com.lion.virtualcamserver.Vo.ClientMessage;
import com.lion.virtualcamserver.Vo.CommonResultVo;
import com.lion.virtualcamserver.Vo.NotifyMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    public SseEmitter getConn(String clientId);
    public CommonResultVo closeConn(String clientId);
    public CommonResultVo sendClient(String clientId, ClientMessage message);
    public CommonResultVo<NotifyMessage> sendCameraStatus(NotifyMessage notifyMessage);
}
