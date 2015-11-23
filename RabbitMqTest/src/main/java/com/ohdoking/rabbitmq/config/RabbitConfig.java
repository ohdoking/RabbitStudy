package com.ohdoking.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig{
	
	public static String EXCHANGE_NAME = "rabbit.ohdoking.exchange";
	
	public static String QUEUE_NAME_MYSQL = "rabbit.ohdoking.queue.mysql";
	public static String QUEUE_NAME_CUBRID = "rabbit.ohdoking.queue.cubrid";
	public static String QUEUE_NAME_MONGO = "rabbit.ohdoking.queue.mongo";
	
	public static String ROUTE_NAME_MYSQL = QUEUE_NAME_MYSQL;
	public static String ROUTE_NAME_CUBRID = QUEUE_NAME_CUBRID;
	public static String ROUTE_NAME_MONGO = QUEUE_NAME_MONGO;
	
/*	@Autowired
	private ConnectionFactory rabbitConnectionFactory;*/
	
	@Bean(name = "rabbitmqConnectionFactory")
	public CachingConnectionFactory rabbitConnectionFactory() {
		//TODO make it possible to customize in subclasses
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setPort(5672);
	    connectionFactory.setUsername("guest");
	    connectionFactory.setPassword("guest");
		return connectionFactory;
	}
	
	@Bean
	DirectExchange ohdokingExchange(){
		return new DirectExchange(EXCHANGE_NAME);
	}
	
	@Bean
	public Queue msyqlQueue(){
		return new Queue(QUEUE_NAME_MYSQL);
	}
	
	@Bean
	public Queue cubridQueue(){
		return new Queue(QUEUE_NAME_CUBRID);
	}
	
	@Bean
	public Queue mongoQueue(){
		return new Queue(QUEUE_NAME_MONGO);
	}
	
	@Bean
	public Binding mysqlExchangeBinding(DirectExchange ohdokingExchange, @Qualifier("msyqlQueue")Queue ohdokingQueue){
		return BindingBuilder.bind(ohdokingQueue).to(ohdokingExchange).with(ROUTE_NAME_MYSQL);
	}
	
	@Bean
	public Binding cubridExchangeBinding(DirectExchange ohdokingExchange, @Qualifier("cubridQueue")Queue ohdokingQueue){
		return BindingBuilder.bind(ohdokingQueue).to(ohdokingExchange).with(ROUTE_NAME_CUBRID);
	}
	
	@Bean
	public Binding mongoExchangeBinding(DirectExchange ohdokingExchange, @Qualifier("mongoQueue")Queue ohdokingQueue){
		return BindingBuilder.bind(ohdokingQueue).to(ohdokingExchange).with(ROUTE_NAME_MONGO);
	}
	
	@Bean
	public RabbitTemplate mysqlQueueTemplate(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory){
		RabbitTemplate rt = new RabbitTemplate();
		rt.setQueue(QUEUE_NAME_MYSQL);
		rt.setExchange(EXCHANGE_NAME);
		rt.setRoutingKey(ROUTE_NAME_MYSQL);
		rt.setConnectionFactory(connectionFactory);
		return rt;
	}
	
	@Bean
	public RabbitTemplate cubridQueueTemplate(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory){
		RabbitTemplate rt = new RabbitTemplate();
		rt.setQueue(QUEUE_NAME_CUBRID);
		rt.setExchange(EXCHANGE_NAME);
		rt.setRoutingKey(ROUTE_NAME_CUBRID);
		rt.setConnectionFactory(connectionFactory);
		return rt;
	}
	
	@Bean
	public RabbitTemplate mongoQueueTemplate(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory){
		RabbitTemplate rt = new RabbitTemplate();
		rt.setQueue(QUEUE_NAME_MONGO);
		rt.setExchange(EXCHANGE_NAME);
		rt.setRoutingKey(ROUTE_NAME_MONGO);
		rt.setConnectionFactory(connectionFactory);
		return rt;
	}
	
	
	@Bean
	public RabbitAdmin rabbitAdmin(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory connectionFactory) {
		
		RabbitAdmin admin = new RabbitAdmin(connectionFactory);		
		return admin;
	}
	
	/*@Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(exampleListener());
        return container;
    }


    @Bean
    public MessageListener exampleListener() {
        return new MessageListener() {
            public void onMessage(Message message) {
                System.out.println("received: " + new String(message.getBody()));
            }
        };
    }
 
	*/

}
