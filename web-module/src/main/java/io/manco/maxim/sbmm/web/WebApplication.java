package io.manco.maxim.sbmm.web;

import io.manco.maxim.sbmm.core.rabbitMQ.RabbitMQListener;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;


import io.manco.maxim.sbmm.core.CoreApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApplication {

	public final static String PDF_MERGE_QUEUE= "pdf-merge-queue";

	@Bean
	Queue queue(){
		return new Queue(PDF_MERGE_QUEUE, false);
	}

	@Bean
	TopicExchange exchange(){
		return new TopicExchange("pdf-nerge-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange topicExchange){
		return BindingBuilder.bind(queue).to(topicExchange).with(PDF_MERGE_QUEUE);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(PDF_MERGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(RabbitMQListener rabbitMQListener){
		return new MessageListenerAdapter(rabbitMQListener, "receiveMessage");
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder()
			.bannerMode(Banner.Mode.CONSOLE)
			.sources(CoreApplication.class, WebApplication.class)
			.run(args);
	}

}
