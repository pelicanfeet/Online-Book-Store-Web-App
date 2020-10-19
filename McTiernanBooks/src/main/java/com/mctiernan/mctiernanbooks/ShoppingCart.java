package com.mctiernan.mctiernanbooks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<OrderItem> orderItems;

	public ShoppingCart(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public ShoppingCart() {
		this.orderItems = new ArrayList<>();
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}