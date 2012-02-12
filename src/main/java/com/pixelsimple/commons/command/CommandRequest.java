/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.apache.commons.exec.CommandLine;

/**
 *
 * @author Akshay Sharma
 * Nov 17, 2011
 */
public final class CommandRequest {
	private CommandLine commandLine;
	private int commandExitValue = 0;
	
	public CommandRequest addCommand(String command, int commandExitValue) {
		this.commandLine = CommandLine.parse(command);
		this.commandExitValue = commandExitValue;
		return this;
	}
	
	public CommandRequest addArguments(String argument) {
		if (argument != null)
			this.commandLine.addArgument(argument);
		return this;
	}
	
	/**
	 * @return the command
	 */
	public CommandLine getCommandLine() {
		return this.commandLine;
	}

	/**
	 * @return the command
	 */
	public String getCommandAsString() {
		return this.commandLine.toString();
	}

	/**
	 * @return the commandExitValue
	 */
	public int getCommandExitValue() {
		return commandExitValue;
	}
}
