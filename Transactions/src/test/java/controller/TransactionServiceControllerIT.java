package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Set;

import service.Transaction;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TransactionServiceControllerIT {

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testTransactionServiceController() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        final String type1 = "type1";
        final String type2 = "type2";
        final String type3 = "type3";

        final Long id1 = 1L;
        final Long id2 = 2L;
        final Long id3 = 3L;
        final Long id4 = 4L;
        final Long id5 = 5L;
        final Long id6 = 6L;

        final Transaction transaction1 = new Transaction(1.0, type1, null);
        final Transaction transaction2 = new Transaction(5.0, type1, id1);
        final Transaction transaction3 = new Transaction(7.0, type2, id2);
        final Transaction transaction4 = new Transaction(13.0, type3, id1);
        final Transaction transaction5 = new Transaction(21.0, type1, id4);
        final Transaction transaction6 = new Transaction(24.0, type2, null);

        Assertions.assertThat(performPUT(id1, transaction1)).isTrue();
        Assertions.assertThat(performPUT(id2, transaction2)).isTrue();
        Assertions.assertThat(performPUT(id3, transaction3)).isTrue();
        Assertions.assertThat(performPUT(id4, transaction4)).isTrue();
        Assertions.assertThat(performPUT(id5, transaction5)).isTrue();
        Assertions.assertThat(performPUT(id6, transaction6)).isTrue();

        String responseJson = performGETTransaction(id1);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction1);
        responseJson = performGETTransaction(id2);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction2);
        responseJson = performGETTransaction(id3);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction3);
        responseJson = performGETTransaction(id4);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction4);
        responseJson = performGETTransaction(id5);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction5);
        responseJson = performGETTransaction(id6);
        assertTransactionsAreEqual(jsonToTransaction(responseJson), transaction6);

        String idsJson = performGETIdsByType(type1);
        Assertions.assertThat(jsonToIds(idsJson)).containsExactly(id1, id2, id5);
        idsJson = performGETIdsByType(type2);
        Assertions.assertThat(jsonToIds(idsJson)).containsExactly(id3, id6);
        idsJson = performGETIdsByType(type3);
        Assertions.assertThat(jsonToIds(idsJson)).containsExactly(id4);

        String sumJson = performGETSumAmountByParentIds(id1);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(47.0);
        sumJson = performGETSumAmountByParentIds(id2);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(12.0);
        sumJson = performGETSumAmountByParentIds(id3);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(7.0);
        sumJson = performGETSumAmountByParentIds(id4);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(34.0);
        sumJson = performGETSumAmountByParentIds(id5);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(21.0);
        sumJson = performGETSumAmountByParentIds(id6);
        Assertions.assertThat(jsonToSum(sumJson)).isEqualTo(24.0);
    }

    private boolean performPUT(final Long id, final Transaction transaction) throws Exception {
        final MvcResult mvcResult = mockMvc.perform(put("/transactionservice/transaction/{id}", id)
                .content(transactionToJson(transaction))
                .contentType("application/json")
                .accept(contentType)).andReturn();
        return mvcResult.getResponse().getStatus() == 200;
    }

    private String performGETTransaction(final Long id) throws Exception {
        final MvcResult result = mockMvc.perform(get("/transactionservice/transaction/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private String performGETIdsByType(final String type) throws Exception {
        final MvcResult result = mockMvc.perform(get("/transactionservice/types/{type}", type))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private String performGETSumAmountByParentIds(final Long id) throws Exception {
        final MvcResult result = mockMvc.perform(get("/transactionservice/sum/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private String transactionToJson(final Transaction transaction) throws JsonProcessingException {
        return mapper.writeValueAsString(transaction);
    }

    private Transaction jsonToTransaction(final String json) throws IOException {
        return mapper.readValue(json, Transaction.class);
    }

    private Set<Long> jsonToIds(final String json) throws IOException {
        return mapper.readValue(json, new TypeReference<Set<Long>>() {
        });
    }

    private Double jsonToSum(final String json) throws IOException {
        return mapper.readValue(json, Double.class);
    }

    private void assertTransactionsAreEqual(final Transaction t1, final Transaction t2) {
        Assertions.assertThat(t1.getAmount()).isEqualTo(t2.getAmount());
        Assertions.assertThat(t1.getType()).isEqualTo(t2.getType());
        Assertions.assertThat(t1.getParentId()).isEqualTo(t2.getParentId());
    }
}