/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

/**
 *
 * @author Akshay Sharma
 * Nov 17, 2011
 */
public class CommandRequest {
	private String command;
	private int commandExitValue = 0;
	
	public CommandRequest addCommand(String command, int commandExitValue) {
		this.command = command;
		this.commandExitValue = commandExitValue;
		return this;
	}
	
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the commandExitValue
	 */
	public int getCommandExitValue() {
		return commandExitValue;
	}
}
