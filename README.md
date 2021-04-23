#  Task number 3

## Table of contents
* [Setup](#setup)
* [Sending API requests](#sending-API-requests)
* [Possible development directions](#possible-development-directions)


## Setup

* clone the repository
* navigate to the root folder and build the project using following command:
mvn clean install
* after building the application run following command to start it:
java -jar target/allegrotask-1.0.jar

## Sending Api requests

Unauthenticated clients can make up to 60 requests per hour, which is in my opinion insufficient. Therefore I have implemented authentication via username and github access token, which extends the amount of requests per hour to 5000. 

The application exposes 2 endpoints:
/list/{username} - returns a list of all repositories, alongside their stargazer count, for a given GitHub user.
/rating/{username} - returns a sum of stargazers from all repositories of a given GitHub user.

IMPORTANT NOTE
your requests have to be authenticated via GitHub personal access token. Please use an already existing token, or generate one here:
https://github.com/settings/tokens/

Now you can access the application's endpoints with curl command 

curl -i -u YourUsername:YourGitHubAccessToken  URL
e.g

curl -i -u Vendeis:XXXX http://localhost:8080/repo/list/Vendeis

or by using a more convinient tool, such as Postman


## Possible development directions
Currently the project is relatively small, but it can be developed in various ways, such as:
* Sorting repositories by the amount of stargazers, in order to find out which repositories are most widely endorsed by GitHub users 
* Returning more information about repositories and use it to create some sort of statistics e.g. which programming language is most commonly used across listed repositories
* Creating an aesthetic UI and displaying collected data there

 ## Technologies

* Java 11
* Spring boot 2.4.5
* JUnit 5
