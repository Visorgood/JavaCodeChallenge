package controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import service.Transaction;
import service.TransactionService;

@RestController
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private final TransactionService transactionService = new TransactionService();

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void putTransaction(
            @PathVariable("id") Long id,
            @RequestBody Transaction transaction) {
        System.out.println("Adding transaction with id: " + id);
        transactionService.addTransaction(id, transaction);
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    public Transaction getTransaction(
            @PathVariable("id") Long id) {
        System.out.println("Returning transaction with id: " + id);
        return transactionService.getTransaction(id);
    }

    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    public Set<Long> getIdsByType(@PathVariable("type") String type) {
        System.out.println("Returning list of transaction ids for type: " + type);
        return transactionService.getIdsByType(type);
    }

    @RequestMapping(value = "/sum/{id}", method = RequestMethod.GET)
    public Double getSumAmountByParentIds(@PathVariable("id") Long id) {
        System.out.println("Returning sum amount transitively for id: " + id);
        return transactionService.getSumAmountByParentIds(id);
    }
}
