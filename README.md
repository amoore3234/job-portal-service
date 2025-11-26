# Job Portal Service
A service that generates job board data.

# Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequistes)
- [Installation](#installation)
- [Getting Started](#getting-started)
  - [Starting the Job Portal Service Container](#spinning-up-the-job-board-api-container)
    - [Running The Application](#running-the-application)
    - [Running Queries Against The Prostgres Container](#running-queries-against-the-postgres-container)
- [Testing](#testing)
  - [Testing User Registration and Login Functionality](#testing-user-registration-and-login-functionality)
  - [Running Testcontainers](#running-testcontainers)

# Introduction
This project generates job board data by making client calls to Job Board API. OAuth2.0 is configured to make secured requests between clients and to confirm user authentication. The project is containerized using Docker and Docker Compose for easy deployment.

# Features
- **Job Portal Service:** A Java application built using Maven and Java JDK 21.
- **Spring Security:** A secured way to apply authentication and authorization for user registration.
- **OAuth2.0:** A security mechanism for secured authentication and authorization utilizing client credentials.
- **Keycloak Integration:** The application utilizes Keycloak's features for implementing client credentials for secured requests between clients.

# Prerequistes
- [Java JDK 21](https://www.openlogic.com/openjdk-downloads)
  - **For Mac**, you can install the JDK version with brew
    - `brew install openjdk@21`
- [Docker](https://www.docker.com/get-started/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Docker Desktop for Windows](#https://docs.docker.com/desktop/setup/install/windows-install/)
- [Docker Desktop for Mac](#https://docs.docker.com/desktop/setup/install/mac-install/)
- [Git](https://git-scm.com/)
- **For Windows (WSL):**
  - [WSL 2](https://learn.microsoft.com/en-us/windows/wsl/install)
  - [Ubuntu](https://documentation.ubuntu.com/wsl/latest/howto/install-ubuntu-wsl2/)
    - **Note:** If there is an issue starting the docker engine, you may need to add the user to the docker group. you can run this command:
      - `sudo usermod -aG docker $USER`

# Installation
If you run into an issue where the `JAVA_HOME`is not set in WSL, you may need to install the Linux version of the JDK inside your WSL Distribution.
  1. If you don't have Java JDK already installed in WSL:
    - `sudo apt update`
    - `sudo apt install openjdk-21-jdk`
  2. Find the path to your JDK and set the path to the `JAVA_HOME` environment in your bash script.
    - `ls /usr/lib/jvm/` - You should see the file name `java-21-openjdk-amd64` or similar.
    - `nano ~/.bashrc`
    - `export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"`
    - `export PATH=$JAVA_HOME/bin:$PATH`
  3. Confirm that the path is set within your environment.
    - `echo $JAVA_HOME`
    - `java -version`

# Getting Started

## Starting the Job Portal Service Container
The `env_template` file contains default variables to store your database credentials and other sensitive data. Create a `.env` file in your project and copy the environment variables from the template and store them into the new config file. For anyone using IntelliJ, you can store the env variables within the configuration settings. Ensure the file is referenced in `.gitignore`.
  - For the `POSTGRES_DATABASE_URL` environment variable, you may run into issues connecting to the database. If so, ensure that the host name in the url string points to the postgresql container. Ex: `jdbc:postgresql://[container_name]:5432/[database_name]`
  - The `KEYCLOAK_DB_URL` env should follow the same URL structure as the postgres URL string illustrated above. Ex: `jdbc:postgresql://[keycloak_container_name]:5432/[keycloak_database_name]`

If you are using VSCode as your IDE and are have issues connecting to the PostgreSQL database, you can add the json payload in your `launch.json` file:
  ```
  {
    "type": "java",
    "name": "JobPortalServiceApplication",
    "request": "launch",
    "mainClass": "com.portal.job_portal_service.JobPortalServiceApplication",
    "projectName": "job-portal-service",
    "envFile": "${workspaceFolder}/.env"
  }
  ```
To create a `launch.json` file, open the **Run and Debug**(Ctrl+Shift+D) view.

If the `launch.json` file doesn't exist, click the link to `create a launch.json file` and select Java from the environment dropdown. This creates a `.vscode` folder with a launch.json file.

### Running the Application
  - Build the application locally
    - `mvn clean install`
  - Start and build the application with the required docker command
    - `docker compose --env-file .env -f docker/docker-compose.yml up --build -d`
  - To shut down and stop active containers
    - `docker compose --env-file .env -f docker/docker-compose.yml down`
  - **Optional:** If you would like to run the application from your desired IDE instead of using the docker command above, you can run the `docker compose --env-file .env -f docker/docker-compose.yml up <postgres_container_name>` to spin up the standalone postgres container only. After starting the database, you can proceed running the spring boot application from your local environment.
  - Go to to the [Testing User Registration and Login Functionality](#testing-user-registration-and-login-functionality) section to interact with the UI to test the login feature.

### Running Queries Against the Postgres Container
1. Once you spin up the application, if you need to run queries against the database, you can use the command below to access bash
  - `docker exec -it <container_name_or_id> /bin/bash`
2. Once you are able to interact with the container, you can run queries against the db. Run the command below to access the db by entering your postgres user name and datbase name.
  - `psql -U <your_username> -d <your_database_container_name>`
3. If you would like to access the database without executing the docker command in step 1, then you can run the command below.
  - `psql -h localhost -p 5432 <your_username> -d <your_database_container_name>`

# Testing

## Testing User Registration and Login Functionality
Once you have successfully build the application, navigate to the [registration](http:localhost:8080/portal/user/register) form to register a user. Once a user is registered into the system, navigate to the [login](http:localhost:8080/portal/user/login) page and log in with the username and password credentials you registed with. If login was succesful, you should be navigated to the home page. Once you logout from the home page, you should be redirected to the login page. If you try accessing the [home](http:localhost:8080/portal/user/home) page without entering your credentials, you should be redirected to the login page. The service utilizes Spring Security to only allow authenticated users access to additional content after proceeding through the login flow.

## Testing the GitHub and Google Authentication in the Login Flow
Users also have the option to use their GitHub or Google credentials to Log into the job board. Download [this](https://drive.google.com/file/d/16B7ysQCqG95XmnH9a1tDyYQ8IOtfA9Ww/view?usp=sharing) file, unzip it locally, and store the Google and GitHub client credentials in your `.env` or Spring Boot configuration settings to interact with the auth flow. Once you configure the credentials in your env, then you should be able to access the home page using Github or Google's autorization flow.

## Running Testcontainers
You would need to install Docker Desktop to run the Testcontainers locally. If you are running docker natively through WSL, additional configurations are required to be able to successfully run the Testcontainers in the test directory. When running the Testcontainers, it tries to connect to Docker on the Windows host machine, which may not exist if you Docker Desktop is not installed. You can try configuring your WSL instance to have Testcontainers point to the docker daemon, but this can expose potential security risks if you are not working from a private machine. The most simplest solution is to install Docker Desktop, so that your Windows host machine can connect to Docker successfully when the Testcontainers are executed.

If you are using VSCode and having issues running Testcontainers on your Windows machine, you can create a `launch.json` file in your `.vscode ` folder and add the json object in your configurations. More details for creating the json file in the [Starting the Job Portal Service Container](#spinning-up-the-job-board-api-container) section.

```
{
  "type": "java",
  "name": "Launch UserRepositoryIT",
  "request": "launch",
  "mainClass": "com.portal.job_portal_service.repository.UserRepositoryIT",
  "projectName": "job-portal-service",
  "env": {
    "DOCKER_HOST": "unix:///var/run/docker.sock"
  }
}
```
# Resources
- [Springdoc OpenAPI 3 and Spring Boot](https://springdoc.org/#google_vignette)
- [Adding Authentication for GitHub Application](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/about-authentication-to-github)
  - [Creating a GitHub Application Sample](https://docs.github.com/en/apps/creating-github-apps)
- [Adding Authentication for Google](https://developers.google.com/identity/protocols/oauth2)
- [Integrating a web client in Spring Boot](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)