package com.citi.example.reactivemongoexample1.resource;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.citi.example.reactivemongoexample1.model.Employee;
import com.citi.example.reactivemongoexample1.model.EmployeeEvent;
import com.mongodb.ConnectionString;
import com.mongodb.CursorType;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@RestController
public class EmployeeResource {

@RequestMapping(value = "/events" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  Flux<EmployeeEvent> getEvents()
{

    ConnectionString conn = new ConnectionString("mongodb://localhost:27017/");

    MongoClient mongoClient = MongoClients.create(conn);

    //Approach 2 using akka streams + actor system
    final ActorSystem system = ActorSystem.create("QuickStart");
    final Materializer materializer = ActorMaterializer.create(system);

    Flux<Document> pub;
    pub = Flux.from(mongoClient.getDatabase("reactive").getCollection("pubsub").find(and(eq("type", "insert")))
            .cursorType(CursorType.Tailable));
    //if()

    return getEmployeeEventFlux(pub);


}

    private Flux<EmployeeEvent> getEmployeeEventFlux(Flux<Document> pub) {
        Disposable disposable = pub.subscribe();
        // Disposable subscribe = pub.subscribe(document -> document.getObjectId("hello world3"));
        //StepVerifier.create(pub).expectNext("kyle").verifyComplete();
        //  System.out.println(" pub subscribe = "+subscribe.equals("hello world3"));

        final Flux<Document>  docue = pub.checkpoint().doOnEach(documentSignal -> documentSignal.isOnNext()).log();

        System.out.println("is disposed = "+disposable.isDisposed());
        //disposable.dispose();
        return    pub.log().subscribeOn(Schedulers.parallel()).map(doc -> new EmployeeEvent(new Employee((String)doc.get("type"), (String)doc.get("message"), 100L), new Date())
        );
    }

}





















