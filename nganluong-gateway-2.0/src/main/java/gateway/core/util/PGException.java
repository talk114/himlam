/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.util;

/**
 *
 * @author nnes
 */
public class PGException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String errorCode;

	public PGException() {
		super();
	}

	public PGException(String errorCode, String message) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}

	public PGException(String message) {
		super(message, new Throwable(message));
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
