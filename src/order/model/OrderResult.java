package order.model;

public class OrderResult {
    private final boolean success;
    private final String orderId;
    private final String trackingNumber;
    private final String message;

    private OrderResult(boolean success, String orderId, String trackingNumber, String message) {
        this.success = success;
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
        this.message = message;
    }

    public static OrderResult success(String orderId, String trackingNumber, String message) {
        return new OrderResult(true, orderId, trackingNumber, message);
    }

    public static OrderResult failure(String message) {
        return new OrderResult(false, null, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getMessage() {
        return message;
    }
}
