/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;

import com.pixelsimple.commons.command.handler.CommandResponseHandler;

/**
 *
 * @author Akshay Sharma
 * Nov 23, 2011
 */
abstract class AbstractCommandRunner implements CommandRunner {

	protected static Logger LOG;
	protected CommandResponse commandResponse;
	protected CommandRequest commandRequest;

	@Override
	public void runCommand(CommandRequest commandRequest, CommandResponse commandResponse) {
		
		if (commandRequest == null || commandResponse == null) {
			throw new IllegalStateException("Pass in a valid and not null CommandRequest and CommandResponse.");
		}
		this.commandResponse = commandResponse;
		this.commandRequest = commandRequest;
		
		CommandLine cmdLine = CommandLine.parse(commandRequest.getCommand());
		Executor executor = new DefaultExecutor();
		executor.setExitValue(commandRequest.getCommandExitValue());
		
		try {
			PumpStreamHandler psh = new PumpStreamHandler(new CommandResponseHandler(commandResponse));
			executor.setStreamHandler(psh);
			this.executeCommand(executor, cmdLine);
			
			LOG.debug("runCommand::Output from the command response at this point:: {}", commandResponse);
		} catch (ExecuteException e) {
			// TODO: decide if we need to retry? Or if the command is bad. Etc. Is this a recoverable error?
			this.handleError(e);
			LOG.error("Error occurred executing the command - [{}] ", commandRequest.getCommand(), e);
		} catch (IOException e) {
			// Is this recoverable? What to do? Decide
			this.handleError(e);
			LOG.error("IO Error occurred executing the command - [{}] ", commandRequest.getCommand(), e);
		}		
			
	}
	
	protected abstract void executeCommand(Executor executor, CommandLine cmdLine) throws ExecuteException, IOException;

	protected abstract void handleError(Throwable t);

}