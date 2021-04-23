package io.github.Vendeis.allegrotask.service;


import io.github.Vendeis.allegrotask.exception.UserNotFoundException;
import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static String url = "https://api.github.com/users/";
    private final static Logger logger = LoggerFactory.getLogger(RepositoryService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public RepositoryService(){
    }

    public List<Repo> listRepositories(String username, String token)  {
        List<Repo> repos = getUserRepos(username, token);
        logger.info("User " + username + " has following repositories: {}",repos);

        return repos;
    }

    public int countStargazers(String username, String token) {
        int starCount = 0;
        List<Repo> repos = getUserRepos(username, token);

        for(Repo repo : repos){
            starCount += repo.getStargazers_count();
        }

        logger.info("User " + username + " has " + starCount + " stars in total");
        return starCount;
    }

    private List<Repo> getUserRepos(String username, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<User> userResponseEntity = restTemplate.exchange(url + username, HttpMethod.GET, request, User.class);
            ResponseEntity<Repo[]> repoResponseEntity = restTemplate.exchange(url + username + "/repos?per_page=100", HttpMethod.GET, request, Repo[].class);

            return getReposFromGithub(userResponseEntity, repoResponseEntity, username, request);
        }
        catch (HttpClientErrorException exception){
            throw new UserNotFoundException("User " + username + " was not found!", exception);
        }
    }


    private List<Repo> getReposFromGithub(ResponseEntity<User> userResponseEntity, ResponseEntity<Repo[]> repoResponseEntity,
                                          String username, HttpEntity<String> request) {

        //if user has more than 30 repositories, they are divided into pages, it needs to be addressed
        int numberOfRepos = userResponseEntity.getBody().getPublic_repos();

        //we can change the amount of repos per page, but only up to 100/page
        if(numberOfRepos <= 100) {
            logger.info("User " + username + " has " + numberOfRepos + " repositories");
            Repo[] repoArray = repoResponseEntity.getBody();

            if (repoArray != null) {
                return Arrays.asList(repoArray);
            }
        }
        //if user has more than 100 repositories, then we need to traverse through all pages
        else{
            int numberOfPages = (int)Math.ceil((double)numberOfRepos / 100);
            logger.info("User " + username + " has " + numberOfRepos + " repositories on " + numberOfPages + " pages");

            List<Repo> repoList = new ArrayList<>();
            for(int i = 1; i <= numberOfPages; i++){
                repoResponseEntity = restTemplate.exchange(url + username + "/repos?per_page=100&page=" + i, HttpMethod.GET, request, Repo[].class);

                Collections.addAll(repoList,repoResponseEntity.getBody());
            }
            return repoList;
        }
        return null;
    }
}

