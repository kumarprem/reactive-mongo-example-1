package com.citi.example.reactivemongoexample1.reactivemongo;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import com.mongodb.ConnectionString;
import com.mongodb.CursorType;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;

import java.util.concurrent.CompletionStage;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class HelloReactive {

	public static void main(String[] args) {

		ConnectionString conn = new ConnectionString("mongodb://localhost:27017/");

		MongoClient mongoClient = MongoClients.create(conn);
		MongoCollection<Document> coll = mongoClient.getDatabase("reactive").getCollection("pubsub");

		//Approach 1 using mongodb reactive driver
		coll.find(and(eq("type", "insert"), eq("status", "notstarted"))).cursorType(CursorType.Tailable)
				.subscribe(new SubscriberHelpers.PrintDocumentSubscriber());


		//Code to simply make the code wait for events, not needed in a web app
		Runnable task = () -> {
			System.out.println("\tWaiting for events");
			while (true) {

			}
		};
		new Thread(task).start();
		
		
		//Approach 2 using akka streams + actor system
		final ActorSystem system = ActorSystem.create("QuickStart");
		final Materializer materializer = ActorMaterializer.create(system);

		Source<Document, NotUsed> source = Source.fromPublisher(
				coll.find(and(eq("type", "insert"))).cursorType(CursorType.Tailable));
		final CompletionStage<Done> done = source.buffer(1, OverflowStrategy.backpressure()).runForeach(s -> {
			System.out.println(s.toJson());
		}, materializer);
		done.thenRun(() -> system.terminate());
			
		

	}

}
