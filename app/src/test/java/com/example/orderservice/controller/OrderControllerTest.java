package com.example.orderservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllOrders_returnsOk() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getOrderById_existingOrder_returnsOk() throws Exception {
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice Johnson"));
    }

    @Test
    void getOrderById_nonExistent_returns404() throws Exception {
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_validOrder_returns201() throws Exception {
        String json = """
                {
                    "customerName": "Test User",
                    "customerEmail": "test@example.com",
                    "productName": "Test Product",
                    "quantity": 1,
                    "unitPrice": 19.99
                }
                """;
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void cancelOrder_pendingOrder_succeeds() throws Exception {
        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelOrder_shippedOrder_fails() throws Exception {
        mockMvc.perform(post("/api/orders/3/cancel"))
                .andExpect(status().isBadRequest());
    }
}
