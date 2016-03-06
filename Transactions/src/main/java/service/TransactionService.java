package service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TransactionService {

    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<Long>> typeToIds = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Set<Long>> idToChildIds = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Double> idToTotalSum = new ConcurrentHashMap<>();

    @Async
    public void addTransaction(final Long id, final Transaction transaction) {
        transactions.put(id, transaction);

        final String type = transaction.getType();
        Set<Long> ids = typeToIds.get(type);
        if (ids == null) {
            ids = new HashSet<>();
            typeToIds.put(type, ids);
        }
        ids.add(id);

        if (!idToChildIds.containsKey(id)) {
            idToChildIds.put(id, new HashSet<>());
        }

        Set<Long> childIds = idToChildIds.get(transaction.getParentId());
        if (childIds != null) {
            childIds.add(id);
        }
    }

    public Transaction getTransaction(final Long id) {
        return transactions.get(id);
    }

    public Set<Long> getIdsByType(final String type) {
        return typeToIds.get(type);
    }

    public Double getSumAmountByParentIds(final Long id) {
        return idToTotalSum.get(id);
    }
}
