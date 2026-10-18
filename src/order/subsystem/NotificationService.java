package order.subsystem;

import order.model.Customer;
import order.model.Order;

public class NotificationService {
    public void sendOrderConfirmation(Customer customer, Order order) {
        System.out.printf("[NotificationService] Email to %s: Order %s confirmed! Total: $%.2f. Tracking: %s%n",
                customer.getEmail(), order.getOrderId(), order.getTotalAmount(), order.getTrackingNumber());
    }

    public void sendOrderFailure(Customer customer, String reason) {
        System.out.printf("[NotificationService] Email to %s: Order processing failed. Reason: %s%n",
                customer.getEmail(), reason);
    }

    public void sendCancellationNotice(Customer customer, String orderId) {
        System.out.printf("[NotificationService] Email to %s: Order %s has been cancelled and refunded successfully.%n",
                customer.getEmail(), orderId);
    }
}
