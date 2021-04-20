package io.github.Vendeis.allegrotask.controller;

import io.github.Vendeis.allegrotask.model.Repo;
import io.github.Vendeis.allegrotask.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
