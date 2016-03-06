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
    private final ConcurrentMap<Long, Double> idToTotalSum = new ConcurrentHashMap<>();

    /**
     * Adds new transaction in asynchronous way.
     */
    @Async
    public synchronized void addTransaction(final Long id, final Transaction transaction) {
        addTransactionImpl(id, transaction);
    }

    /**
     * Adds new transaction.
     *
     * @return True if new transaction was added, false otherwise.
     */
    boolean addTransactionImpl(final Long id, final Transaction transaction) {
        if (null == id || null == transaction) {
            // bad input
            return false;
        }
        if (transactions.containsKey(id)) {
            // transaction already exists
            return false;
        }
        if (transaction.getParentId() != null) {
            if (id.equals(transaction.getParentId())) {
                // bad parent id
                return false;
            }
            if (!transactions.containsKey(transaction.getParentId())) {
                // parent transaction doesn't exist
                return false;
            }
        }

        addNewTransaction(id, transaction);
        assignNewTransactionIdToType(id, transaction);
        updateAllTotalSumsTransitivelyByParentIds(transaction);

        return true;
    }

    private void addNewTransaction(final Long id, final Transaction transaction) {
        transactions.put(id, transaction);
        idToTotalSum.put(id, transaction.getAmount());
    }

    private void assignNewTransactionIdToType(final Long id, final Transaction transaction) {
        final String type = transaction.getType();
        Set<Long> ids = typeToIds.get(type);
        if (ids == null) {
            ids = new HashSet<>();
            typeToIds.put(type, ids);
        }
        ids.add(id);
    }

    private void updateAllTotalSumsTransitivelyByParentIds(final Transaction transaction) {
        final Double amount = transaction.getAmount();
        Long id = transaction.getParentId();
        Transaction t;
        while (null != id && null != (t = transactions.get(id))) {
            idToTotalSum.put(id, idToTotalSum.get(id) + amount);
            id = t.getParentId();
        }
    }

    /**
     * Returns transaction given transaction id.
     *
     * @return Transaction or null if transaction with such id wasn't added before.
     */
    public Transaction getTransaction(final Long id) {
        return transactions.get(id);
    }

    /**
     * Returns set of transaction ids corresponding to given type.
     *
     * @return Set of transaction ids or empty set if given type hasn't appeared yet.
     */
    public Set<Long> getIdsByType(final String type) {
        final Set<Long> ids = typeToIds.get(type);
        return ids != null ? ids : new HashSet<>();
    }

    /**
     * Returns total amount of all transactions transitively linked by parent id to a transaction
     * with given id.
     */
    public Double getSumAmountByParentIds(final Long id) {
        final Double sum = idToTotalSum.get(id);
        return sum != null ? sum : 0.0;
    }
}
