#  Task number 3

## Table of contents
* [Setup](#setup)
* [Sending API requests](#sending-API-requests)
* [Possible development directions](#possible-development-directions)


## Setup

* clone the repository
* navigate to src/main/resources/application.properties and fill in your GITHUB_USERNAME & GITHUB_TOKEN<br/>
(explained in "Sending Api requests", it is required for tests to run correctly) 
* navigate to the root folder and build the project using following command:<br/>
mvn clean install
* after building the application run following command to start it:<br/>
java -jar target/allegrotask-1.0.jar

## Sending Api requests

Unauthenticated clients can make up to 60 requests to GitHub API per hour, which is in my opinion insufficient(let's say you want to list repos of a GitHub user that has A LOT of repositories - it will exhaust your hourly limit right away!). Therefore I have implemented authentication via username and github access token, which extends the amount of requests per hour to 5000. 

The application exposes 2 endpoints:<br/>

/list/{username} - returns a list of all repositories, alongside their stargazer count, for a given GitHub user.<br/>
/rating/{username} - returns a sum of stargazers from all repositories of a given GitHub user.

**IMPORTANT NOTE**<br/>
your requests have to be authenticated via GitHub personal access token. Please use an already existing token, or generate one here:
https://github.com/settings/tokens/

Now you can access the application's endpoints with curl command<br/>

curl -i -u YourUsername:YourGitHubAccessToken  URL<br/>

e.g.

curl -i -u Vendeis:XXXX http://localhost:8080/repo/list/Vendeis <br/>

or by using a more convinient tool, such as Postman

![postman](https://user-images.githubusercontent.com/56355926/115912552-c21df380-a46f-11eb-9bd2-974a4eed5cf8.png)

## Possible development directions
Currently the project is relatively small, but it can be developed in various ways, such as:
* Sorting repositories by the amount of stargazers, in order to find out which repositories are most widely endorsed by GitHub users 
* Returning more information about repositories and use it to create some sort of statistics e.g. which programming language is most commonly used across listed repositories
* Creating an aesthetic UI and displaying collected data there

 ## Technologies

* Java 11
* Spring boot 2.4.5
* JUnit 5
