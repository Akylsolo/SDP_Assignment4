# Assignment 4: Facade Pattern

**Student:** Muratbek Akyl  
**Group:** SE 2523  

## Overview
This project implements the Facade structural design pattern in Java (JDK 17) for an Online Order Processing system (`OrderFacade`).

The `OrderFacade` hides the complexity of coordinating four independent subsystem services:
- `InventoryService`: Checks, reserves, and releases stock.
- `PaymentProcessor`: Charges customer balances and processes refunds.
- `ShippingService`: Creates shipments, issues tracking numbers, and cancels shipments.
- `NotificationService`: Dispatches order confirmations, cancellation notices, and failure alerts.

## Project Structure
```text
src/
├── Main.java
└── order/
    ├── facade/
    │   └── OrderFacade.java
    ├── model/
    │   ├── CartItem.java
    │   ├── Customer.java
    │   ├── Order.java
    │   ├── OrderResult.java
    │   └── OrderStatus.java
    └── subsystem/
        ├── InventoryService.java
        ├── NotificationService.java
        ├── PaymentProcessor.java
        └── ShippingService.java
```

## How to Compile and Run

From the root directory of the project:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

On Windows (PowerShell):
```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out Main
```

## Scenarios Demonstrated
1. **Successful Order Placement:** Stock is reserved, payment is charged, shipment is dispatched with tracking, and confirmation is sent.
2. **Failure with Rollback:** When payment is declined, previously reserved stock is automatically released back to inventory.
3. **Order Cancellation:** Reverses all operations by cancelling shipment, refunding payment, returning stock, and notifying customer.
4. **Direct Subsystem Access:** Demonstrates that subsystem services remain decoupled and accessible for advanced operations (e.g. warehouse stock management).
