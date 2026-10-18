package order.facade;

import order.model.CartItem;
import order.model.Customer;
import order.model.Order;
import order.model.OrderResult;
import order.model.OrderStatus;
import order.subsystem.InventoryService;
import order.subsystem.NotificationService;
import order.subsystem.PaymentProcessor;
import order.subsystem.ShippingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderFacade {
    private final InventoryService inventoryService;
    private final PaymentProcessor paymentProcessor;
    private final ShippingService shippingService;
    private final NotificationService notificationService;
    private final Map<String, Order> orders = new HashMap<>();

    public OrderFacade() {
        this(new InventoryService(), new PaymentProcessor(), new ShippingService(), new NotificationService());
    }

    public OrderFacade(InventoryService inventoryService,
                       PaymentProcessor paymentProcessor,
                       ShippingService shippingService,
                       NotificationService notificationService) {
        this.inventoryService = inventoryService;
        this.paymentProcessor = paymentProcessor;
        this.shippingService = shippingService;
        this.notificationService = notificationService;
    }

    public OrderResult placeOrder(Customer customer, List<CartItem> items) {
        if (customer == null || items == null || items.isEmpty()) {
            return OrderResult.failure("Invalid customer or empty cart items");
        }

        boolean stockReserved = inventoryService.reserveStock(items);
        if (!stockReserved) {
            notificationService.sendOrderFailure(customer, "Insufficient stock for requested items");
            return OrderResult.failure("Insufficient stock for requested items");
        }

        double totalAmount = calculateTotal(items);
        boolean paymentApproved = paymentProcessor.processPayment(customer, totalAmount);
        if (!paymentApproved) {
            inventoryService.releaseStock(items);
            notificationService.sendOrderFailure(customer, "Payment was declined");
            return OrderResult.failure("Payment was declined; reserved stock has been released");
        }

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Order order = new Order(orderId, customer, items, totalAmount);
        order.setStatus(OrderStatus.PAID);

        String trackingNumber = shippingService.shipOrder(order);
        order.setTrackingNumber(trackingNumber);
        order.setStatus(OrderStatus.SHIPPED);

        orders.put(orderId, order);
        notificationService.sendOrderConfirmation(customer, order);

        return OrderResult.success(orderId, trackingNumber, "Order successfully processed and shipped");
    }

    public boolean cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null || order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        if (order.getTrackingNumber() != null) {
            shippingService.cancelShipment(order.getTrackingNumber());
        }

        paymentProcessor.refundPayment(order.getCustomer(), order.getTotalAmount());
        inventoryService.releaseStock(order.getItems());

        order.setStatus(OrderStatus.CANCELLED);
        notificationService.sendCancellationNotice(order.getCustomer(), orderId);

        return true;
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    private double calculateTotal(List<CartItem> items) {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
