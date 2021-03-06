package io.github.Vendeis.allegrotask.controller;

import io.github.Vendeis.allegrotask.exception.UserNotFoundException;
import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repo")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    @GetMapping("/list/{username}")
    public List<Repo> listRepositories(@PathVariable String username, @RequestHeader("Authorization") String token){
        return repositoryService.listRepositories(username, token);
    }
    @GetMapping("/rating/{username}")
    public int countStargazers(@PathVariable String username, @RequestHeader("Authorization") String token){
        return repositoryService.countStargazers(username, token);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundException(UserNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
