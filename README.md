# JavaCodeChallenge

REST Web Service is implemented using Spring 4.2.

Class Application is a starting point of the service.

TransactionServiceController is a main controller of the service. It handles requests propogating them to TransactionService object.

Class TransactionService contains logic of the service, and is able to answer required queries.
Method addTransaction works in asynchronous fashion, which is done by Sprint using @Async annotation. It is a synchronized method, which is needed to avoid collisions in case of simultaneous requests. This is not the best solution, because it uses locking. Queue of requests could be used to avoid it. But as far as it is done in a different thread always, it doesn't block service from serving other requests.
Other methods, that answer different queries, are neither asynchronous nor synchronized, which is in theory dangerous, and could be improved, but in the current case they return value from only one concurrent map (among three) at a time. In my understanding it should lead to any race condition.

TransactionServiceTest class tests behaviour of methods of TransactionService calls.

TransactionServiceControllerIT class tests integrated behaviour of the service.
