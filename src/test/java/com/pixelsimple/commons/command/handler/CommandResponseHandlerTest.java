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

	/**
	 * Test method for {@link com.pixelsimple.commons.command.handler.CommandResponseHandler#processLine(java.lang.String, int)}.
	 */
	public void testProcessLine() {
		// Test that logging works!! Thats all
		CommandResponse response = new CommandResponse();	
		CommandResponseHandler handler = new CommandResponseHandler(response);
		handler.processLine("hello world", 1);
		LOG.debug("Response form Command::{}", response.getSuccessResponse());
		Assert.assertTrue(true);
	}

}
