package com.ecobill.ecobill.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class InvoiceControllerTests {

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @Autowired
        public InvoiceControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {

                this.mockMvc = mockMvc;
                this.objectMapper = objectMapper;

        }

        @Test
        public void testThatCreateInvoiceSuccessfullyReturnsHttp201Created() throws Exception {
                String jsonString = "{\n" +
                                "    \"subscription\": {\n" +
                                "        \"subscription_number\": 5546889\n" +
                                "    },\n" +
                                "    \"epr\": {\n" +
                                "        \"commercial_register\": 123564,\n" +
                                "        \"tax_number\": 55555555,\n" +
                                "        \"name\": \"test\"\n" +
                                "    },\n" +
                                "    \"customer\": {\n" +
                                "        \"phone_number\": 966536922003\n" +
                                "    },\n" +
                                "    \"invoice\": {\n" +
                                "        \"qr_code\": 553,\n" +
                                "        \"created_at\": \"2024-04-05\",\n" +
                                "        \"total_amount\": 150.00\n" +
                                "    },\n" +
                                "    \"invoice_items\": [\n" +
                                "        {\n" +
                                "            \"name\": \"Product A\",\n" +
                                "            \"price\": 50.00,\n" +
                                "            \"quantity\": 2\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"name\": \"Product B\",\n" +
                                "            \"price\": 25.00,\n" +
                                "            \"quantity\": 3\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}";

                mockMvc.perform(
                                MockMvcRequestBuilders.post("/invoices")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonString))
                                .andExpect(
                                                MockMvcResultMatchers.status().isCreated());

        }
}
