package order.subsystem;

import order.model.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShippingService {
    private final Map<String, String> activeShipments = new HashMap<>();

    public String shipOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        activeShipments.put(trackingNumber, order.getOrderId());
        System.out.printf("[ShippingService] Order %s dispatched with tracking %s to %s%n",
                order.getOrderId(), trackingNumber, order.getCustomer().getAddress());
        return trackingNumber;
    }

    public boolean cancelShipment(String trackingNumber) {
        if (trackingNumber == null || !activeShipments.containsKey(trackingNumber)) {
            return false;
        }
        String orderId = activeShipments.remove(trackingNumber);
        System.out.printf("[ShippingService] Shipment cancelled for tracking %s (Order %s)%n",
                trackingNumber, orderId);
        return true;
    }

    public boolean isShipmentActive(String trackingNumber) {
        return activeShipments.containsKey(trackingNumber);
    }
}
