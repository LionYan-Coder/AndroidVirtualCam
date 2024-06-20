package com.lion.virtualcamserver.controller;

import com.lion.virtualcamserver.Vo.CommonResultVo;
import com.lion.virtualcamserver.Vo.NotifyMessage;
import com.lion.virtualcamserver.enums.CameraStatus;
import com.lion.virtualcamserver.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@CrossOrigin(origins = "*")
public class SseController {

    @Autowired
    private SseService sseService;

    @GetMapping(value = "/connect/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable("clientId") String clientId) {
        final SseEmitter emitter = sseService.getConn(clientId);

        return emitter;
    }


    @GetMapping("/close/{clientId}")
    @ResponseBody
    public CommonResultVo closeConn(@PathVariable("clientId")  String clientId) {
        return sseService.closeConn(clientId);
    }


    @PostMapping("/camera")
    @ResponseBody
    public CommonResultVo<NotifyMessage> sendCameraStatus(@RequestBody NotifyMessage notifyMessage) {
        return sseService.sendCameraStatus(notifyMessage);
    }
}
