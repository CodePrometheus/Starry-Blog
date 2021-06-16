package com.star.core.util;

import com.alibaba.fastjson.JSON;
import com.star.core.config.RabbitConfig;
import com.star.core.service.dto.EmailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 06-04-2021 09:55
 */
@Component
@RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
public class EmailSend {

    @Value("${spring.mail.username}")
    private String email;

    @Resource
    private JavaMailSender javaMailSender;

    @RabbitHandler
    public void process(byte[] data) {
        EmailDTO emailDTO = JSON.parseObject(new String(data), EmailDTO.class);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(emailDTO.getEmail());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getContent());
        javaMailSender.send(message);
    }

}

