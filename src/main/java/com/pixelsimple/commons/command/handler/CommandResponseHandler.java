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

	private CommandResponse response;

	public CommandResponseHandler(CommandResponse response) {
		this.response = response;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.exec.LogOutputStream#processLine(java.lang.String, int)
	 */
	@Override
	protected void processLine(String line, int level) {
		response.gatherSuccessResponse(line);
		LOG.debug("{}", line);
	}

}
