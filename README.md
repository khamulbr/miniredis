# Mini REDIS challenge

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
