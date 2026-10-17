package order.model;

public class Customer {
    private final String id;
    private final String name;
    private final String email;
    private final String address;
    private double balance;

    public Customer(String id, String name, String email, String address, double balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public boolean hasSufficientBalance(double amount) {
        return this.balance >= amount;
    }

    public void deductBalance(double amount) {
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds to deduct " + amount);
        }
        this.balance -= amount;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }
}
