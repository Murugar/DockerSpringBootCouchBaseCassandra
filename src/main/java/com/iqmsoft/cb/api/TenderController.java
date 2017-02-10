package com.iqmsoft.cb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iqmsoft.cb.api.errors.InvalidPaymentException;
import com.iqmsoft.cb.api.errors.OrderNotFoundException;
import com.iqmsoft.cb.core.DataStore;
import com.iqmsoft.cb.core.Order;
import com.iqmsoft.cb.core.OrderTotal;
import com.iqmsoft.cb.core.Payment;

import java.util.Map;

import static com.iqmsoft.cb.api.OrderController.exitIfOrderIsEmpty;
import static java.util.Collections.singletonMap;


@RestController
@RequestMapping("/tender")
public class TenderController {
    @Autowired
    private DataStore data;

    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Map<String, Integer> pay(@RequestBody Payment payment) {
        exitIfInvalidPaymentAmount(payment.amount);
        Order order = getOrderOrExit(payment.orderId);
        exitIfOrderIsEmpty(order);
        // get the balance BEFORE writing the payment to the database
        // then we know it is not included in the total paid returned by the DataStore
        int balance = getRemainingBalance(order) - payment.amount;
        data.createPayment(payment);
        return singletonMap("remaining", balance);
    }

    private static void exitIfInvalidPaymentAmount(int amount) {
        if (amount < 1) {
            throw new InvalidPaymentException("amount was " + amount + " but must be > 0");
        }
    }

    private Order getOrderOrExit(String id) {
        Order order = data.getOrder(id);
        if (order == null) {
            throw new OrderNotFoundException("order " + id + " not found");
        }
        return order;
    }

    private int getRemainingBalance(Order order) {
        OrderTotal total = order.getTotal();
        return total.subtotal + total.tax - data.getTotalPaid(order.getId());
    }
}

