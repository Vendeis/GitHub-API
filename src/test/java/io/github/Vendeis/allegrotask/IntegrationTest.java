package io.github.Vendeis.allegrotask;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Value("${GITHUB_USERNAME}")
    String githubUsername;
    @Value("${GITHUB_TOKEN}")
    String token;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    TestRestTemplate restTemplate;
    String basicAuth;
    HttpHeaders headers;
    HttpEntity<String> request;

    @BeforeEach
    void setUpBasicAuth(){
        String usernameColonPassword = githubUsername + ":" + token;
        basicAuth = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        headers.set("Authorization", token);
        request = new HttpEntity<>(headers);
    }


    @Test
    void shouldGetAllRepos() throws Exception{
        String username = "Vendeis";

        //get repos using application's endpoint
        MvcResult mvcResult = mockMvc.perform(get("/repo/list/" + username)
                .header("Authorization",basicAuth))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Repo[] repos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Repo[].class);

        //get amount of repositories of a given user using GitHub api
        ResponseEntity<User> result = restTemplate.exchange("https://api.github.com/users/" + username, HttpMethod.GET, request, User.class);

        Assertions.assertEquals(result.getBody().getPublic_repos(), repos.length);
    }
    @Test
    void shouldReturnErrorWhenNonExistingUser() throws Exception{
        //non-existing user
        String username = "!";

        MvcResult mvcResult = mockMvc.perform(get("/repo/list/" + username)
                .header("Authorization",basicAuth))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        Assertions.assertEquals("User " + username + " was not found!", mvcResult.getResponse().getContentAsString());
    }
    @Test
    void shouldCountStargazers() throws Exception {
        String username = "Vendeis";

        //get stargazerCount using application's endpoint
        MvcResult mvcResult = mockMvc.perform(get("/repo/rating/" + username)
                .header("Authorization",basicAuth))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Integer stargazerCount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Integer.class);

        Assertions.assertEquals(stargazerCount,0);
    }
}
