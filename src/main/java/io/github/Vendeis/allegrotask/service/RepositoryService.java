package io.github.Vendeis.allegrotask.service;


import io.github.Vendeis.allegrotask.exception.UserNotFoundException;
import io.github.Vendeis.allegrotask.model.Repo;
import org.apache.catalina.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            //if user has more than 30 repositories, they are divided into pages, it needs to be addressed

            //if Link Header is not present, then user has <= 30 repos and all of them are in responseEntity's body
            if(!responseEntity.getHeaders().containsKey("Link")) {
                Repo[] repoArray = responseEntity.getBody();
                if (repoArray != null) {
                    return Arrays.asList(repoArray);
                }
            }
            //if Link Header is present it means that user has > 30 repos, we need to traverse through all pages
            else{
                // how a Link header looks like:
                // Link: <https://api.github.com/user/47313/repos?page=2>; rel="next",
                // <https://api.github.com/user/47313/repos?page=2>; rel="last"

                String linkHeader = responseEntity.getHeaders().get("Link").toString();
                int indexOfRelLast = linkHeader.indexOf("rel=\"last\"");
                int lastPageIndex = linkHeader.indexOf("?page=",indexOfRelLast-20);
                int lastPageNumber = Integer.parseInt(linkHeader.substring(lastPageIndex+6, indexOfRelLast-3));

                List<Repo> repoList = new ArrayList<>();
                for(int i=1; i<=lastPageNumber; i++){
                    responseEntity = restTemplate.getForEntity(url + "?page=" + i, Repo[].class);

                    Collections.addAll(repoList,responseEntity.getBody());
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

