package controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void putTransaction(
            @PathVariable("id") Long id,
            @RequestBody Transaction transaction) {
        System.out.println("Adding transaction with id: " + id);
        transactions.put(id, transaction);
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Transaction getTransaction(
            @PathVariable("id") Long id) {
        System.out.println("Returning transaction with id: " + id);
        return transactions.get(id);
    }

    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getTransactionsByType(@PathVariable("type") String type) {

    }

    @RequestMapping(value = "/sum/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Double getSumOfTransactionsAmountsByParentId(@PathVariable("id") Long parentId) {

    }
}
