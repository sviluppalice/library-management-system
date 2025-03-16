package org.dirimo.biblioteca.jms;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JMSService {

    private static final String QUEUE_NAME = "reservation_history";

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(QUEUE_NAME, message);
    }
}