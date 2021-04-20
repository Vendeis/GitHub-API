package io.github.Vendeis.allegrotask.controller;

import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/repo")
public class RepositoryController {

    final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    @GetMapping("/list/{userId}")
    public List<Repo> listRepositories(@PathVariable String userId){
        return repositoryService.listRepositories(userId);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> userAlreadyExistsException(HttpClientErrorException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
