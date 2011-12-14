/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command.handler;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.command.handler.CommandResponseHandler;

/**
 *
 * @author Akshay Sharma
 * Nov 18, 2011
 */
public class CommandResponseHandlerTest extends TestCase {
	static final Logger LOG = LoggerFactory.getLogger(CommandResponseHandlerTest.class);
	private static final String NEW_LINE_CHARACTER = System.getProperty("line.separator");

	/**
	 * Test method for {@link com.pixelsimple.commons.command.handler.CommandResponseHandler#processLine(java.lang.String, int)}.
	 */
	public void testProcessLineOutputStream() {
		// Test that logging works!! Thats all
		CommandResponse response = new CommandResponse();	
		CommandResponseHandler handler = new CommandResponseHandler(response, false);
		handler.processLine("hello world", 1);
		LOG.debug("Response form Command::{}", response.getSuccessResponseOutputStream());
		
		Assert.assertEquals(response.getSuccessResponseOutputStream().toString(), "hello world" + NEW_LINE_CHARACTER);
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.handler.CommandResponseHandler#processLine(java.lang.String, int)}.
	 */
	public void testProcessLineErrorStream() {
		// Test that logging works!! Thats all
		CommandResponse response = new CommandResponse();	
		CommandResponseHandler handler = new CommandResponseHandler(response, true);
		handler.processLine("hello world", 1);
		LOG.debug("Response form Command::{}", response.getSuccessResponseErrorStream());
		
		Assert.assertEquals(response.getSuccessResponseErrorStream().toString(), "hello world" + NEW_LINE_CHARACTER);
	}

}
