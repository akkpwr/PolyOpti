package org.butbetter.polyopti.client.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Safeguard {

    private static final Logger LOGGER = LoggerFactory.getLogger("PolyOpti");
    private static final Set<String> REPORTED = ConcurrentHashMap.newKeySet();

    private Safeguard() {
    }

    public static void run(String hook, Runnable action) {
        try {
            action.run();
        } catch (Throwable throwable) {
            report(hook, throwable);
        }
    }

    public static boolean test(String hook, BooleanSupplier action, boolean fallback) {
        try {
            return action.getAsBoolean();
        } catch (Throwable throwable) {
            report(hook, throwable);
            return fallback;
        }
    }

    public static <T> T supply(String hook, Supplier<T> action, T fallback) {
        try {
            return action.get();
        } catch (Throwable throwable) {
            report(hook, throwable);
            return fallback;
        }
    }

    private static void report(String hook, Throwable throwable) {
        if (REPORTED.add(hook)) {
            LOGGER.warn("PolyOpti hook '{}' failed and will stay disabled", hook, throwable);
        }
    }
}
