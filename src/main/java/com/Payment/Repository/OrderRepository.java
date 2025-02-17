package com.Payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Payment.Model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	Orders findByRazorpayOrderId(String razorpayId);

}
