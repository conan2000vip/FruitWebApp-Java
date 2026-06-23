package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Order;

public interface OrderService {

	int countTodayOrders();

	List<Order> getNewOrders();

}
