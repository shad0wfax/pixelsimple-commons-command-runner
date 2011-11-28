/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;

/**
 *
 * @author Akshay Sharma
 * Nov 23, 2011
 */
public class CommandRunnerFactory {
	static final Logger LOG = LoggerFactory.getLogger(CommandRunnerFactory.class);
	
	private CommandRunnerFactory() {
	}
	
	public static CommandRunner newBlockingCommandRunner() {
		return new BlockingCommandRunner();
	}

	public static CommandRunner newAsyncCommandRunner(AsyncCallbackHandler optionalAsyncCallbackHandler) {
		return new AsyncCommandRunner(optionalAsyncCallbackHandler);
	}
	
}
