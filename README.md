# Kata banking
This project is developed to represent the basic banking actions.
Bank account kata think of your personal bank account experience. When in doubt, go for the simplest solution requirements
- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing
## User stories
```
- US 1:

In order to save money
As a bank client
I want to make a deposit in my account

- US 2:

In order to retrieve some or all of my savings
As a bank client
I want to make a withdrawal from my account

- US 3:

In order to check my operations
As a bank client
I want to see the history (operation, date, amount, balance) of my operations
```

## Class diagram

![img.png](readme/class_diagram.png)

## Project Structure

```
    ├── src
       ├── main
          ├── java
             ├── com.kata.banking
                ├── config
                ├── enums
                ├── exceptions
                ├── models
                ├── rest
                   ├── controller
                   ├── filter
                   ├── mapper
                   ├── request
                   ├── response
                ├── service
                   ├── impl
          ├── resources
       ├── test
          ├── java
             ├── com.kata.banking
                ├── rest.controller
                ├── service.impl
    ├── pom.xml
    └── README.md
```

## Technologies

```
   Java 11 
   Spring Boot
```

### TDD

```
   Junit 5
   Mockito
```
#### Running tests
![img.png](readme/tests.png)

## Getting Started

To run this project, please stick to the following steps

1. Clone the project
```
   https://github.com/Ons-Ben-Lakhal/banking.git
```
or simply download the project

2. Run the build command

```
  mvn clean install
```
3. Run the application

```
   mvn spring-boot:run
```
or run the jar
```
   cd target
   java -jar banking-0.0.1-SNAPSHOT.jar  
```
