

- in memory database
- Each customer is subject to three limits:
   -  A maximum of $5,000 can be loaded per day 
   - A maximum of $20,000 can be loaded per week 
   - A maximum of 3 loads can be performed


- An individual transaction attempt is read from the InputFileController
- The InputFileController invokes the VelocityLimitService.processTransactionAttempt method
- The processTransactionAttemptMethod checks if ID exists in database, does all other checks, creates TransactionAttempt object if necessary. Only TransactionAttempts need to be stored. Responses can be modeled but no need to store them. TransactionResponse object returned but not stored.
