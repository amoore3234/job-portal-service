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

# Introduction
This project generates job board data by making client calls to Job Board API. OAuth2.0 is configured to make secured requests between clients and to confirm user authentication. The project is containerized using Docker and Docker Compose for easy deployment.

# Features
- **Job Portal Service:** A Java application built using Maven and Java JDK 21.
- **OAuth2.0:** A security mechanism for secured authentication and authorization utilizing client credentials.
- **Keycloak Inegration:** The application utilizes Keycloak's features for implementing client credentials for secured requests between clients.

# Prerequistes
- [Java JDK 21](https://www.openlogic.com/openjdk-downloads)
- [Docker](https://www.docker.com/get-started/)
- [Docker Compose](https://docs.docker.com/compose/install/)
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
The `env_template` file contains default variables to store your datbase credentials and other sensitive data. Create a `.env` file in your project and copy the environment variables from the template and store them into the new config file. Ensure the file is referenced in `.gitignore`.
  - For the `POSTGRES_DATABASE_URL` environment variable, you may run into issues connecting to the database. If so, ensure that the host name in the url string points to the postgresql container. Ex: `postgresql://[user]:[password]@[container name]/[database]`

### Running the Application
  - Start and build the application with the required docker command
    - `docker compose --env-file .env -f docker/docker-compose.yml up --build -d`

### Running Queries Against the Postgres Container
1. Once you spin up the application, if you need to run queries against the database, you can use the command below to access bash
    - `docker exec -it <container_name_or_id> /bin/bash`
2. Once you are able to interact with the container, you can run queries against the db. Run the command below to access the db by entering your postgres user name and datbase name.
    - `psql -U your_username -d your_database_name`

# Testing