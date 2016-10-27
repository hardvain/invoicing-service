# Acme Invoicing Service

This is an API for creating and querying invoices for the ACME cable TV company.

### Getting Started
* Install sbt. Mac users can install it using `brew install sbt`
* Build the solution with `sh ./build.sh`. This fetches the necessary artifacts, compiles and tests the project
* Run the app with `sh ./run.sh`.
* The port and host details of the app can be configured via `src/main/resources/application.conf`

### Assumptions & Decisions
* No Dependency Injection framework is used because of the limited number of classes and dependencies in between them. But still
 the code is written in a decoupled way where the dependencies are injected from the main class rather than instantiated inside
 the individual classes. This helps in mocking the dependencies during unit tests.
* Each class has its own documentation explaining its purpose.
* The `RequirementsSpec` class contains API tests for the requirements (endpoints) given.
* Implementations are provided for both MongoDB Backend and an inmemory backend.
* Contains an intelligent QueryBuilder which is capable of composing simple to complex queries with no addition of code.



## Foo Bar Assignment
* This repo also contains code for foo bar assignment. It is located under `folder/FooBar.scala`.
* It can be run using `scala foobar/FooBar.scala`.