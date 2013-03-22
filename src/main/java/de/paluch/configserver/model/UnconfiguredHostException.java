package de.paluch.configserver.model;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 21:05
 */
public class UnconfiguredHostException extends RuntimeException {

    public UnconfiguredHostException(String s) {
        super(s);
    }
}
