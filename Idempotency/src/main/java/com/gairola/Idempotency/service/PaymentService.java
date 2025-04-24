package com.gairola.Idempotency.service;

import com.gairola.Idempotency.model.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final Map<String, String> idempotencyStore = new ConcurrentHashMap<>();

    public String processPayment(PaymentRequest request) {
        String key = request.getIdempotencyKey();

        if (idempotencyStore.containsKey(key)) {
            return "Already processed: " + idempotencyStore.get(key);
        }

        String result = "Payment of $" + request.getAmount() + " by " + request.getUserId() + " processed.";
        idempotencyStore.put(key, result);

        return result;
    }
}
