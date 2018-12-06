package com.novell.zenworks;/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */

/*
 * © 2017 Micro Focus Software Inc. All rights reserved
 *
 *   THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
 *   AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS  TO  THIS  WORK IS
 *   RESTRICTED TO (I) NOVELL, INC.  EMPLOYEES WHO HAVE A NEED TO  KNOW HOW
 *   TO  PERFORM  TASKS WITHIN  THE SCOPE  OF  THEIR   ASSIGNMENTS AND (II)
 *   ENTITIES OTHER  THAN  NOVELL, INC.  WHO  HAVE ENTERED INTO APPROPRIATE
 *   LICENSE   AGREEMENTS.  NO  PART  OF  THIS WORK MAY BE USED, PRACTICED,
 *   PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
 *   CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED,  RECAST, TRANSFORMED
 *   OR ADAPTED  WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC.  ANY USE
 *   OR EXPLOITATION  OF  THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
 *   PERPETRATOR  TO CRIMINAL AND CIVIL LIABILITY.
 */




import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by user on 7/11/2017.
 */
public class ConsumerLoop implements Runnable {


    KafkaConsumer consumer;
    KafkaMessageConsumer msgConsumer;
    /***
     *
     * @param msgConsumer - The consumer over which the loop is running
     */

    public ConsumerLoop(KafkaMessageConsumer msgConsumer){

        this.consumer=msgConsumer.getConsumer();
        this.msgConsumer=msgConsumer;

    }

    List<Object> getMessageObject(ConsumerRecords<String, String> records) {
        List<ConsumerRecord<String, String>> topicData = StreamSupport.stream(records.records(msgConsumer.getTopicName()).spliterator(), false)
                .collect(Collectors.toList());
        ;
        List<Object> msgObjects = new ArrayList();
        topicData.forEach(x -> msgObjects.add(x.value()));
        return msgObjects;
    }

    public void run() {
        try {

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                if (records != null && records.count() > 0)
                    System.out.print("Read record count" + records.count());
                    for (IObserver observer:msgConsumer.getObservers()) {
                        List<Object> messageObjects = getMessageObject(records);
                        for(Object message: messageObjects) {
                            try {
                                observer.notify(msgConsumer.getTenantName(), message);
                            }
                            catch (Exception e) {
                                System.out.println("skipping current message due to: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }

                //consumer.commitSync();
            }
        } catch (WakeupException e) {
            // ignore for shutdown
            System.out.print("Exception in processing record " + e.getStackTrace());
        }
        catch(Exception e) {
            System.out.print("Exception in processing record " + e.getStackTrace());
        }
        finally
         {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
