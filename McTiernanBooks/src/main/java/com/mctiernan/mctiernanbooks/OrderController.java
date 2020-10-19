package com.mctiernan.mctiernanbooks;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

	@Autowired
	private PurchaseOrderDao purchaseOrderDao = new PurchaseOrderDaoImpl();

	@Autowired
	private BookDao bookDao = new BookDaoImpl();

	@Autowired
	private UserDao userDao = new UserDaoImpl();

	@GetMapping("/order")
	public String order(OrderItem orderItem, Model model, HttpSession session) {
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("SHOPPINGCART");
		if(shoppingCart != null) {
			List<OrderItem> orderItems = shoppingCart.getOrderItems();
			model.addAttribute("orderItems", orderItems);
		}
		return "order";
	}

	@PostMapping("/addToShoppingCart")
	public String addToShoppingCart(@Valid OrderItem orderItem, BindingResult bindingResult,
									HttpServletRequest request, Model model) {
		Book book = bookDao.getBookByIsbn(orderItem.getIsbn());
		if(book != null && orderItem.getQuantity() > 0) {
			ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("SHOPPINGCART");
			if(shoppingCart == null) {
				shoppingCart = new ShoppingCart();
			}

			Double totalCost = (Double) request.getSession().getAttribute("TOTALCOST");
			if(totalCost == null) {
				totalCost = 0.0;
			}
			totalCost += (book.getCost() * orderItem.getQuantity());
			request.getSession().setAttribute("TOTALCOST", totalCost);

			shoppingCart.getOrderItems().add(orderItem);
			request.getSession().setAttribute("SHOPPINGCART", shoppingCart);

			return "redirect:/book/all";
		}
		else if(book == null) {
			model.addAttribute("books", bookDao.getAllBooks());
			model.addAttribute("bookWithIsbnDoesNotExistError", "Book with entered ISBN does not exist!");
			if(orderItem.getQuantity() <= 0) {
				model.addAttribute("invalidQuantityError", "Quantity must be greater than 0!");
			}
			return "books";
		}
		else {
			model.addAttribute("books", bookDao.getAllBooks());
			model.addAttribute("invalidQuantityError", "Quantity must be greater than 0!");
			return "books";
		}
	}

	@PostMapping("/emptyShoppingCart")
	public String emptyShoppingCart(HttpServletRequest request) {
		request.getSession().removeAttribute("SHOPPINGCART");
		request.getSession().removeAttribute("TOTALCOST");
		return "redirect:/order";
	}

	@PostMapping("/placeOrder")
	public String placeOrder(@Valid PurchaseOrder purchaseOrder, BindingResult result,
							 HttpServletRequest request, Principal principal, Model model) {
		ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("SHOPPINGCART");
		purchaseOrder = new PurchaseOrder();
		purchaseOrder.setShoppingCart(shoppingCart);
		purchaseOrder.setDate(getDate());

		User user = userDao.getUserByEmail(principal.getName());
		purchaseOrder.setCustomerEmail(user.getEmail()); 
		Double totalCost = (Double) request.getSession().getAttribute("TOTALCOST");
		purchaseOrder.setTotalCost(totalCost);

		model.addAttribute("purchaseOrder", purchaseOrder);
		purchaseOrder = purchaseOrderDao.createPurchaseOrder(purchaseOrder);
		
		request.getSession().setAttribute("po_id", purchaseOrder.getId());

		for(OrderItem o : shoppingCart.getOrderItems()) {
			o.setPurchaseOrderId(purchaseOrder.getId());
		}

		return "redirect:/confirmation";
	}

	@GetMapping("/confirmation")
	public String confirmation(OrderDetail orderDetail, PurchaseOrder purchaseOrder, Model model, HttpSession session) {
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("SHOPPINGCART");
		if(shoppingCart != null) {
			Integer po_id = (Integer) session.getAttribute("po_id");
			List<OrderDetail> confirmItems = purchaseOrderDao.getOrderDetailById(po_id);
			model.addAttribute("confirmItems", confirmItems);
			model.addAttribute("purchaseOrder", purchaseOrderDao.getPurchaseOrderById(po_id));
			session.removeAttribute("SHOPPINGCART");
			session.removeAttribute("TOTALCOST");
		}
		else {
			model.addAttribute("emptyCartError", "Shopping cart is empty!");
			return "order";
		}
		return "confirmation";
	}

	@GetMapping("/purchaseOrder/detail")
	public String displayOrderDetailById(@RequestParam(value = "po_id") Integer po_id, Model model) {
		model.addAttribute("po_id", po_id);
		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrderById(po_id);
		List<OrderDetail> orderDetail = purchaseOrderDao.getOrderDetailById(po_id);
		model.addAttribute("purchaseOrder", purchaseOrder);
		model.addAttribute("orderDetail", orderDetail);
		if(purchaseOrderDao.getPurchaseOrderById(po_id) == null)
			return "redirect:/purchaseOrder/all";
		return "orderDetails";
	}

	@GetMapping("/purchaseOrder/all")
	public String displayPurchaseOrders(Model model) {
		model.addAttribute("purchaseOrders", purchaseOrderDao.getAllPurchaseOrders());
		return "purchaseOrders";
	}

	private static Date getDate() {
		java.util.Date utilDate = new java.util.Date();
		return new Date(utilDate.getTime());
	}
}