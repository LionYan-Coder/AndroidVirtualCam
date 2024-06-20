package com.lion.virtualcamserver.service.impl;

import com.lion.virtualcamserver.Vo.ClientMessage;
import com.lion.virtualcamserver.Vo.CommonResultVo;
import com.lion.virtualcamserver.Vo.NotifyMessage;
import com.lion.virtualcamserver.service.SseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseServiceImpl implements SseService {
    private static final Logger logger = LogManager.getLogger("SSE service");
    private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, ClientMessage> clients = new ConcurrentHashMap<>();

    @Override
    public SseEmitter getConn(String clientId) {
        final SseEmitter sseEmitter = SSE_CACHE.get(clientId);
        if (sseEmitter != null){
            return sseEmitter;
        }else {
            final SseEmitter emitter = new SseEmitter(5 * 60 * 60 * 1000L);
            // 注册超时回调，超时后触发
            emitter.onTimeout(() -> {
                logger.warn("连接已超时，正准备关闭，clientId = {}", clientId);
                System.out.println();
                SSE_CACHE.remove(clientId);
            });
            // 注册完成回调，调用 emitter.complete() 触发
            emitter.onCompletion(() -> {
                logger.warn("连接已关闭，正准备释放，clientId = {}", clientId);
                System.out.println("连接已关闭，正准备释放，clientId = "+clientId);
                SSE_CACHE.remove(clientId);
            });
            // 注册异常回调，调用 emitter.completeWithError() 触发
            emitter.onError(throwable -> {
                logger.warn("连接异常，正准备关闭，clientId = {}", clientId);
                SSE_CACHE.remove(clientId);
            });

            SSE_CACHE.put(clientId, emitter);
            try {
                emitter.send(SseEmitter.event().data("heartbeat"));
            } catch (IOException e) {
                // 处理异常情况
                SSE_CACHE.remove(clientId);
            }

//            new Thread(() -> {
//                try {
//                    while (true) {
//                        SSE_CACHE.forEach((key,e) -> {
//                            try {
//                                e.send(SseEmitter.event().comment("heartbeat"));
//                            } catch (IOException ex) {
//                                throw new RuntimeException(ex);
//                            }
//                        });
//                        Thread.sleep(15000); // 每15秒发送一次心跳
//                    }
//                } catch (Exception e) {
//                    emitter.completeWithError(e);
//                }
//            }).start();

            return emitter;
        }
    }

    @Override
    public CommonResultVo closeConn(String clientId) {
        final SseEmitter sseEmitter = SSE_CACHE.get(clientId);
        if (sseEmitter != null) {
            sseEmitter.complete();
        }

        return CommonResultVo.success();
    }

    @Override
    public CommonResultVo sendClient(String clientId, ClientMessage message) {
        clients.put(clientId, message);
        return CommonResultVo.success();
    }

    @Override
    public CommonResultVo<NotifyMessage> sendCameraStatus(NotifyMessage notifyMessage) {

        SSE_CACHE.forEach((key,emitter) -> {
            try {
                logger.info("发送状态通知，clientId = {}", key);
                logger.info("发送状态通知，status = {}", notifyMessage.getCameraStatus());
                System.out.println("发送状态通知，clientId = "+ key + "status = " + notifyMessage.getCameraStatus());
                emitter.send(CommonResultVo.success(notifyMessage));
            } catch (IOException e) {
                logger.error("发送状态通知错误 error = {}",e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return CommonResultVo.success(notifyMessage);
    }

    public void sendHeartbeatToClients() {
        SSE_CACHE.forEach((key, emitter) -> {
            try {
                emitter.send(SseEmitter.event().data("heartbeat"));
            } catch (IOException e) {
                // 处理异常情况
                SSE_CACHE.remove(key);
            }
        });
    }

    private void clearClients(){
        clients.clear();
    }
}
