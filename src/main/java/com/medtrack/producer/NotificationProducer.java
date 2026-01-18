package com.medtrack.producer;

import com.medtrack.dto.MedicineLogDTO;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private final SqsTemplate sqsTemplate;

    @Value("${medtrack.sqs.queue-name}")
    private String queueName;

    public NotificationProducer(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void sendNotification(MedicineLogDTO message) {
        sqsTemplate.send(queueName, message);
    }
}