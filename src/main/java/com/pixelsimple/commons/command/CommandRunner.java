/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

/**
 *
 *
 * @author Akshay Sharma
 * @Nov 11, 2011
 */
public interface CommandRunner {

	void runCommand(CommandRequest commandRequest, CommandResponse commandResponse);
	
}
