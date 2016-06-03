
package com.syt.aliyun.sdk.exception;

/**
 * The excpetion is thrown if error happen.
 * 
 * @author laotang
 * 
 */
public class LogException extends Exception {

	private static final long serialVersionUID = -4441995860203577032L;

	private String errorCode;
	
	/**
	 * Construct LogException
	 * 
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 */
	public LogException(String code, String message) {
		super(message);
		this.errorCode = code;
	}

	/**
	 * Construct LogException
	 * 
	 * @param code
	 *            error code
	 * @param message
	 *            error message
	 * @param cause
	 *            inner exception, which cause the error
	 */
	public LogException(String code, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = code;
	}

	/**
	 * Get the error code
	 * 
	 * @return error code
	 */
	public String GetErrorCode() {
		return this.errorCode;
	}

	/**
	 * Get the error message
	 * 
	 * @return error message
	 */
	public String GetErrorMessage() {
		return super.getMessage();
	}
}
