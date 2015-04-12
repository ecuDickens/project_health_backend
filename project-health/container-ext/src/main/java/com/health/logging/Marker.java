package com.health.logging;

public enum Marker {
    /* Keywords for log records reporting monitored issues.               */
    HEALTH_ERROR_INTERNAL,
    HEALTH_ERROR_SERVICE_AUTH,
    HEALTH_ERROR_SERVICE_SOFTSWITCH,
    HEALTH_ERROR_SERVICE_JPA,
    HEALTH_ERROR_SERVICE_JERSEY;

    public static String insert(Marker marker, String record) {
        return marker + ": " + record;
    }
}
