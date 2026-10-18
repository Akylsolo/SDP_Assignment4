import order.facade.OrderFacade;
import order.model.CartItem;
import order.model.Customer;
import order.model.OrderResult;
import order.subsystem.InventoryService;
import order.subsystem.NotificationService;
import order.subsystem.PaymentProcessor;
import order.subsystem.ShippingService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        PaymentProcessor payment = new PaymentProcessor();
        ShippingService shipping = new ShippingService();
        NotificationService notification = new NotificationService();

        inventory.addStock("PROD-1", 10);
        inventory.addStock("PROD-2", 5);

        OrderFacade orderFacade = new OrderFacade(inventory, payment, shipping, notification);

        Customer customerWithFunds = new Customer("CUST-1", "Muratbek Akyl", "muratbek@example.com", "Dostyk Ave 10, Almaty", 500.0);

        System.out.println("=== SCENARIO 1: Successful Order via Facade ===");
        List<CartItem> cart1 = List.of(
                new CartItem("PROD-1", "Mechanical Keyboard", 2, 75.0),
                new CartItem("PROD-2", "Wireless Mouse", 1, 50.0)
        );

        OrderResult result1 = orderFacade.placeOrder(customerWithFunds, cart1);
        System.out.println("Result Success: " + result1.isSuccess());
        System.out.println("Order ID: " + result1.getOrderId());
        System.out.println("Tracking: " + result1.getTrackingNumber());
        System.out.println("Stock remaining for PROD-1: " + inventory.getStock("PROD-1"));
        System.out.println();

        System.out.println("=== SCENARIO 2: Payment Failure with Rollback ===");
        Customer customerLowFunds = new Customer("CUST-2", "Aidos Sapar", "aidos@example.com", "Abay St 25, Astana", 30.0);
        List<CartItem> cart2 = List.of(
                new CartItem("PROD-1", "Mechanical Keyboard", 1, 75.0)
        );

        System.out.println("Stock of PROD-1 before failed order: " + inventory.getStock("PROD-1"));
        OrderResult result2 = orderFacade.placeOrder(customerLowFunds, cart2);
        System.out.println("Result Success: " + result2.isSuccess());
        System.out.println("Message: " + result2.getMessage());
        System.out.println("Stock of PROD-1 after rollback: " + inventory.getStock("PROD-1"));
        System.out.println();

        System.out.println("=== SCENARIO 3: Order Cancellation via Facade ===");
        System.out.println("Customer balance before cancellation: $" + customerWithFunds.getBalance());
        boolean cancelled = orderFacade.cancelOrder(result1.getOrderId());
        System.out.println("Cancellation status: " + cancelled);
        System.out.println("Customer balance after refund: $" + customerWithFunds.getBalance());
        System.out.println("Stock of PROD-1 after restock: " + inventory.getStock("PROD-1"));
        System.out.println();

        System.out.println("=== SCENARIO 4: Direct Subsystem Usage (Advanced Client) ===");
        inventory.addStock("PROD-1", 10);
        System.out.println("Direct inventory check by warehouse client: " + inventory.getStock("PROD-1"));
    }
}
