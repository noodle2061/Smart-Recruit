package com.ptit.thesis.smartrecruit.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.ptit.thesis.smartrecruit.dto.message.CvResultScoreMessage;
import com.ptit.thesis.smartrecruit.service.ApplicationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CvResultConsumer {
    ApplicationService applicationService;

    @RabbitListener(queues = "${rabbitmq.queue.cv-result}")
    public void receiveCvResult(CvResultScoreMessage message) {
        applicationService.updateApplicationScore(message);
    }
}
