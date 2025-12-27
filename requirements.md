

- in memory database
- Each customer is subject to three limits:
   -  A maximum of $5,000 can be loaded per day 
   - A maximum of $20,000 can be loaded per week 
   - A maximum of 3 loads can be performed


- An individual transaction attempt is read from the InputFileController
- The InputFileController invokes the VelocityLimitService.processTransactionAttempt method
- The processTransactionAttemptMethod checks if TransactionEntity ID exists in database, does all other checks, creates TransactionEntity object if necessary. Only TransactionAttempts need to be stored. Responses can be modeled but no need to store them. TransactionResponse object returned but not stored.
- return output in the format specified in doc, either to standard output or a file (have the option for both)






- thoroughly test the application build out suite to 
  - test restrictions of velocity service X
  - test the sample venn input/output 
  - test application basics, startup etc.
- Have a runner for outputting correct output text and nothing else. Use CommandLineRunner to run at startup
- Have invoking the runner
- Have an endpoint for adding individual transaction entities with the expected acceptance or non-acceptance response
- Have an endpoint to flush/clear database
- Refactor core code
  - tidy it up, remove block comments, spacing
  - Inspect if usage of records for modeling is appropriate or not.
  - introduce immutability where possible and where its best spring practice
  - introduce appropriate annotations as per spring best practice.

- create the option to encapsulate/run the application as a microservice with docker. 
- also create the option to run the application as a jar.
- create a README.md listing requirements, usage, architectural overview