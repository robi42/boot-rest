package net.robi42.boot.util;

import lombok.experimental.UtilityClass;
import lombok.val;

import static com.google.common.base.MoreObjects.firstNonNull;

public @UtilityClass class Manifest {

    public static String version() {
        val implementationVersion = Manifest.class.getPackage().getImplementationVersion();
        return firstNonNull(implementationVersion, "SNAPSHOT");
    }
}
