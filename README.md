# JavaCodeChallenge

REST Web Service is implemented using Spring 4.2.

Class Application is a starting point of the service.

TransactionServiceController is a main controller of the service. It handles requests propogating them to TransactionService object.

Class TransactionService contains logic of the service, and is able to answer required queries.

It has three concurrent hash maps. One maps transaction ids to transaction objects. Another one maps types to sets of corresponding transaction ids. The last one maps transaction ids to total sums of amounts of all transactions transitively linked by parent ids. This allows to answer third GET query instantly.

Method addTransaction works in asynchronous fashion, which is done by Spring using @Async annotation. It is a synchronized method, which is needed to avoid collisions in case of simultaneous requests. This is not the best solution, because it uses locking. Queue of requests could be used to avoid it. But as far as this method is always called in a different thread, it doesn't block service from serving other requests. Complexity of this method is domindated by updates of total sums of all parent transactions. This is in the worst case O(number of all transactions added before), if they were all linked one by one. In general case it is O(number of transitively linked parent transactions).
Other methods, that answer different queries, are neither asynchronous nor synchronized, which is in theory dangerous, and could be improved, but in the current case they return value from only one concurrent map (among three) at a time. In my understanding it shouldn't lead to any race condition. They all have constant time complexity, because simply query one hash map each.

TransactionServiceTest class tests behaviour of methods of TransactionService.

TransactionServiceControllerIT class tests integrated behaviour of the service.
