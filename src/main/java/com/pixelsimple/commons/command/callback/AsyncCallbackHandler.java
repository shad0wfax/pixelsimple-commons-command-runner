/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command.callback;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;

/**
 *
 * @author Akshay Sharma
 * Nov 25, 2011
 */
public interface AsyncCallbackHandler {
	
	void onCommandStart(CommandRequest commandRequest, CommandResponse commandResponse);
	
	void onCommandComplete(CommandRequest commandRequest, CommandResponse commandResponse);
	
	void onCommandFailed(CommandRequest commandRequest, CommandResponse commandResponse);

}
