/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.exec.ExecuteException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.test.util.TestUtil;

/**
 *
 * @author Akshay Sharma
 * Dec 2, 2011
 */
public class BlockingCommandRunnerTest extends TestCase {
	static final Logger LOG = LoggerFactory.getLogger(BlockingCommandRunnerTest.class);

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 */
	public void testRunCommandSimpleSuccess() {
		BlockingCommandRunner runner = new BlockingCommandRunner();
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.simpleCommand();
		
		// TODO: This will block - how to test the blockage???
		runner.runCommand(request, response);
		
		Assert.assertNotNull(response.getSuccessResponseOutputStream());
		Assert.assertNull(response.getFailureResponse());
		Assert.assertEquals(response.getCommandExitValueObtained(), request.getCommandExitValue());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.FALSE);
		
		// TODO: Add more meaningful assert - how to assert the output?
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 */
	public void testRunChainedCommandSuccess() {
		BlockingCommandRunner runner = new BlockingCommandRunner();
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.chainedCommand();
		
		// TODO: This will block - how to test the blockage???
		runner.runCommand(request, response);
		
		Assert.assertNotNull(response.getSuccessResponseOutputStream());
		Assert.assertNull(response.getFailureResponse());
		Assert.assertEquals(response.getCommandExitValueObtained(), request.getCommandExitValue());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.FALSE);
		
		// TODO: Add more meaningful assert - how to assert the output?
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 */
	public void testRunCommandSimpleFailureWithWrongReturnCodeExecuteException() {
		BlockingCommandRunner runner = new BlockingCommandRunner();
		CommandResponse response = new CommandResponse();
		CommandRequest request = CommandUtilForTest.simpleCommandWithWrongExitValue();
		
		// TODO: This will block - how to test the blockage???
		runner.runCommand(request, response);
		
		// Since the error occurs after the command has run for a while, the success response is NOT NULL. 
//		Assert.assertNull(response.getSuccessResponse());
		Assert.assertNotNull(response.getFailureResponse());
		Assert.assertEquals(response.hasCommandFailed(), Boolean.TRUE);
		Assert.assertNotNull(response.getFailureCause());
		Assert.assertEquals(response.getFailureCause().getClass(), ExecuteException.class);
		
		// TODO: Add more meaningful assert - how to assert the output?
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.command.AbstractCommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)}.
	 */
	public void testRunCommandNullCommandRequestAndResponse() {
		BlockingCommandRunner runner = new BlockingCommandRunner();

		try {
			runner.runCommand(null, new CommandResponse());
			fail();
		} catch (IllegalStateException e) {
			Assert.assertTrue("Got the exception, good to go", true);
		}
		
		try {
			runner.runCommand(new CommandRequest(), null);
			fail();
		} catch (IllegalStateException e) {
			Assert.assertTrue("Got the exception, good to go", true);
		}
		
	}

	
	@Test
	public void testRunCommandWithSpaceSuccess() {
		String mediaPath = TestUtil.getTestConfig().get(TestUtil.TEST_ARTIFACT_DIR_CONFIG) + "with space in folder name/video1.mov";
		
		if (TestUtil.fileExists(mediaPath)) {
			BlockingCommandRunner runner = new BlockingCommandRunner();
			CommandResponse response = new CommandResponse();
			CommandRequest request = new CommandRequest();
			
			request.addCommand( TestUtil.getTestConfig().get(TestUtil.FFPROBE_EXECUTABLE_CONFIG), 0);
			request.addArgument(mediaPath);
			request.addArgument("-show_format");
			request.addArgument("-show_streams");
			request.addArgument("-sexagesimal");
			
			runner.runCommand(request, response);
			
			Assert.assertNotNull(response.getSuccessResponseOutputStream());
			Assert.assertNull(response.getFailureResponse());
			Assert.assertEquals(response.getCommandExitValueObtained(), request.getCommandExitValue());
			Assert.assertEquals(response.hasCommandFailed(), Boolean.FALSE);
		} else {
			LOG.error("testRunCommandWithSpaceSuccess::Did not find the media to test with, logging and passing the test.");
			Assert.assertTrue(true);
		}
	}
}
