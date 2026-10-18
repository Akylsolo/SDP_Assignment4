package order.subsystem;

import order.model.Customer;

public class PaymentProcessor {
    public boolean processPayment(Customer customer, double amount) {
        if (customer == null || amount <= 0) {
            return false;
        }
        if (!customer.hasSufficientBalance(amount)) {
            System.out.printf("[PaymentProcessor] Payment of $%.2f DECLINED for customer %s (Balance: $%.2f)%n",
                    amount, customer.getName(), customer.getBalance());
            return false;
        }
        customer.deductBalance(amount);
        System.out.printf("[PaymentProcessor] Payment of $%.2f APPROVED for customer %s (Remaining balance: $%.2f)%n",
                amount, customer.getName(), customer.getBalance());
        return true;
    }

    public boolean refundPayment(Customer customer, double amount) {
        if (customer == null || amount <= 0) {
            return false;
        }
        customer.addBalance(amount);
        System.out.printf("[PaymentProcessor] Refund of $%.2f PROCESSED for customer %s (Updated balance: $%.2f)%n",
                amount, customer.getName(), customer.getBalance());
        return true;
    }
}
