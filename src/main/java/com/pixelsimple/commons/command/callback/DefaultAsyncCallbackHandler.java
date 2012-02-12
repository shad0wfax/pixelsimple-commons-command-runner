/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;

/**
 * A default Async Callback Handler implementation. This is a very dumbed down version that just logs the complete
 * and failure states. This is used only if the client doens't supply a callback handler. This is useful for logging 
 * purpose only.   
 * 
 * @author Akshay Sharma
 * Nov 25, 2011
 */
public class DefaultAsyncCallbackHandler implements AsyncCallbackHandler {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAsyncCallbackHandler.class);

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.callback.AsyncCallbackHandler#onCommandComplete(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public void onCommandComplete(CommandRequest commandRequest, CommandResponse commandResponse) {
		LOG.debug("Completed running the command - {} \n the output of the command \n:{}", commandRequest.getCommandAsString(), 
				commandResponse);		
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.callback.AsyncCallbackHandler#onCommandFailed(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public void onCommandFailed(CommandRequest commandRequest, CommandResponse commandResponse) {
		LOG.debug("Failed running the command - {} \n the output of the command \n:{}", commandRequest.getCommandAsString(), 
				commandResponse);
		LOG.debug("Failure cause:\n {}", commandResponse.getFailureCause());
	}

}
