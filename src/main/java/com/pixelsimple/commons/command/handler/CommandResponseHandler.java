/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command.handler;

import org.apache.commons.exec.LogOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;

/**
 *
 *
 * @author Akshay Sharma
 * @Nov 15, 2011
 */
public class CommandResponseHandler extends LogOutputStream {
	private static final Logger LOG = LoggerFactory.getLogger(CommandResponseHandler.class);
	private static final String NEW_LINE_CHARACTER = System.getProperty("line.separator");

	private CommandResponse response;
	private boolean useErrorStream;

	public CommandResponseHandler(CommandResponse response, boolean useErrorStream) {
		this.response = response;
		this.useErrorStream = useErrorStream;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.exec.LogOutputStream#processLine(java.lang.String, int)
	 */
	@Override
	protected void processLine(String line, int level) {
		LOG.debug("useErrorStream::{}==>{}", this.useErrorStream, line);
		
		if (this.useErrorStream) {
			response.gatherSuccessResponseErrorStream(line + NEW_LINE_CHARACTER);
		} else {
			
			response.gatherSuccessResponseOutputStream(line + NEW_LINE_CHARACTER);
		}
			
	}

}
