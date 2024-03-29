/**
 * � PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;
import com.pixelsimple.commons.util.OSUtils;

/**
 *
 * @author Akshay Sharma
 * Dec 2, 2011
 */
public class CommandUtilForTest extends TestCase {
	static final Logger LOG = LoggerFactory.getLogger(CommandUtilForTest.class);
	
	public void testDummy() {
		Assert.assertTrue("Just a dummy test that needs to be there in this class for junit. DO NOT REMOVE.", true);
	}

	public static AsyncCallbackHandler anonAsyncHandler(final String optionalCustomMessage) {
		return (new AsyncCallbackHandler() {
			
			@Override
			public void onCommandFailed(CommandRequest commandRequest, CommandResponse commandResponse) {
				LOG.debug("testNewAsyncCommandRunner::Command Failed");
				
				// A hack to allow for testing - not to be done ever!! 
				commandResponse.gatherFailureResponse(optionalCustomMessage);
			}
			
			@Override
			public void onCommandComplete(CommandRequest commandRequest, CommandResponse commandResponse) {
				LOG.debug("testNewAsyncCommandRunner::Command Succeeded");

				// A hack to allow for testing - not to be done ever!! 
				commandResponse.gatherSuccessResponseOutputStream(optionalCustomMessage);
			}

			@Override
			public void onCommandStart(CommandRequest commandRequest, CommandResponse commandResponse) {
				LOG.debug("testNewAsyncCommandRunner::Command Started - {}", commandRequest.getCommandAsString());
			}
		});
	}
	
	public static CommandRequest simpleCommand() {
		CommandRequest request = new CommandRequest();
		request.addCommand("ls", 0);
		request.addArgument("-l");
	
//		request.addCommand("cmd", 0).addArgument("/c").addArgument("echo").addArgument("\"\"").addArgument(">").addArgument("Z:\\Downloads\\Nova\\hls_transcode.complete");

		return request;
	}
	
	/**
	 * Very well known that ls -l command should exit with 0. If it is to exit with 1, it throws an exception. 
	 * @return
	 */
	public static CommandRequest simpleCommandWithWrongExitValue() {
		CommandRequest request = new CommandRequest();
		request.addCommand("ls", 1);
		request.addArgument("-l");
		return request;
	}
	

	public static CommandRequest chainedCommand() {
		CommandRequest request = new CommandRequest();
		request.addCommandToRunFromShell("ls", 0).addArgument("-l");
		
		if (OSUtils.isWindows()) {
			request.addArgument("&");	
		} else {
			request.addArgument(";");	
		}
		request.addArgument("ps");

		return request;
	}
	
}
