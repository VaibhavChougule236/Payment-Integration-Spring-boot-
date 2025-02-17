package com.Payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Payment.Model.Orders;
import com.Payment.Services.OrderService;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/orders")
	public String orderpage() {
		return "orders";
		}
@PostMapping(value="/createOrder", produces="application/json")
@ResponseBody
@Transactional
public ResponseEntity<Orders> createorder(@RequestBody Orders orders) throws RazorpayException{
	Orders order=orderService.createOrders(orders);
	return new ResponseEntity<>(order,HttpStatus.CREATED);
}
@PostMapping("/paymentCallback")
public String paymentCallback(@RequestParam Map<String, String> response) throws NotFoundException{
    orderService.updateStatus(response);
    return "success";
}
}
