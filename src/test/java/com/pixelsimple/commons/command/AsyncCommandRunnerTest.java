/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.apache.commons.exec.ExecuteException;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Akshay Sharma
 * Dec 2, 2011
 */
public class AsyncCommandRunnerTest extends TestCase {

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 * @throws InterruptedException 
	 */
	public void testRunCommandSimpleSuccessWithNoCallback() throws InterruptedException {
		AsyncCommandRunner runner = new AsyncCommandRunner(null);
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.simpleCommand();
		
		// TODO: This will NOT block - how to test the non-blockage???
		runner.runCommand(request, response);
		// Wait 2s to see if the output is gathered. Else tweak this number. Junit exits unfortunately before other 
		// thread finishes. 
		Thread.sleep(2000);
		
		Assert.assertNotNull(response.getSuccessResponse());
		Assert.assertNull(response.getFailureResponse());
		Assert.assertEquals(response.getCommandExitValueObtained(), request.getCommandExitValue());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.FALSE);
		
		// TODO: Add more meaningful assert - how to assert the output in async mode?
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 * @throws InterruptedException 
	 */
	public void testRunCommandSimpleSuccessWithCallback() throws InterruptedException {
		String msg = "This command succeeded correctly - coming from this test run.!";
		AsyncCommandRunner runner = new AsyncCommandRunner(CommandUtilForTest.anonAsyncHandler(msg));
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.simpleCommand();
		
		// TODO: This will NOT block - how to test the non-blockage???
		runner.runCommand(request, response);
		// Wait 2s to see if the output is gathered. Else tweak this number. Junit exits unfortunately before other 
		// thread finishes. 
		Thread.sleep(2000);
		
		Assert.assertNotNull(response.getSuccessResponse());
		Assert.assertNull(response.getFailureResponse());
		Assert.assertEquals(response.getCommandExitValueObtained(), request.getCommandExitValue());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.FALSE);
		Assert.assertTrue(response.getSuccessResponse().indexOf(msg) != -1);
		
		// TODO: Add more meaningful assert - how to assert the output in async mode?
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 */
	public void testRunCommandSimpleFailureWithWrongReturnCodeExecuteException() throws InterruptedException {
		String msg = "This command succeeded correctly - coming from this test run.!";
		AsyncCommandRunner runner = new AsyncCommandRunner(CommandUtilForTest.anonAsyncHandler(msg));
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.simpleCommandWithWrongExitValue();
		
		// TODO: This will block - how to test the blockage???
		runner.runCommand(request, response);
		// Wait 2s to see if the output is gathered. Else tweak this number. Junit exits unfortunately before other 
		// thread finishes. 
		Thread.sleep(2000);
		
		// Since the error occurs after the command has run for a while, the success response is NOT NULL. 
//		Assert.assertNull(response.getSuccessResponse());
		Assert.assertNotNull(response.getFailureResponse());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.TRUE);
		Assert.assertNotNull(response.getFailureCause());
		Assert.assertEquals(response.getFailureCause().getClass(), ExecuteException.class);
		Assert.assertTrue(response.getFailureResponse().indexOf(msg) != -1);
		
		// TODO: Add more meaningful assert - how to assert the output?
	}

	
}
