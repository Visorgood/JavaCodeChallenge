package controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
    public void putTransaction(
            @PathVariable("id") Long id,
            @RequestBody Transaction transaction) {
        transactions.put(id, transaction);
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Transaction getTransaction(
            @PathVariable("id") Long id) {
        return transactions.get(id);
    }
}
