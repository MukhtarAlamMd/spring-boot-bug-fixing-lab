
package com.practice.buglab;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String validOrderJson() {
        return """
                {
                  "customerEmail": "john.doe@example.com",
                  "productName": "Wireless Mouse",
                  "quantity": 3,
                  "unitPrice": 24.99
                }
                """;
    }

    private long createOrder() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderJson()))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(
                result.getResponse().getContentAsString());

        return response.get("id").asLong();
    }

    @Test
    void validOrderShouldReturn201() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void invalidEmailShouldReturn400() throws Exception {
        String invalidJson = """
                {
                  "customerEmail": "invalid-email",
                  "productName": "Keyboard",
                  "quantity": 2,
                  "unitPrice": 49.99
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shippingCancelledOrderShouldReturn409() throws Exception {
        long id = createOrder();

        mockMvc.perform(patch("/api/orders/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        mockMvc.perform(patch("/api/orders/{id}/ship", id))
                .andExpect(status().isConflict());
    }

    @Test
    void cancellingMissingOrderShouldReturn404() throws Exception {
        mockMvc.perform(patch("/api/orders/{id}/cancel", 999999L))
                .andExpect(status().isNotFound());
    }
}
