package com.sashutosh.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class Sample2 {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("behavior");
        ActorRef alarm = system.actorOf(Alarm.props("mypw"), "alarm");
        alarm.tell(new Alarm.Activity(), ActorRef.noSender());
        alarm.tell(new Alarm.Enable("wrong"), ActorRef.noSender());
        alarm.tell(new Alarm.Enable("mypw"), ActorRef.noSender());
        alarm.tell(new Alarm.Activity(), ActorRef.noSender());
        alarm.tell(new Alarm.Disable("wrong"), ActorRef.noSender());
        alarm.tell(new Alarm.Disable("mypw"), ActorRef.noSender());

        System.out.println("Done..");


    }

    static class Alarm extends AbstractLoggingActor {
        private final String password;

        private final Receive enabled;
        private final Receive disabled;

        public Alarm(String password) {
            this.password = password;
            enabled = ReceiveBuilder
                    .create()
                    .match(Activity.class, this::onActivity)
                    .match(Disable.class, this::onDisabled)
                    .build();

            disabled = ReceiveBuilder
                    .create()
                    .match(Activity.class, this::onActivity)
                    .match(Enable.class, this::onEnabled)
                    .build();

            getContext().become(disabled);

        }

        public static Props props(String password) {
            return Props.create(Alarm.class, password);
        }

        private void onEnabled(Enable enable) {
            if (this.password.equals(enable.password)) {
                log().warning("Alarm Enabled");
                getContext().become(enabled);
            } else {
                log().info("Trying to enable alarm. Wrong password..");
            }

        }

        private void onDisabled(Disable disable) {
            if (this.password.equals(disable.password)) {
                log().warning("Alarm Disabled");
                getContext().become(disabled);
            } else {
                log().warning("Trying to disable alarm. Wrong password..");
            }
        }

        private void onActivity(Activity ignore) {
            /*if(getContext().)*/
            {
                log().warning("Burglar Alarm......");
            }
            /*else {
                log().info("Alarm disabled");
            }*/
        }

        @Override
        public Receive createReceive() {
            return null;
        }

        static class Activity {
        }

        static class Disable {
            private final String password;

            public Disable(String password) {
                this.password = password;
            }
        }

        static class Enable {
            private final String password;

            public Enable(String password) {
                this.password = password;
            }
        }
    }
}
