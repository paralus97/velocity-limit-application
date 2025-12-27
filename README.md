# Velocity Limit Application
The project creates a fully functional Velocity Limits Application as described by the challenge specification. Velocity
limits are common in digital day-to-day banking and are used to control the total volume of funds an individual can move
over a set period of time. 

## Functional Requirements
What follows are the velocity limits and rules for any given user in the system.

- In an individual day, a user is able to make no more than 3 transactions.
- There is a daily transaction limit of $5000.00
- There is a weekly transaction limit of $20000.00

Each load attempt will return a response in the following form
```json
{
  "id": "1234",
  "customer_id": "1234",
  "accepted": true
}
```

## Application Set Up and Usage

Requirements:
 - Java 17+
 - Spring 4.0.1
 - Maven 3.6.x

First, clone the repository into your working directory with the following:

`git clone https://github.com/paralus97/velocity-limit-application.git`

Navigate into the cloned velocity-limit-application and install the dependencies.

`mvn clean install     # this builds the app, pulls in dependencies and runs tests.`

To start up the Spring application, once dependencies are installed, use the following.

`mvn spring-boot:run`

This will have triggered the `InputFileProcessorRunner`. The function of this is to take the sample input given by Venn located in `assets/Venn-Back-End-Input.txt`, run it through the application's core logic and produce the expected output expected in `output.txt` (which should be in the root of the project now).

To package the application as a jar file, you can run `mvn package` and run the application with `java -jar target/.velocitylimitapp-0.0.1-SNAPSHOT.jar`.

### Testing the Solution

The suite of unit tests in the project can be executed by running the following

`mvn test` or `mvn clean test`

This executes tests located in `VelocityLimitServiceTest.java`, `TransactionEntityRepositoryTest.java` and `VelocityLimitApplicationTests.java`.

To verify the output of the application, there is a script in the root of the project designed to check that the application's `output.txt` matches exactly the contents of `assets/Venn-Back-End-Output.txt`. **Important: Make sure that the application has been run with** `mvn spring-boot:run` **first, or else there will be no output file to check.** The script can be run with the following.

`bash check_output_is_expected.sh`

The output should read `Success: The output file matches the example output` if the application has functioned correctly.


