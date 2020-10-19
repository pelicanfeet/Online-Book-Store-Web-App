package com.mctiernan.mctiernanbooks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Repository
public class PurchaseOrderDaoImpl implements PurchaseOrderDao {

private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "insert into purchase_order (po_id, customer_email, po_date, total_cost) "
					   	 + "values (?, ?, ?, ?)";
			System.out.println("HERE: " + getMaxPoId()+1);
			purchaseOrder.setId(getMaxPoId()+1);
			jdbcTemplate.update(SQL, purchaseOrder.getId(), purchaseOrder.getCustomerEmail(), purchaseOrder.getDate(),
								purchaseOrder.getTotalCost());
			
			SQL = "insert into order_detail (po_id, line_no, Isbn, quantity) "
				  + "values (?, ?, ?, ?)";
			int size = purchaseOrder.getShoppingCart().getOrderItems().size();
			List<OrderItem> orderItems = purchaseOrder.getShoppingCart().getOrderItems();
			for(int i = 0; i < size; i++) {
				OrderItem item = orderItems.get(i);
				jdbcTemplate.update(SQL, purchaseOrder.getId(), getMaxLineNo()+1, item.getIsbn(), 
									item.getQuantity());
			}

			logger.info("Created Purchase Order po_id = " + purchaseOrder.getId());
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in creating purchase order record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}

		return purchaseOrder;
	}

	@Override
	public List<PurchaseOrder> getAllPurchaseOrders() {
		String SQL = "select * from purchase_order";
		List<PurchaseOrder> purchaseOrders = jdbcTemplate.query(SQL, new PurchaseOrderMapper());
		return purchaseOrders;
	}

	@Override
	public PurchaseOrder getPurchaseOrderById(Integer id) {
		PurchaseOrder purchaseOrder = null;
		try {
			String SQL = "select * from purchase_order where po_id = ?";
			purchaseOrder = jdbcTemplate.queryForObject(SQL, new Object[] { id }, new PurchaseOrderMapper());
			logger.info("Retrieved Purchase Order po_id = " + purchaseOrder.getId());
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
		return purchaseOrder;
	}

	@Override
	public List<OrderDetail> getOrderDetailById(Integer id) {
		List<OrderDetail> orderDetail = new ArrayList<>();
		try {
			String SQL = "select * from order_detail where po_id = ?";
			orderDetail = jdbcTemplate.query(SQL, new Object[] { id }, new OrderDetailMapper());
			logger.info("Retrieved Order Detail po_id = " + orderDetail.get(0).getPurchaseOrderId());
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
		return orderDetail;
	}

	@Override
	public PurchaseOrder deletePurchaseOrderById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getMaxPoId() {
		List<Integer> result = new ArrayList<>();
		try {
			String SQL = "select MAX(po_id) from purchase_order";
			result = jdbcTemplate.query(SQL, new IdMapper());
		}
		catch(EmptyResultDataAccessException e) {
			return 0;
		}
		return result.get(0);
	}
	
	public Integer getMaxLineNo() {
		List<Integer> result = new ArrayList<>();
		try {
			String SQL = "select MAX(line_no) from order_detail";
			result = jdbcTemplate.query(SQL, new LineNoMapper());
		}
		catch(EmptyResultDataAccessException e) {
			return 0;
		}
		return result.get(0);
	}

	class PurchaseOrderMapper implements RowMapper<PurchaseOrder> {
		public PurchaseOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			PurchaseOrder purchaseOrder = new PurchaseOrder();
			purchaseOrder.setId(rs.getInt("po_id"));
			purchaseOrder.setCustomerEmail(rs.getString("customer_email"));
			purchaseOrder.setDate(rs.getDate("po_date"));
			purchaseOrder.setTotalCost(rs.getDouble("total_cost"));
			return purchaseOrder;
		}
	}

	class OrderDetailMapper implements RowMapper<OrderDetail> {
		public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setPurchaseOrderId(rs.getInt("po_id"));
			orderDetail.setLineNumber(rs.getInt("line_no"));
			orderDetail.setIsbn(rs.getString("isbn"));
			orderDetail.setQuantity(rs.getInt("quantity"));
			return orderDetail;
		}
	}

	class IdMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt(1);
		}
	}
	
	class LineNoMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt(1);
		}
	}

}