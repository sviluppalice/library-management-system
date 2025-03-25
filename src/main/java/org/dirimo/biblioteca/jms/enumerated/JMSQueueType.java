package org.dirimo.biblioteca.jms.enumerated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum JMSQueueType {

    QUEUE_RESERVATIONS("reservation_history"),
    QUEUE_CATALOG("catalog");

    private final String queueName;
}
