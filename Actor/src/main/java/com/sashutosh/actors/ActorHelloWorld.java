package com.sashutosh.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class ActorHelloWorld {

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("Actor1");
        final ActorRef counterRef = actorSystem.actorOf(Counter.props(), "Counter");
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    counterRef.tell(new Counter.Message(finalI, j), ActorRef.noSender());
                }
            }).start();

        }


        System.out.println("Press any key to end");
        //StdIn.readline();
    }

    static class Counter extends AbstractLoggingActor {
        private int counter = 0;

        public static Props props() {
            return Props.create(Counter.class);
        }

        public Receive createReceive() {
            return ReceiveBuilder
                    .create()
                    .match(Message.class, this::onMessage)
                    .build();
        }

        private void onMessage(Message p) {
            counter += 1;
            log().info("Received message from thread " + p.threadId + " for iteration " + p.iteration);
            log().info("Increased counter " + counter);
        }

        //protocol
        static class Message {
            private final int threadId;
            private final int iteration;

            public Message(int id, int count) {
                this.threadId = id;
                this.iteration = count;
            }
        }
    }
}

