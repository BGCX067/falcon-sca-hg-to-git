/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.db;

/**
 *
 * @author rikscarborough
 */
public class NotFoundException extends Exception {

	public NotFoundException(String table, long id) {
		this(String.format("Data of type %s not found with key %d", table, id));
	}

	public NotFoundException(String table, long id, Throwable cause) {
		this(String.format("Data of type %s not found with key %d", table, id), cause);
	}

	private NotFoundException() {
	}

	private NotFoundException(String message) {
		super(message);
	}

	private NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	private NotFoundException(Throwable cause) {
		super(cause);
	}

	private NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
