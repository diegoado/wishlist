package com.mychallenge.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mychallenge.wishlist.infrastructure.persistence.dao.WishlistMongoDBDao;
import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WishlistSteps {

    private static final String baseUrl = "/wishlist/";

    private final MockMvc mockMvc;

    private final WishlistMongoDBDao wishlistMongoDBDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ResultActions resultActions;

    private WishlistMongoDBEntity wishlist;

    public WishlistSteps(
        WebApplicationContext applicationContext,
        WishlistMongoDBDao wishlistMongoDBDao
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        this.wishlistMongoDBDao = wishlistMongoDBDao;
    }

    @Given("Exist a client {string} wishlist")
    public void exist_client_wishlist(String client, DataTable dataTable) {
        wishlist = new WishlistMongoDBEntity(
            client,
            dataTable.asList().stream().map(ProductMongoDBEntity::new).toList()
        );
        wishlistMongoDBDao.save(wishlist);
    }

    @When("I make a request to verify if client {string} wishlist has product {string}")
    public void i_make_a_request_to_verify_if_wishlist_has_product(String client, String product) throws Exception {
        var path = baseUrl + client + "/products/" + product + "/check";
        resultActions = mockMvc.perform(get(path));
    }

    @When("I make a request to get client {string} wishlist")
    public void i_make_a_request_to_get_client_wishlist(String client) throws Exception {
        var path = baseUrl + client;
        resultActions = mockMvc.perform(get(path));
    }

    @When("I make a request to upsert product in client {string} wishlist")
    public void i_make_a_request_to_upsert_product_client_wishlist(
        String client,
        DataTable dataTable
    ) throws Exception {
        var path = baseUrl + client;
        var body = dataTable.entries().stream().findFirst().orElseThrow();

        resultActions = mockMvc.perform(
            put(path).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body))
        );
    }


    @When("I make a request to delete a product {string} from client {string} wishlist")
    public void i_make_a_request_to_delete_a_product_from_client_wishlist(
        String product,
        String client
    ) throws Exception {
        var path = baseUrl + client + "/products/" + product;
        resultActions = mockMvc.perform(delete(path).contentType(MediaType.APPLICATION_JSON));
    }

    @Then("API status code must be {int}")
    public void api_status_code_must_be(int status) throws Exception {
        resultActions.andExpect(status().is(status));
    }

    @Then("Wishlist must not contain the product")
    public void wishlist_must_not_contain_the_product() throws Exception {
        resultActions.andExpect(jsonPath("$.data", is(false)));
    }

    @Then("Wishlist must contain the product")
    public void wishlist_must_contain_the_product() throws Exception {
        resultActions.andExpect(jsonPath("$.data", is(true)));
    }

    @Then("Returned wishlist contains the products")
    public void returned_wishlist_contains_the_products(DataTable dataTable) throws Exception {
        for (String product : dataTable.asList()) {
            resultActions.andExpect(
                jsonPath("$.data.products", hasItem(Collections.singletonMap("name", product)))
            );
        }
    }

    @Then("Stored wishlist must not contain the product {string}")
    public void stored_wishlist_must_not_contain_the_product(String product) {
        assertFalse(wishlistMongoDBDao.existsByIdAndProductNameEqualsTo(wishlist.id(), product));
    }

    @Then("Stored wishlist must contain the product {string}")
    public void stored_wishlist_must_contain_the_product(String product) {
        assertTrue(wishlistMongoDBDao.existsByIdAndProductNameEqualsTo(wishlist.id(), product));
    }
}
