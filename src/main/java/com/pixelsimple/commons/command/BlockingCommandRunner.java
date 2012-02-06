/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.slf4j.LoggerFactory;

/**
 * Use this when the command being executed is known to run very quickly and return short responses. 
 * Ex: Use this for querying meta info on a media file using "ffmpeg -i FILE_NAME" command. But do not use this for 
 * for actually running the transcoding. 
 * 
 * Doesn't work well with CommandStreamReader handler (deadlocks). Use the AsyncCommandRunner for CommandStreamReader.
 * Works well for CommandResponseHandler handler.
 *
 * @author Akshay Sharma
 * @Nov 11, 2011
 */
class BlockingCommandRunner extends AbstractCommandRunner {
	static {
		 LOG = LoggerFactory.getLogger(BlockingCommandRunner.class);

	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.AbstractCommandRunner#executeCommand(org.apache.commons.exec.Executor, org.apache.commons.exec.CommandLine)
	 */
	@Override
	protected void executeCommand(Executor executor, CommandLine cmdLine) throws ExecuteException, IOException {
		LOG.debug("Starting from BlockingCommandRunner - {}", cmdLine.toString());
		
		int exitVal = executor.execute(cmdLine);
		LOG.debug("Exit value from running the command- {}", exitVal);

		this.commandResponse.setCommandExitValueObtained(exitVal);
		
		LOG.debug("Submitted with BlockingCommandRunner - {}", cmdLine.toString());
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.AbstractCommandRunner#handleError(java.lang.Throwable)
	 */
	@Override
	protected void handleError(Throwable t) {
		this.commandResponse.markFailure().gatherFailureResponse("Error running task BlockingCommandRunner for command line: " 
				+ this.commandRequest.getCommand()).storeFailureCause(t);
	}
	
}
