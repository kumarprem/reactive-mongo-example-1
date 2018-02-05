package com.citi.example.reactivemongoexample1;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestDisposeFlux {

    public static void main(String arg[]){

        getfluxData();
    }

   static void  getfluxData(){

       List<String> data = new ArrayList<>(Arrays.asList("{A}", "{B}", "{C}"));
       data.stream();
       Flux<String> stringFlux = Flux.just(data).map(strings -> {
           return data.toString();
       });
        stringFlux.subscribe( s -> System.out.println(s)
       );

    Disposable obj =  Flux.just(1, 2, 3, 4)
                .log().delayElements(Duration.ofSeconds(1))
                .map(i -> i * 3
                )
                .subscribe(integer -> System.out.println(integer.toString())
                );


       System.out.println( "dispose =  "+obj.isDisposed());
       try {
           Thread.sleep(3000L);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

       obj.dispose(); // stop the flux
       System.out.println( "after dispose =  "+obj.isDisposed());
    }
}
