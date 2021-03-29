package com.star.rest;

import com.star.service.impl.WebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zzStar
 * @Date: 03-29-2021 19:15
 */
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketServiceImpl webSocketService;



}
