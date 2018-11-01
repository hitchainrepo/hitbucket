/*******************************************************************************
 * Copyright (c) 2018-11-01 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import scala.AnyVal;
import scala.Function0;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockUtilHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-01
 * auto generate by qdp.
 */
public class LockUtilHelper {
    /**
     * lock objects
     */
    private static final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();

    /**
     * Returns the lock object for the specified repository.
     */
    public static synchronized Lock getLockObject(String key) {
        if (!locks.containsKey(key)) {
            locks.put(key, new ReentrantLock());
        }
        return locks.get(key);
    }

    /**
     * Synchronizes a given function which modifies the working copy of the wiki repository.
     */
    public static <T> T lock(String key, Object f) {
        Lock lock = getLockObject(key);
        try {
            lock.lock();
            return (T) ((Function0)f).apply();
        } finally {
            lock.unlock();
        }
    }
}
