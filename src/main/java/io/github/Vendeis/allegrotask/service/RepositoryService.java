package io.github.Vendeis.allegrotask.service;


import io.github.Vendeis.allegrotask.exception.UserNotFoundException;
import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RepositoryService {

    private final String listUrl = "https://api.github.com/users/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Repo> listRepositories(String username, String token)  {

        List<Repo> repos = getReposFromGitHub(username, token);
        return repos;
    }

    public int countStargazers(String username, String token) {
        int starCount = 0;
        List<Repo> repos = getReposFromGitHub(username, token);

        for(Repo repo : repos){
            starCount += repo.getStargazers_count();
        }
        return starCount;
    }

    public List<Repo> getReposFromGitHub(String username, String token){
        String url = listUrl + username;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<User> userResponseEntity = restTemplate.exchange(url, HttpMethod.GET, request, User.class);
            ResponseEntity<Repo[]> repoResponseEntity = restTemplate.exchange(url + "/repos?per_page=100", HttpMethod.GET, request, Repo[].class);

            int numberOfRepos = userResponseEntity.getBody().getPublic_repos();

            //if user has more than 30 repositories, they are divided into pages, it needs to be addressed
            //we can change the amount of repos per page, but only up to 100/page

            //if the amount of user's repositories does not exceed 100, they will all fit into one page
            if(numberOfRepos<=100) {
                Repo[] repoArray = repoResponseEntity.getBody();
                if (repoArray != null) {
                    return Arrays.asList(repoArray);
                }
            }
            //if user has more than 100 repositories, then we need to traverse through all pages
            else{
                int numberOfPages = (int)Math.ceil((double)numberOfRepos/100);

                List<Repo> repoList = new ArrayList<>();
                for(int i=1; i<=numberOfPages; i++){
                    repoResponseEntity = restTemplate.exchange(url + "/repos?per_page=100&page=" + i, HttpMethod.GET, request, Repo[].class);

                    Collections.addAll(repoList,repoResponseEntity.getBody());
                }
                return repoList;
            }
        }
        catch (HttpClientErrorException exception){
            throw new UserNotFoundException("User " + username + " was not found!", exception);
        }
        return null;
    }
}

