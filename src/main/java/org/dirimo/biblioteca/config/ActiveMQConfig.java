package org.dirimo.biblioteca.config;

import jakarta.jms.JMSException;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms

public class ActiveMQConfig {

    @Value("${spring.artemis.broker-url}")
    private String brokerUrl;

    @Value("${spring.artemis.user}")
    private String username;

    @Value("${spring.artemis.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        factory.setUser(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() throws JMSException {
        return new JmsTemplate(connectionFactory());
    }
}
