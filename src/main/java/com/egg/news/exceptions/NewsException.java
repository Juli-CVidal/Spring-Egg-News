/*
// Curso Egg FullStack
 */
package com.egg.news.exceptions;

/**
 *
 * @author JulianCVidal
 */
public class NewsException extends Exception {

    /**
     * Creates a new instance of <code>newsException</code> without detail message.
     */
    public NewsException() {
    }

    /**
     * Constructs an instance of <code>newsException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NewsException(String msg) {
        super(msg);
    }
}
