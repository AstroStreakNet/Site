package net.astrostreak.site.exceptions;

public class RegisterException extends AuthException {
    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
