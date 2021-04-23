package io.github.Vendeis.allegrotask.model;

public class Repo {
    private String name;
    private int stargazers_count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "name='" + name + '\'' +
                ", stargazers_count=" + stargazers_count +
                '}';
    }
}
