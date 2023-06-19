package com.example.springamqp.aula1;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueCashback() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "orders.v1.order-created.dlx");
//        Enviaria direto para a queue sem passar pela exchange
//        args.put("x-dead-letter-routing-key", "orders.v1.order-created.dlx.generate-cashback.dlq");
        return new Queue("orders.v1.order-created.generate-cashback", true, false, false, args);
    }

    @Bean
    public Binding binding() {
        var queue = queueCashback();
        var fanoutExchange = new FanoutExchange("orders.v1.order-created");
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Queue queueCashbackDLQ() {
        return new Queue("orders.v1.order-created.dlx.generate-cashback.dlq");
    }

    @Bean
    public Queue queueCashbackParkingLot() {
        return new Queue("orders.v1.order-created.dlx.generate-cashback.dlq.parking-lot");
    }

    @Bean
    public Binding bindingDLQ() {
        var queue = queueCashbackDLQ();
        var fanoutExchange = new FanoutExchange("orders.v1.order-created.dlx");
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Para usar json na serialização das mensagens
     *
     * @param connectionFactory
     * @param messageConverter
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
