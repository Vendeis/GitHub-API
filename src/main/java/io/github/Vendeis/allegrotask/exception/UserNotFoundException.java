package io.github.Vendeis.allegrotask.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message, RuntimeException exception){
        super(message,exception);
    }
}
