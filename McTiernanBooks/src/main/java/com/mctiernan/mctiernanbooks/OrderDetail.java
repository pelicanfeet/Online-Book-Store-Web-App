package com.mctiernan.mctiernanbooks;

public class OrderDetail {

	private Integer purchaseOrderId;

	private Integer lineNumber;

	private String isbn;

	private int quantity;

	public OrderDetail(Integer purchaseOrderId, Integer lineNumber, String isbn, int quantity) {
		this.setPurchaseOrderId(purchaseOrderId);
		this.setLineNumber(lineNumber);
		this.setIsbn(isbn);
		this.setQuantity(quantity);
	}

	public OrderDetail() {}

	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
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