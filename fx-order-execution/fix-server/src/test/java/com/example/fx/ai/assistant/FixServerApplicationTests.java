package com.example.fx.ai.assistant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FixServerApplicationTests {

//	@Test
//	public void testFixClientServerInteraction() throws InterruptedException {
//		FixSession fixSession = new FixSession("localhost", 9878);
//		fixSession.connect();
//
//		OrderManagementServiceFIX oms = new OrderManagementServiceFIX(fixSession);
//
//		// Create a FIX order
//		FixOrder fixOrder = new FixOrder("ORD001", "EUR/USD", 100, "LIMIT", 1.1234);
//		oms.submitOrder(fixOrder);
//
//		// Register a listener to get notified when the order completes
//		fixOrder.setOrderCompletionListener(order ->
//				log.info("[Order Completed] " + order.getOrderId() + " status: " + order.getStatus()));
//
//		// Keep the test running to simulate async message flow
//		Thread.sleep(15000);
//
//		fixSession.close();
//	}
}