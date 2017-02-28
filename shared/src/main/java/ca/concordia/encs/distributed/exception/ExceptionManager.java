package ca.concordia.encs.distributed.exception;

import ca.concordia.encs.distributed.messaging.DefaultMessages;
import ca.concordia.encs.distributed.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

public class ExceptionManager {
    static Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    public static Message handleAndTranslate(Exception e) {
        if(e instanceof InvalidOperationException) {
            return DefaultMessages.BAD_REQUEST;
        }
        else if(e instanceof AccessViolationException)
            return DefaultMessages.FORBIDDEN;
        else if(e instanceof TimeoutException)
            return DefaultMessages.TIMEOUT;

        return DefaultMessages.SERVER_ERROR;
    }

    public static void handle(UnknownHostException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(IOException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(RemoteException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(SocketException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(NoSuchFieldException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(IllegalAccessException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(NumberFormatException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(InterruptedException e) {
        logger.debug("[EXCEPTION]", e);
    }
    public static void handle(NotBoundException e) {
        logger.debug("[EXCEPTION]", e);
    }
}
