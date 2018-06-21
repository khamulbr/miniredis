# Mini REDIS challenge

Redis is an in-memory NoSQL data store that supports operations or “commands” on data structures
such as sets, lists and hashes. Your objective is to implement a service that supports a subset of the
Redis command set. That is, you are to build a “mini redis”.

This has two parts -- first, the implementation of Redis commands and the underlying data structure to
support them, and second (optional part), support for calling this “mini redis” over the network.
As you work on this challenge, try to complete the first part in its entirety before moving on to the
optional part.

Java implementation of the following REDIS commands:

1. SET key value
2. SET key value EX seconds (need not implement other SET options)
3. GET key
4. DEL key
5. DBSIZE
6. INCR key
7. ZADD key score member
8. ZCARD key
9. ZRANK key member
10. ZRANGE key start stop

# Atomicity
One of the key benefits of Redis is that it guarantees atomic, ordered access to data. Your
implementation should offer the same guarantee, so that, for example, access from multiple threads is
handled safely.

# Deliverable
When this part of the challenge is complete, you should have a working implementation of the specified
commands, and there should be one or more methods or other entry point that can invoke those
commands.

This should include some sort of test harness or set of test cases that allows the developer to
demonstrate the functionality of the implemented commands.

# Setup project on IntelliJ
Call ./gradlew cleanIdea idea

# Building
Call ./gradlew clean build

# Running tests
Call ./gradlew test

# Running Springboot HTTP REST service
Call ./gradlew bootRun

# URL's for using HTTP REST service
sets value to key -                       (PUT)       localhost:8080/miniredis/teste2?value=10

sets value to key for 2 seconds -         (PUT)       localhost:8080/miniredis/teste1?value=10&expirationMode=EX&expirationValue=2  

returns value of key -                    (GET)       localhost:8080/miniredis/teste2

increments value of key and returns it -  (POST)      localhost:8080/miniredis/teste2

removes key from database -               (DELETE)    localhost:8080/miniredis/teste2

returns size of database -                (GET)       localhost:8080/miniredis/dbsize

# Observations
As this version doesn't have any authentication mechanism, the session is being created based on the client, i.e: curl commands will always create a new session, so it's advised to use some REST client (Postman, ARC, etc.) for properly accessing the API.
