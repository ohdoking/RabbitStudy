package com.ohdoking.rabbitmq.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ohdoking.rabbitmq.config.RabbitConfig;
import com.rabbitmq.client.AMQP.Channel;



@Controller
@RequestMapping("/rabbit")
public class RabbitController {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitController.class);
	
	@Resource(name = "mysqlQueueTemplate" )
	private volatile RabbitTemplate mysqlRabbitTemplate;
	
	@Resource(name = "cubridQueueTemplate" )
	private volatile RabbitTemplate cubridRabbitTemplate;
	
	@Resource(name = "mongoQueueTemplate" )
	private volatile RabbitTemplate mongoRabbitTemplate;
	


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> rabbitData(@RequestParam("type")String type) {
		
		String obj = null;
		
		if(type.equals("mysql")){
			obj = (String)mysqlRabbitTemplate.receiveAndConvert(RabbitConfig.QUEUE_NAME_MYSQL);
		}
		else if(type.equals("cubrid")){
			obj = (String)cubridRabbitTemplate.receiveAndConvert(RabbitConfig.QUEUE_NAME_CUBRID);
		}
		else if(type.equals("mongo")){
			obj = (String)mongoRabbitTemplate.receiveAndConvert(RabbitConfig.QUEUE_NAME_MONGO);
		}
		logger.info(" [x] Received '" + obj + "'");
		
/*		 int maxthread =20;
	        ExecutorService es = Executors.newFixedThreadPool(maxthread);
	        try {
	            Connection conn = connectionFactory.newConnection(es);

	            // Thread 당 다른 Channel 을 사용하기 위해서 Thread수 만큼 별도의 채널을 생성하낟.
	           for(int i=0;i<maxthread;i++){
	                   Channel channel = conn.createChannel();     
	                   channel.basicQos(1);
	                   channel.basicConsume("test",false,new MyQueueConsumer(channel));
	           }
	           System.out.println("Invoke "+maxthread+" thread and wait for listening");   
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
*/	        
		
		
		
		/*Channel channel =  rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
			}
		};
		try {
			
			channel.basicConsume(RabbitConfig.EXCHANGE_NAME, true, consumer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		
		Map<String, String> m = new HashMap<String, String>();
		return m;
	}
	
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> inputRabbitData(@RequestParam(value = "type")String type) {
		
		Map<String, String> m = new HashMap<String, String>();
		if(type.equals("mysql")){
			mysqlRabbitTemplate.convertAndSend(type);
			m.put("data", type);
		}
		else if(type.equals("cubrid")){
			cubridRabbitTemplate.convertAndSend(type);
			m.put("data", type);
		}
		else if(type.equals("mongo")){
			mongoRabbitTemplate.convertAndSend(type);
			m.put("data", type);
		}
		else{
			
			m.put("data", "");
		}
			
//			rabbitTemplate.Con(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTE_NAME, message);
		
		
		
		return m;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public Map<String, String> deleteRabbitData(@RequestParam(value = "data")String data) {
		
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("data", data);
		
		return m;	
	}
	
	
}
