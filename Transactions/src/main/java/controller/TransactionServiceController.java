package controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Long>> typeToIds = new ConcurrentHashMap<>();

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void putTransaction(
            @PathVariable("id") Long id,
            @RequestBody Transaction transaction) {
        System.out.println("Adding transaction with id: " + id);

        transactions.put(id, transaction);

        final String type = transaction.getType();
        List<Long> ids = typeToIds.get(type);
        if (ids == null) {
            ids = new ArrayList<>();
            typeToIds.put(type, ids);
        }
        ids.add(id);
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
    public List<Long> getIdsByType(@PathVariable("type") String type) {
        System.out.println("Returning list of transaction ids for type: " + type);
        return typeToIds.get(type);
    }

    @RequestMapping(value = "/sum/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Double getSumAmountByParentIds(@PathVariable("id") Long id) {
        System.out.println("Returning sum amount transitively for id: " + id);
        double sum = 0.0;
        Transaction transaction;
        while (null != id && null != (transaction = transactions.get(id))) {
            sum += transaction.getAmount();
            id = transaction.getParentId();
        }
        return sum;
    }
}
