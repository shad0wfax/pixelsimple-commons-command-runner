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
	private StringBuilder successResponseOutputStream;
	private StringBuilder successResponseErrorStream;
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

	public CommandResponse gatherSuccessResponseOutputStream(String responsePart) {
		if (this.successResponseOutputStream == null) {
			this.successResponseOutputStream = new StringBuilder();
		}
		this.successResponseOutputStream.append(responsePart);
		return this;
	}
	
	public CommandResponse gatherSuccessResponseErrorStream(String responsePart) {
		if (this.successResponseErrorStream == null) {
			this.successResponseErrorStream = new StringBuilder();
		}
		this.successResponseErrorStream.append(responsePart);
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
	 * Can be null if no successResponseOutputStream has been gathered.
	 * @return the successResponseOutputStream
	 */
	public StringBuilder getSuccessResponseOutputStream() {
		return this.successResponseOutputStream;
	}
	
	/**
	 * Can be null if no successResponseErrorStream has been gathered.
	 * @return the successResponseErrorStream
	 */
	public StringBuilder getSuccessResponseErrorStream() {
		return this.successResponseErrorStream;
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
	 * Can be expensive if the command has a large successResponseOutputStream/successResponseErrorStream output. 
	 * Use wisely in logging.
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "successOutputStream::" + (successResponseOutputStream != null ? getSuccessResponseOutputStream().toString() : "null")
			+ "\nsuccessErrorStream::" + (successResponseErrorStream != null ? getSuccessResponseErrorStream().toString() : "null")
			+ "\nfailure::" + (failureResponse != null ? getFailureResponse().toString() : "null");
	}

}
