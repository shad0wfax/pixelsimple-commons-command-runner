/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.apache.commons.exec.CommandLine;

import com.pixelsimple.commons.util.OSUtils;

/**
 *
 * @author Akshay Sharma
 * Nov 17, 2011
 */
public final class CommandRequest {
	private CommandLine commandLine;
	private int commandExitValue = 0;
	
	public CommandRequest addCommand(String command, int commandExitValue) {
		command = quote(command);
		this.commandLine = CommandLine.parse(command);
	
		this.commandExitValue = commandExitValue;
		return this;
	}

	/**
	 * This will create a new shell/command and run the command. This needs to be done if the commands are chained :
	 *  - either using &(win) / ; (mac) OR using | (pipes).
	 *  Ex: commands like: ls -l & ps -a OR ls -l ; ps -a OR ls -l | ps -a have to be run using this method.
	 *  This will be converted as follows:
	 *  
	 *  Win: cmd /c ls -l & ps -a
	 *  Mac: /bin/sh -c ls -l & ps -a
	 *  
	 * @param command
	 * @param commandExitValue
	 * @return
	 */
	public CommandRequest addCommandToRunFromShell(String startCommand, int commandExitValue) {
		startCommand = quote(startCommand);
		
		if (OSUtils.isWindows()) {
			this.addCommand("cmd", commandExitValue);
			this.addArgument("/c");
		} else {
			this.addCommand("/bin/sh", commandExitValue);
			this.addArgument("-c");
		}
		this.addArgument(startCommand);
		return this;
	}
	
	/**
	 * Use only when using chained commands.
	 * @param nextCommand
	 * @return
	 */
	public CommandRequest addNextChainedCommand(String nextCommand) {
		nextCommand = quote(nextCommand);
		this.addArgument(nextCommand);
		return this;
	}

	
	public CommandRequest addArgument(String argument) {
		if (argument != null) {
			
			// Looks like it should not be done if we pass false to handleQuote.
//			// quote the argument if there is a space in it
//			if (argument.indexOf(" ") != -1) {
//				argument = "\"" + argument + "\"";
//			}
			
			this.commandLine.addArgument(argument, false);
		}
		return this;
	}
	
	// quote the argument if there is a space in it	
	private String quote(String command) {
		// quote the argument if there is a space in it
		if (command != null && command.indexOf(" ") != -1) {
			command = "\"" + command + "\"";
		}
		return command;
	}
	
	/**
	 * Can be expensive on repeated invokes. Since commandLine does not give a good way to poll this. 
	 * @param argument
	 * @return
	 */
	public boolean doesArgumentExist(String argument, boolean ignoreCase) {
		String [] args = this.commandLine.getArguments();
		
		if (args == null || args.length == 0)
			return false;
		
		boolean exists = false;
		
		for (String s : args) {
			//if (s.equals(anObject))
			exists = (ignoreCase) ? s.equalsIgnoreCase(argument)	: s.equals(argument);
			
			if (exists)
				break;
		}
		
		return exists;
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
