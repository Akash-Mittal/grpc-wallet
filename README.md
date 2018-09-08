# grpc-wallet
Based on Google RPC and Protocool Buffer - a multi threaded App the Allows Deposit, Withdrawal and Balance


Overview
In order to access your readiness to work with our technology stack, we have devised this test task.

All technologies are used on a daily basis within our organization and this will prepare you for everyday software development participation.

Requirements
The task consists of a wallet server and a wallet client. The wallet server will keep track of a users monetary balance in the system. The client will emulate users depositing and withdrawing funds.

Wallet Server
The wallet server must expose the interface described below via gRPC.

Interfaces
Deposit
Deposit funds to the users wallet.

Input
User id
Amount
Currency (allowed values are EUR, USD, GBP)
Output
No output needed
Errors
Unknown currency
Withdraw
Withdraw funds from the users wallet.

Input
User id
Amount
Currency (allowed values are EUR, USD, GBP)
Output
No output needed
Errors
Unknown currency, insufficient funds
Balance
Get the users current balance.

Input
User id
Output
The balance of the users account for each currency
Database
Include the database schema that is needed for the server application.

Integration Test
Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
Make a deposit of USD 100 to user with id 1.
Check that all balances are correct
Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
Make a deposit of EUR 100 to user with id 1.
Check that all balances are correct
Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
Make a deposit of USD 100 to user with id 1.
Check that all balances are correct
Make a withdrawal of USD 200 for user with id 1. Must return "ok".
Check that all balances are correct
Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
Wallet Client
The wallet client will emulate a number of users concurrently using the wallet. The wallet client must connect to the wallet server over gRPC. 

The client eliminating users doing rounds (a sequence of events). Whenever a round is needed it is picked at random from the following list of available rounds

Round A
Deposit 100 USD
Withdraw 200 USD
Deposit 100 EUR
Get Balance
Withdraw 100 USD
Get Balance
Withdraw 100 USD
Round B
Withdraw 100 GBP
Deposit 300 GPB
Withdraw 100 GBP
Withdraw 100 GBP
Withdraw 100 GBP
Round C
Get Balance
Deposit 100 USD
Deposit 100 USD
Withdraw 100 USD
Depsoit 100 USD
Get Balance
Withdraw 200 USD
Get Balance
The wallet client should have the following CLI parameters:

users (number of concurrent users emulated)
concurrent_threads_per_user (number of concurrent requests a user will make)
rounds_per_thread (number of rounds each thread is executing)
Make sure the client exits when all rounds has been executed.

Technologies
The following technologies MUST be used

Java
gRPC
MySQL or PostgreSQL
Gradle
JUnit
SLF4J
Docker
Hibernate
