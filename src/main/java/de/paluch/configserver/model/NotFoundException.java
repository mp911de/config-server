package de.paluch.configserver.model;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 20:50
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String s) {
        super(s);
    }
}
