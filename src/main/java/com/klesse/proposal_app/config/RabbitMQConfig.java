package com.klesse.proposal_app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue createPendingProposalQueueMsCreditAnalysis() {
        return QueueBuilder.durable("pending-proposal.ms-credit-analysis").build();
    }

    @Bean
    public Queue createPendingProposalQueueNotification() {
        return QueueBuilder.durable("pending-proposal.ms-notification").build();
    }

    @Bean
    public Queue createCompletedProposalQueueMsProposal() {
        return QueueBuilder.durable("completed-proposal.ms-proposal").build();
    }

    @Bean
    public Queue createCompletedProposalQueueNotification() {
        return QueueBuilder.durable("completed-proposal.ms-notification").build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> adminInitializer(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange createFanoutExchangePendingProposal() {
        return ExchangeBuilder.fanoutExchange("pending-proposal.ex").build();
    }

    @Bean
    public Binding createBindingPendingProposalMSCreditAnalysis() {
        return BindingBuilder.bind(createPendingProposalQueueMsCreditAnalysis())
                .to(createFanoutExchangePendingProposal());
    }

    @Bean
    public Binding createBindingPendingProposalMSNotification() {
        return BindingBuilder.bind(createPendingProposalQueueNotification())
                .to(createFanoutExchangePendingProposal());
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;

    }
}
