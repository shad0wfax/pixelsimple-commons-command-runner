/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Akshay Sharma
 * Dec 1, 2011
 */
public class CommandRunnerFactoryTest extends TestCase {
	static final Logger LOG = LoggerFactory.getLogger(CommandRunnerFactoryTest.class);

	/**
	 * Test method for {@link com.pixelsimple.commons.command.CommandRunnerFactory#newBlockingCommandRunner()}.
	 */
	public void testNewBlockingCommandRunner() {
		CommandRunner runner = CommandRunnerFactory.newBlockingCommandRunner();
		Assert.assertNotNull(runner);
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.CommandRunnerFactory#newAsyncCommandRunner(com.pixelsimple.commons.command.callback.AsyncCallbackHandler)}.
	 */
	public void testNewAsyncCommandRunner() {
		CommandRunner runner = CommandRunnerFactory.newAsyncCommandRunner(null);
		Assert.assertNotNull(runner);
		
		runner = CommandRunnerFactory.newAsyncCommandRunner(CommandUtilForTest.anonAsyncHandler(null));
		Assert.assertNotNull(runner);
	}
	

}
