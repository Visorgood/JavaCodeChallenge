package service;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TransactionServiceTest {

    @Test
    public void testAddTransaction() throws Exception {
        final TransactionService transactionService = new TransactionService();
        Assertions.assertThat(transactionService.addTransactionImpl(null, null)).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(null, new Transaction())).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(1L, null)).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(1L, new Transaction(1.0, "type", null))).isTrue();
        Assertions.assertThat(transactionService.addTransactionImpl(1L, new Transaction(1.0, "type", null))).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(2L, new Transaction(1.0, "type", 2L))).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(2L, new Transaction(1.0, "type", 3L))).isFalse();
        Assertions.assertThat(transactionService.addTransactionImpl(2L, new Transaction(1.0, "type", 1L))).isTrue();
    }

    @Test
    public void testGetTransaction() throws Exception {
        final Long id1 = 11L;
        final Transaction transaction1 = new Transaction(150d, "type1", null);
        final Long id2 = 12L;
        final Transaction transaction2 = new Transaction(170d, "type2", id1);
        final Long id3 = 13L;
        final Transaction transaction3 = new Transaction(180d, "type3", id2);

        final TransactionService transactionService = new TransactionService();
        transactionService.addTransactionImpl(id1, transaction1);
        transactionService.addTransactionImpl(id2, transaction2);
        transactionService.addTransactionImpl(id3, transaction3);

        Assertions.assertThat(transactionService.getTransaction(id1)).isEqualTo(transaction1);
        Assertions.assertThat(transactionService.getTransaction(id2)).isEqualTo(transaction2);
        Assertions.assertThat(transactionService.getTransaction(id3)).isEqualTo(transaction3);
    }

    @Test
    public void testGetIdsByType() throws Exception {
        final String type1 = "type1";
        final String type2 = "type2";
        final String type3 = "type3";

        final TransactionService transactionService = new TransactionService();
        transactionService.addTransactionImpl(3L, new Transaction(1.0, type1, null));
        transactionService.addTransactionImpl(4L, new Transaction(1.0, type2, null));
        transactionService.addTransactionImpl(5L, new Transaction(1.0, type3, null));
        transactionService.addTransactionImpl(6L, new Transaction(1.0, type1, null));
        transactionService.addTransactionImpl(7L, new Transaction(1.0, type3, null));
        transactionService.addTransactionImpl(8L, new Transaction(1.0, type1, null));

        Assertions.assertThat(transactionService.getIdsByType(type1)).containsExactly(3L, 6L, 8L);
        Assertions.assertThat(transactionService.getIdsByType(type2)).containsExactly(4L);
        Assertions.assertThat(transactionService.getIdsByType(type3)).containsExactly(5L, 7L);
    }

    @Test
    public void testGetSumAmountByParentIds() throws Exception {
        final Long id1 = 1L;
        final Long id2 = 2L;
        final Long id3 = 3L;
        final Long id4 = 4L;
        final Long id5 = 5L;

        final TransactionService transactionService = new TransactionService();
        transactionService.addTransactionImpl(id1, new Transaction(1.0, "type", null));
        transactionService.addTransactionImpl(id2, new Transaction(5.0, "type", id1));
        transactionService.addTransactionImpl(id3, new Transaction(7.0, "type", id2));
        transactionService.addTransactionImpl(id4, new Transaction(13.0, "type", id1));
        transactionService.addTransactionImpl(id5, new Transaction(21.0, "type", id4));

        Assertions.assertThat(transactionService.getSumAmountByParentIds(id1)).isEqualTo(47.0);
        Assertions.assertThat(transactionService.getSumAmountByParentIds(id2)).isEqualTo(12.0);
        Assertions.assertThat(transactionService.getSumAmountByParentIds(id3)).isEqualTo(7.0);
        Assertions.assertThat(transactionService.getSumAmountByParentIds(id4)).isEqualTo(34.0);
        Assertions.assertThat(transactionService.getSumAmountByParentIds(id5)).isEqualTo(21.0);
    }
}