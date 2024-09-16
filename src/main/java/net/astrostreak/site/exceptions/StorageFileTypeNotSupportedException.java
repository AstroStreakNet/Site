package net.astrostreak.site.exceptions;

public class StorageFileTypeNotSupportedException extends StorageException {

    public StorageFileTypeNotSupportedException(String message) {
        super(message);
    }

    public StorageFileTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
