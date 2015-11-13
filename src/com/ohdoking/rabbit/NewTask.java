package com.ohdoking.rabbit;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] argv) throws java.io.IOException {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			Connection connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

//			int a = (int) (Math.random() * 10);
			
			
			for(int i = 0; i < 5; i++){
				
				String message = String.valueOf(i);

				channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println(" [x] Sent '" + message + "'");
			}

			channel.close();
			connection.close();

		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}