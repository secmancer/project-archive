package io.github.secmancer.scuffedclient.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModLogger
{
    public static final Logger LOGGER = LoggerFactory.getLogger("ScuffedClient");

    public static void log(String text) {
        LOGGER.info(text);
    }
}
