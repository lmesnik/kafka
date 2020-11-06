/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * A wrapper for Thread that sets things up nicely
 */
public class KafkaThread {

    private final static Logger log = LoggerFactory.getLogger(KafkaThread.class);

    public static Thread daemon(final String name, Runnable runnable) {
        return newKafkaThread(name, runnable, true);
    }

    public static Thread nonDaemon(final String name, Runnable runnable) {
        return newKafkaThread(name, runnable, false);
    }

    public static Thread newKafkaThread(final String name, Runnable runnable, boolean daemon) {
            Thread thread;
            try {
                Method newThread = Thread.class.getDeclaredMethod("newThread",
                        new Class[] { String.class, int.class, Runnable.class });
                thread = (Thread) newThread.invoke(null, name, 1, runnable);
            } catch (Exception e) {
                e.printStackTrace();
                thread = new Thread(runnable, name);
            }
        thread.setDaemon(daemon);
        thread.setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception in thread '{}':", name, e));
        return thread;
    }

}
