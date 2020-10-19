package com.mctiernan.mctiernanbooks;

import java.sql.Date;

import javax.validation.constraints.NotNull;

public class PurchaseOrder {

	@NotNull
	private Integer id;

	private String customerEmail;

	private Date date;

	@NotNull
	private double totalCost;

	private ShoppingCart shoppingCart;
	
	public PurchaseOrder(Integer id, String customerEmail, Date date, double totalCost, ShoppingCart shoppingCart) {
		this.setId(id);
		this.setCustomerEmail(customerEmail);
		this.setDate(date);
		this.setTotalCost(totalCost);
		this.setShoppingCart(shoppingCart);
	}

	public PurchaseOrder() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
}