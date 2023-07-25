package com.clau.gpt.execption;

/**
 * Custom exception class for chat-related errors
 *
 */
public class ChatException extends RuntimeException {


    /**
     * Constructs a new ChatException with the specified detail message.
     *
     * @param msg the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ChatException(String msg) {
        super(msg);
    }


}
