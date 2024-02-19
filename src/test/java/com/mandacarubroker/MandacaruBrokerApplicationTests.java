package com.mandacarubroker;

import com.mandacarubroker.domain.stock.RequestStockDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class MandacaruBrokerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("GET all stocks")
	void testGetStocks() throws Exception {
		mockMvc.perform(get("/stocks")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	@DisplayName("GET stock by id")
	void testGetStockById() throws Exception {
		String id = "04e05485-2f81-4016-a77c-b01163a01991";
		String expectedJson = "{\"id\":\"04e05485-2f81-4016-a77c-b01163a01991\",\"symbol\":\"BBA3\",\"companyName\":\"Banco do Brasil SA\",\"price\":55.97}";

		mockMvc.perform(get("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedJson));
	}

	@Test
	@DisplayName("GET stock by non existing id")
	void testGetNonExistingStockById() throws Exception {
		String nonExistingId = "04e05485-2f81-4016-a77c-b01163a01900";

		mockMvc.perform(get("/stocks/{id}", nonExistingId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string(emptyOrNullString()));
	}

	@Test
	@DisplayName("POST a new stock")
	void testCreateStock() throws Exception {
		RequestStockDTO requestStockDTO = new RequestStockDTO("APL0", "Apple Inc.", 150.25);
		String requestJson = objectMapper.writeValueAsString(requestStockDTO);

		mockMvc.perform(post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.symbol").value("APL0"))
				.andExpect(jsonPath("$.companyName").value("Apple Inc."))
				.andExpect(jsonPath("$.price").value(150.25));
	}

	@Test
	@DisplayName("Update stock by id")
	void testUpdateStock() throws Exception {
		String id = "84636132-beaa-46fb-b1e9-10ad483080a3";

		RequestStockDTO updatedStockDTO = new RequestStockDTO("APL1", "Apple Inc.", 155.78);

		String requestJson = objectMapper.writeValueAsString(updatedStockDTO);

		mockMvc.perform(put("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.symbol").value("APL1"))
				.andExpect(jsonPath("$.companyName").value("Apple Inc."))
				.andExpect(jsonPath("$.price").value(155.78));
	}

	@Test
	@DisplayName("DELETE stock by id")
	void testDeleteStockById() throws Exception {
		mockMvc.perform(delete("/stocks/{id}", "04e05485-2f81-4016-a77c-b01163a01991")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("DELETE stock by invalid id")
	void testDeleteStockByInvalidId() throws Exception {
		String id = "04e05485-2f81-4016-a77c-b01163a01992";
		MockHttpServletResponse response = mockMvc.perform(delete("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Stock not found with id: " + id));

	}

	@Test
	@DisplayName("POST stock with invalid symbol")
	void testCreateStockWithInvalidSymbol() throws Exception {

		RequestStockDTO requestStockDTO = new RequestStockDTO("AMER1", "Lojas Americanas", 150.25);

		String requestJson = objectMapper.writeValueAsString(requestStockDTO);

		MockHttpServletResponse response = mockMvc.perform(post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Symbol must be 3 letters followed by 1 number"));
	}

	@Test
	@DisplayName("POST stock without company name")
	void testCreateStockWithoutCompanyName() throws Exception {

		RequestStockDTO requestStockDTO = new RequestStockDTO("AMR1", "", 27.2);

		String requestJson = objectMapper.writeValueAsString(requestStockDTO);

		MockHttpServletResponse response = mockMvc.perform(post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Company name cannot be blank"));
	}

	@Test
	@DisplayName("POST stock with negative price")
	void testCreateStockWithNegativePrice() throws Exception {

		RequestStockDTO requestStockDTO = new RequestStockDTO("MGL1", "Magazine Luiza", -25.2);

		String requestJson = objectMapper.writeValueAsString(requestStockDTO);

		MockHttpServletResponse response = mockMvc.perform(post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Price must be positive"));
	}

	@Test
	@DisplayName("Update stock with invalid id")
	void testUpdateStockWithInvalidId() throws Exception {
		String id = "83049289-oapy-93id-k9w4-93vj83000000";

		RequestStockDTO updatedStockDTO = new RequestStockDTO("MIL3", "123 Milhas", 42.18);

		String requestJson = objectMapper.writeValueAsString(updatedStockDTO);

		MockHttpServletResponse response = mockMvc.perform(put("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Stock not found with id: " + id));

	}

	@Test
	@DisplayName("Update stock with invalid symbol")
	void testUpdateStockWithInvalidSymbol() throws Exception {
		String id = "71dc4bff-20c2-4399-a116-a1445e72acd2";

		RequestStockDTO updatedStockDTO = new RequestStockDTO("BBRA", "Banco do Brasil", 77.29);

		String requestJson = objectMapper.writeValueAsString(updatedStockDTO);

		MockHttpServletResponse response = mockMvc.perform(put("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Symbol must be 3 letters followed by 1 number"));

	}

	@Test
	@DisplayName("Update stock without company name")
	void testUpdateStockWithoutCompanyName() throws Exception {
		String id = "71dc4bff-20c2-4399-a116-a1445e72acd2";

		RequestStockDTO updatedStockDTO = new RequestStockDTO("BBR4", "", 77.29);

		String requestJson = objectMapper.writeValueAsString(updatedStockDTO);

		MockHttpServletResponse response = mockMvc.perform(put("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Company name cannot be blank"));

	}

	@Test
	@DisplayName("Update stock with negative price")
	void testUpdateStockWithNegativePrice() throws Exception {
		String id = "71dc4bff-20c2-4399-a116-a1445e72acd2";

		RequestStockDTO updatedStockDTO = new RequestStockDTO("BBR4", "Banco do Brasil", -70.99);

		String requestJson = objectMapper.writeValueAsString(updatedStockDTO);

		MockHttpServletResponse response = mockMvc.perform(put("/stocks/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse();

		String errorMessage = response.getErrorMessage();
		assertTrue(errorMessage.contains("Price must be positive"));

	}

}
