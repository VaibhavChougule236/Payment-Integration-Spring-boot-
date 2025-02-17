package com.Payment.Services;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.Payment.Model.Orders;
import com.Payment.Repository.OrderRepository;
import com.razorpay.*;

import jakarta.annotation.PostConstruct;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	// Value Annotation is used to take value from application.properties

	@Value("razorPay.key.id")
	private String razorpayId;

	@Value("razorPay.key.secret")
	private String razorpaysecret;

	private RazorpayClient razorpayClient;

	@PostConstruct
	public void init() throws RazorpayException {
		this.razorpayClient = new RazorpayClient(razorpayId, razorpaysecret);
	}

	public Orders createOrders(Orders order) throws RazorpayException {
		JSONObject json = new JSONObject();
		json.put("amount", order.getAmount() * 100);
		json.put("currency", "INR");
		json.put("reciept", order.getEmail());

		Order razorpayOrder = this.razorpayClient.orders.create(json);

		order.setRazorpayOrderId(razorpayOrder.get("id"));
		order.setOrderStatus(razorpayOrder.get("status"));
		if(order==null) {
			System.out.println("It is null");
		}

		return (order!=null)?orderRepository.save(order):null;
	}

	public Orders updateStatus(Map<String, String> map) throws NotFoundException {
		String razorpayId = map.get("razorpay_order_id");
	    Orders order = orderRepository.findByRazorpayOrderId(razorpayId);
	    
	    if (order != null) {
	        order.setOrderStatus("PAYMENT DONE");
	        Orders orders = orderRepository.save(order);
	        return orders;
	    } else {
	        // Handle the case where the order is not found
	        // For example, you can throw a custom exception or return a specific error message
	        throw new NotFoundException();
	    }

    }
}
