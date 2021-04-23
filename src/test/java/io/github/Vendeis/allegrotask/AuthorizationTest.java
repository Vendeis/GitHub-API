package io.github.Vendeis.allegrotask;

import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.service.RepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Base64;
import java.util.List;

@SpringBootTest
public class AuthorizationTest {

    @Value("${GITHUB_USERNAME}")
    String githubUsername;
    @Value("${GITHUB_TOKEN}")
    String token;

    RepositoryService repositoryService = new RepositoryService();

    @Test
    void shouldGetUserReposWhenAuthenticated(){
        String usernameColonPassword = githubUsername + ":" + token;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

        List<Repo> list = repositoryService.listRepositories("Vendeis", basicAuth);
        Assertions.assertNotNull(list);
    }
}

