package order.subsystem;

import order.model.CartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();

    public void addStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative");
        }
        stock.put(productId, stock.getOrDefault(productId, 0) + quantity);
    }

    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public boolean isAvailable(String productId, int quantity) {
        return getStock(productId) >= quantity;
    }

    public boolean reserveStock(List<CartItem> items) {
        for (CartItem item : items) {
            if (!isAvailable(item.getProductId(), item.getQuantity())) {
                return false;
            }
        }
        for (CartItem item : items) {
            int current = stock.get(item.getProductId());
            stock.put(item.getProductId(), current - item.getQuantity());
        }
        return true;
    }

    public void releaseStock(List<CartItem> items) {
        for (CartItem item : items) {
            int current = stock.getOrDefault(item.getProductId(), 0);
            stock.put(item.getProductId(), current + item.getQuantity());
        }
    }
}
