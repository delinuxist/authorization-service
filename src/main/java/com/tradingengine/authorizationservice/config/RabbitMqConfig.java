package com.tradingengine.authorizationservice.config;

import com.tradingengine.authorizationservice.enums.QueueRoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitMq.queue.user-queue}")
    private String user_queue;

    @Value("${rabbitMq.exchange.user-exchange}")
    private String user_exchange;

    // bean for rabbitmq queue
    @Bean
    public Queue userQueue() {
        return new Queue(user_queue);
    }

    // bean for rabbitmq exchange
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(user_exchange);
    }

    //  binding queue and exchange using routing key
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(userQueue())
                .to(userExchange())
                .with(QueueRoutingKeys.USER_ROUTE_KEY.getKey());
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }


}
