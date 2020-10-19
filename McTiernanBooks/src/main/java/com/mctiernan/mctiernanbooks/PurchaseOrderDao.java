package com.mctiernan.mctiernanbooks;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderDao {
	public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);
	public List<PurchaseOrder> getAllPurchaseOrders();
	public PurchaseOrder getPurchaseOrderById(Integer id);
	public PurchaseOrder deletePurchaseOrderById(Integer id);
	public List<OrderDetail> getOrderDetailById(Integer po_id);
}