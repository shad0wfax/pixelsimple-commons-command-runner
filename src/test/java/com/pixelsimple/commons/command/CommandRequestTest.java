/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.junit.Test;

import com.pixelsimple.commons.util.OSUtils;

import junit.framework.Assert;

/**
 *
 * @author Akshay Sharma
 * Apr 6, 2012
 */
public class CommandRequestTest {

	@Test
	public void testAddArguments() {
		CommandRequest request = CommandUtilForTest.simpleCommand();
		Assert.assertEquals(request.getCommandAsString(), "ls -l");
		
		request.addArgument("a");
		Assert.assertEquals(request.getCommandAsString(), "ls -l a");
		
		String spaceSepartedComment = "this has space";		
		request.addArgument(spaceSepartedComment);
		Assert.assertEquals(request.getCommandAsString(), "ls -l a this has space");

		request = CommandUtilForTest.simpleCommand();
		Assert.assertEquals(request.getCommandAsString(), "ls -l");
		request.addArgument(null);
		Assert.assertEquals(request.getCommandAsString(), "ls -l");
		
		request = CommandUtilForTest.simpleCommand();
		request.addArgument("a");
		spaceSepartedComment = "this has space";		
		request.addArgument(spaceSepartedComment);
		Assert.assertEquals(request.doesArgumentExist("a", false), true);
		Assert.assertEquals(request.doesArgumentExist("A", true), true);
		Assert.assertEquals(request.doesArgumentExist("A", false), false);
		Assert.assertEquals(request.doesArgumentExist("this has SPACE", true), true);
		Assert.assertEquals(request.doesArgumentExist("this", true), false);
	}

	@Test
	public void createChainedCommand() {
		CommandRequest req = new CommandRequest();
		req.addCommandToRunFromShell("ls", 0);
		req.addArgument("-l");
		
		if (OSUtils.isWindows()) {
			req.addArgument("&");
		} else {
			req.addArgument(";");
		}
		req.addArgument("ps");
		
		if (OSUtils.isWindows()) {
			Assert.assertEquals(req.getCommandAsString(), "cmd /c ls -l & ps");
		} else {
			Assert.assertEquals(req.getCommandAsString(), "/bin/sh -c ls -l ; ps");
		}
	}

	@Test
	public void createChainedCommandWithSpace() {
		CommandRequest req = new CommandRequest();
		req.addCommandToRunFromShell("my awesome command", 0);
		req.addArgument("-l");
		
		if (OSUtils.isWindows()) {
			req.addArgument("&");
		} else {
			req.addArgument(";");
		}
		req.addArgument("ps");
		
		if (OSUtils.isWindows()) {
			Assert.assertEquals(req.getCommandAsString(), "cmd /c \"my awesome command\" -l & ps");
		} else {
			Assert.assertEquals(req.getCommandAsString(), "/bin/sh -c \"my awesome command\" -l ; ps");
		}
		
		// A lil more complex
		req.addArgument("|");
		req.addNextChainedCommand("even more kickass command");
		req.addArgument("yo");
		if (OSUtils.isWindows()) {
			Assert.assertEquals(req.getCommandAsString(), "cmd /c \"my awesome command\" -l & ps | \"even more kickass command\" yo");
		} else {
			Assert.assertEquals(req.getCommandAsString(), "/bin/sh -c \"my awesome command\" -l ; ps | \"even more kickass command\" yo");
		}
	}


}
