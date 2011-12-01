/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

/**
 *
 * @author Akshay Sharma
 * Nov 17, 2011
 */
public class CommandResponse {
	private StringBuilder successResponse;
	private Boolean failed = false;
	private StringBuilder failureResponse;
	private Throwable failureCause;
	private int commandExitValueObtained;
	
	/**
	 * @return the commandExitValueObtained
	 */
	public int getCommandExitValueObtained() {
		return commandExitValueObtained;
	}

	/**
	 * @param commandExitValueObtained the commandExitValueObtained to set
	 */
	public void setCommandExitValueObtained(int commandExitValueObtained) {
		this.commandExitValueObtained = commandExitValueObtained;
	}

	public CommandResponse gatherSuccessResponse(String responsePart) {
		if (this.successResponse == null) {
			this.successResponse = new StringBuilder();
		}
		this.successResponse.append(responsePart);
		return this;
	}

	public CommandResponse gatherFailureResponse(String responsePart) {
		if (this.failureResponse == null) {
			this.failureResponse = new StringBuilder();	
			this.failed = true;
		}
		this.failureResponse.append(responsePart);
		return this;
	}
	
	public CommandResponse markFailure() {
		this.failed = true;
		return this;
	}
	
	public CommandResponse storeFailureCause(Throwable cause) {
		this.failed = true;
		this.failureCause = cause;
		return this;
	}
	
	public Boolean hasCommandFailed() {
		return failed;
	}

	/**
	 * Can be null if no successResponse has been gathered.
	 * @return the successResponse
	 */
	public StringBuilder getSuccessResponse() {
		return this.successResponse;
	}
	
	/**
	 * @return the failureResponse
	 */
	public StringBuilder getFailureResponse() {
		return failureResponse;
	}

	/**
	 * @return the failureCause
	 */
	public Throwable getFailureCause() {
		return failureCause;
	}

	/**
	 * Can be expensive if the command has a large successResponse output. Use wisely in logging.
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (successResponse != null ? getSuccessResponse().toString() : "success::null") 
				+ "\n" + (failureResponse != null ? getFailureResponse().toString() : "failure:null");
	}

}
