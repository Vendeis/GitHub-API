package io.github.Vendeis.allegrotask.service;


import io.github.Vendeis.allegrotask.model.Repo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RepositoryService {

    private String listUrl = "https://api.github.com/users/";

    public List<Repo> listRepositories(String userId) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        
            ResponseEntity<Repo[]> responseEntity =
                    restTemplate.getForEntity(listUrl + userId + "/repos", Repo[].class);

//        System.out.println(responseEntity.getStatusCode());
//        if(responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)){
//            throw new UserNotFoundException(HttpStatus.NOT_FOUND);
//        }
//        List<Repo> repoList = Arrays.asList(responseEntity.getBody());
//        for (Repo repo : repoList) {
//            System.out.println(repo);
//        }
//        return repoList;

return null;
    }
}

