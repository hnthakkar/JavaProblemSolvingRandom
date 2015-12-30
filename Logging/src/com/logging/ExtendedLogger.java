package com.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

/**
 * Extended Logger interface with convenience methods for
 * the SplunkFeed custom log level.
 */
public final class ExtendedLogger extends ExtendedLoggerWrapper {
    private static final long serialVersionUID = 461547332660932L;
    private final ExtendedLoggerWrapper logger;

    private static final String FQCN = ExtendedLogger.class.getName();
    private static final Level SplunkFeed = Level.forName("SplunkFeed", 350);

    private ExtendedLogger(final Logger logger) {
        super((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());
        this.logger = this;
    }

    /**
     * Returns a custom Logger with the name of the calling class.
     * 
     * @return The custom Logger for the calling class.
     */
    public static ExtendedLogger create() {
        final Logger wrapped = LogManager.getLogger();
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger using the fully qualified name of the Class as
     * the Logger name.
     * 
     * @param loggerName The Class whose name should be used as the Logger name.
     *            If null it will default to the calling class.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final Class<?> loggerName) {
        final Logger wrapped = LogManager.getLogger(loggerName);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger using the fully qualified name of the Class as
     * the Logger name.
     * 
     * @param loggerName The Class whose name should be used as the Logger name.
     *            If null it will default to the calling class.
     * @param messageFactory The message factory is used only when creating a
     *            logger, subsequent use does not change the logger but will log
     *            a warning if mismatched.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final Class<?> loggerName, final MessageFactory factory) {
        final Logger wrapped = LogManager.getLogger(loggerName, factory);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger using the fully qualified class name of the value
     * as the Logger name.
     * 
     * @param value The value whose class name should be used as the Logger
     *            name. If null the name of the calling class will be used as
     *            the logger name.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final Object value) {
        final Logger wrapped = LogManager.getLogger(value);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger using the fully qualified class name of the value
     * as the Logger name.
     * 
     * @param value The value whose class name should be used as the Logger
     *            name. If null the name of the calling class will be used as
     *            the logger name.
     * @param messageFactory The message factory is used only when creating a
     *            logger, subsequent use does not change the logger but will log
     *            a warning if mismatched.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final Object value, final MessageFactory factory) {
        final Logger wrapped = LogManager.getLogger(value, factory);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger with the specified name.
     * 
     * @param name The logger name. If null the name of the calling class will
     *            be used.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final String name) {
        final Logger wrapped = LogManager.getLogger(name);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Returns a custom Logger with the specified name.
     * 
     * @param name The logger name. If null the name of the calling class will
     *            be used.
     * @param messageFactory The message factory is used only when creating a
     *            logger, subsequent use does not change the logger but will log
     *            a warning if mismatched.
     * @return The custom Logger.
     */
    public static ExtendedLogger create(final String name, final MessageFactory factory) {
        final Logger wrapped = LogManager.getLogger(name, factory);
        return new ExtendedLogger(wrapped);
    }

    /**
     * Logs a message with the specific Marker at the {@code SplunkFeed} level.
     * 
     * @param marker the marker data specific to this log statement
     * @param msg the message string to be logged
     */
    public void splunkfeed(final Marker marker, final Message msg) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, msg, (Throwable) null);
    }

    /**
     * Logs a message with the specific Marker at the {@code SplunkFeed} level.
     * 
     * @param marker the marker data specific to this log statement
     * @param msg the message string to be logged
     * @param t A Throwable or null.
     */
    public void splunkfeed(final Marker marker, final Message msg, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, msg, t);
    }

    /**
     * Logs a message object with the {@code SplunkFeed} level.
     * 
     * @param marker the marker data specific to this log statement
     * @param message the message object to log.
     */
    public void splunkfeed(final Marker marker, final Object message) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, message, (Throwable) null);
    }

    /**
     * Logs a message at the {@code SplunkFeed} level including the stack trace of
     * the {@link Throwable} {@code t} passed as parameter.
     * 
     * @param marker the marker data specific to this log statement
     * @param message the message to log.
     * @param t the exception to log, including its stack trace.
     */
    public void splunkfeed(final Marker marker, final Object message, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, message, t);
    }

    /**
     * Logs a message object with the {@code SplunkFeed} level.
     * 
     * @param marker the marker data specific to this log statement
     * @param message the message object to log.
     */
    public void splunkfeed(final Marker marker, final String message) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, message, (Throwable) null);
    }

    /**
     * Logs a message with parameters at the {@code SplunkFeed} level.
     * 
     * @param marker the marker data specific to this log statement
     * @param message the message to log; the format depends on the message factory.
     * @param params parameters to the message.
     * @see #getMessageFactory()
     */
    public void splunkfeed(final Marker marker, final String message, final Object... params) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, message, params);
    }

    /**
     * Logs a message at the {@code SplunkFeed} level including the stack trace of
     * the {@link Throwable} {@code t} passed as parameter.
     * 
     * @param marker the marker data specific to this log statement
     * @param message the message to log.
     * @param t the exception to log, including its stack trace.
     */
    public void splunkfeed(final Marker marker, final String message, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, marker, message, t);
    }

    /**
     * Logs the specified Message at the {@code SplunkFeed} level.
     * 
     * @param msg the message string to be logged
     */
    public void splunkfeed(final Message msg) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, msg, (Throwable) null);
    }

    /**
     * Logs the specified Message at the {@code SplunkFeed} level.
     * 
     * @param msg the message string to be logged
     * @param t A Throwable or null.
     */
    public void splunkfeed(final Message msg, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, msg, t);
    }

    /**
     * Logs a message object with the {@code SplunkFeed} level.
     * 
     * @param message the message object to log.
     */
    public void splunkfeed(final Object message) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, message, (Throwable) null);
    }

    /**
     * Logs a message at the {@code SplunkFeed} level including the stack trace of
     * the {@link Throwable} {@code t} passed as parameter.
     * 
     * @param message the message to log.
     * @param t the exception to log, including its stack trace.
     */
    public void splunkfeed(final Object message, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, message, t);
    }

    /**
     * Logs a message object with the {@code SplunkFeed} level.
     * 
     * @param message the message object to log.
     */
    public void splunkfeed(final String message) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, message, (Throwable) null);
    }

    /**
     * Logs a message with parameters at the {@code SplunkFeed} level.
     * 
     * @param message the message to log; the format depends on the message factory.
     * @param params parameters to the message.
     * @see #getMessageFactory()
     */
    public void splunkfeed(final String message, final Object... params) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, message, params);
    }

    /**
     * Logs a message at the {@code SplunkFeed} level including the stack trace of
     * the {@link Throwable} {@code t} passed as parameter.
     * 
     * @param message the message to log.
     * @param t the exception to log, including its stack trace.
     */
    public void splunkfeed(final String message, final Throwable t) {
        logger.logIfEnabled(FQCN, SplunkFeed, null, message, t);
    }
}

