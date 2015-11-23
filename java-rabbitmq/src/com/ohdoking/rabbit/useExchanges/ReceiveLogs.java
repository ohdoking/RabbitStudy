package com.ohdoking.rabbit.useExchanges;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogs {

	private static final String EXCHANGE_NAME = "logs";
	private static final String HOST = "localhost";

	public static void main(String[] args) {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);

			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			
			
			/*
				queue 이름을 자동으로 생성하고 그 큐를 가져온다.
			 */
			String queueName = channel.queueDeclare().getQueue();
			
			/*
			 * queue를 exchange에 바인딩 시킨다. 
			 */
			channel.queueBind(queueName, EXCHANGE_NAME, "");
			
			System.out.println(" Worker ");
			
			Consumer consumer = new DefaultConsumer(channel){
				 @Override
			      public void handleDelivery(String consumerTag, Envelope envelope,
			                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
			        String message = new String(body, "UTF-8");
			        System.out.println(" [x] Received '" + message + "'");
			      }
			};
			
			channel.basicConsume(queueName, true, consumer);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
