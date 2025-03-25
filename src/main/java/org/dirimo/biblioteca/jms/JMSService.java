package org.dirimo.biblioteca.jms;

import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.jms.enumerated.JMSQueueType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JMSService {
    private final JmsTemplate jmsTemplate;
    public void sendMessage(JMSQueueType queue, String message) {
        jmsTemplate.convertAndSend(queue.getQueueName(), message);
    }
}