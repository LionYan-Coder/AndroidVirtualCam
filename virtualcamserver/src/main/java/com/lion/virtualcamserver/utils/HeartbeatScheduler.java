package com.lion.virtualcamserver.utils;

import com.lion.virtualcamserver.service.impl.SseServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatScheduler {

    private final SseServiceImpl sseService;

    public HeartbeatScheduler(SseServiceImpl sseService) {
        this.sseService = sseService;
    }

    @Scheduled(fixedRate = 8000) // 每15秒执行一次
    public void sendHeartbeat() {
        sseService.sendHeartbeatToClients();
    }
}
