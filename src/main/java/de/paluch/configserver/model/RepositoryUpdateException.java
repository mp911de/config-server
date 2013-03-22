package de.paluch.configserver.model;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 20:52
 */
public class RepositoryUpdateException extends RuntimeException {

    public RepositoryUpdateException(Throwable throwable) {
        super(throwable);
    }
}
