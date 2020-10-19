package com.mctiernan.mctiernanbooks;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private int purchaseOrderId;

	@NotNull
	private int lineNumber;

	@NotNull
	@NotEmpty
	private String isbn;

	@NotNull
	@Min(1)
	private int quantity;

	public OrderItem(int purchaseOrderId, int lineNumber, String isbn, int quantity) {
		this.setPurchaseOrderId(purchaseOrderId);
		this.setLineNumber(lineNumber);
		this.setIsbn(isbn);
		this.setQuantity(quantity);
	}

	public OrderItem() {}

	public int getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(int purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}