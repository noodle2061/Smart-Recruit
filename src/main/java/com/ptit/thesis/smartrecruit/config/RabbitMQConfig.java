package com.ptit.thesis.smartrecruit.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.cv-processing}")
    private String cvProcessingQueue;

    @Value("${rabbitmq.exchange.internal}")
    private String internalExchange;

    @Value("${rabbitmq.routing-key.cv-upload}")
    private String cvUploadRoutingKey;

    @Value("${rabbitmq.queue.cv-result}")
    private String cvResultQueue;

    @Value("${rabbitmq.routing-key.cv-result}")
    private String cvResultRoutingKey;

    @Bean
    public Queue cvProcessingQueue() {
        return new Queue(cvProcessingQueue);
    }

    @Bean
    public Queue cvResultQueue() {
        return new Queue(cvResultQueue);
    }

    @Bean
    public TopicExchange internalExchange() {
        return new TopicExchange(internalExchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(cvProcessingQueue())
                .to(internalExchange())
                .with(cvUploadRoutingKey);
    }

    @Bean
    public Binding resultBinding() {
        return BindingBuilder
                .bind(cvResultQueue())
                .to(internalExchange())
                .with(cvResultRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}