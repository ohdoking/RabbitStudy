package com.ohdoking.rabbit.twoconsumer;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();

    
    //2번째 duration
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" worker 1 [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(1);

    final Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");

        System.out.println(" [x] Received '" + message + "'");
        try {
          doWork(message);
        } finally {
          long l = envelope.getDeliveryTag();
          System.out.println(" [x] Done - " + l);
          
          //ack 을 보내줘서 메시지를 잘 받았다고 알려준다.
          /*
           * RabbitMQ not to give more than one message to a worker at a time. 
           * Or, in other words, don't dispatch a new message to a worker until it has processed and acknowledged the previous one. 
           * Instead, it will dispatch it to the next worker that is not still busy
           * 
           */
          
          channel.basicAck(l, false);
        }
      }
    };
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
  }

  private static void doWork(String task) {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException _ignored) {
          Thread.currentThread().interrupt();
        }
  }
}