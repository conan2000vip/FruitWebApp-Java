package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public void createOrder(Order order) {
		orderRepository.save(order);
	}

	@Override
	// 今日の注文数をカウントするメソッドの実装
	public int countTodayOrders() {
		return (int) orderRepository.count();
	}

	// Spring がリポジトリを注入するための setter
	public void setOrderRepository(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public List<Order> getNewOrders() {
		return orderRepository.findByOrderStatus("新規");
	}

}
