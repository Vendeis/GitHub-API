package io.github.Vendeis.allegrotask.service;


import io.github.Vendeis.allegrotask.exception.UserNotFoundException;
import io.github.Vendeis.allegrotask.model.Repo;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RepositoryService {

    private final String listUrl = "https://api.github.com/users/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Repo> listRepositories(String username)  {

        List<Repo> repos = getReposFromGitHub(username);
        return repos;
    }

    public int countStargazers(String username) {
        int starCount = 0;
        List<Repo> repos = getReposFromGitHub(username);

        for(Repo repo : repos){
            starCount += repo.getStargazers_count();
        }
        return starCount;
    }

    public List<Repo> getReposFromGitHub(String username){
        String url = listUrl + username + "/repos";
        try {
            ResponseEntity<Repo[]> responseEntity =
                    restTemplate.getForEntity(url, Repo[].class);
            System.out.println(responseEntity.getHeaders());
            Repo[] repoArray = responseEntity.getBody();

            if(repoArray != null) {
                return Arrays.asList(repoArray);
            }
        }
        catch (HttpClientErrorException exception){
            throw new UserNotFoundException("User " + username + " was not found!", exception);
        }
        return null;
    }
}

