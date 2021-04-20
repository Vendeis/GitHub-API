package io.github.Vendeis.allegrotask.service;

import io.github.Vendeis.allegrotask.model.Repo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RepositoryService {

    private String listUrl = "https://api.github.com/users/";
    public List<Repo> listRepositories(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Repo[]> responseEntity =
                restTemplate.getForEntity(listUrl + userId + "/repos", Repo[].class);
        if(responseEntity != null) {
            List<Repo> repoList = Arrays.asList(responseEntity.getBody());
            for (Repo repo : repoList){
                System.out.println(repo);
            }
            return repoList;
        }

        return null;
    }
}
