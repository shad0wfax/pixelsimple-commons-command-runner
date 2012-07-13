/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;

/**
 *
 * @author Akshay Sharma
 * Nov 23, 2011
 */
public class CommandRunnerFactory {
	static final Logger LOG = LoggerFactory.getLogger(CommandRunnerFactory.class);
	
	private CommandRunnerFactory() {
	}

	/**
	 * Returns a synchronous command runner. This command runner will block the calling thread until the command completes 
	 * and the streams have been captured.
	 * This command runner should be used primarily when the called commands are short and quick (ex: ffprobe calls). 
	 * For commands that are expected to run longer (say > 3-5s) it is better to use the newAsyncCommandRunner() call.
	 * Building the system with asynchronous calls is much better.   
	 * @return BlockingCommandRunner
	 */
	public static CommandRunner newBlockingCommandRunner() {
		return new BlockingCommandRunner();
	}

	/**
	 * Returns an asyncrhonous command runner. The command will be run in a new thread, thus not blocking the calling thread.
	 * The type returned depends on the ignoreStreamCapture value.
	 * The ignoreStreamCapture=true returns AsyncCommandRunnerIgnoresStreamCapture, which performs better than 
	 * the regular AsyncCommandRunner (when ignoreStreamCapture=false).
	 * The intent of ignoreStreamCapture is when the calling client doesn't care about the output response from the 
	 * command called (Ex: ffmpeg calls). If the calling client needs the response, then ignoreStreamCapture should be 
	 * false. 
	 * In case of an error AsyncCommandRunnerIgnoresStreamCapture would retry using the AsyncCommandRunner, which would
	 * capture the error trace. This is a built in mechanism to provide necessary logging when a command fails.
	 * 
	 * Additional details about a bug noticed when using AsyncCommandRunner - 
	 * Sporadically (especially on windows), it has been observed that AsyncCommandRunner doesn't complete the long running command,
	 * even though the underlying command/sub-process has completed. A high level investigation shows it might be due to the fact that
	 * the sub-process's outputstream hasn't been consumed completely. The thread reading this stream could be waiting and 
	 * thus not allowing the thread running the sub-process to complete. As a result, the call never returns from the thread and 
	 * subsequent actions (like notifying command completion/callbacks etc) are never invoked/completed. 
	 * AsyncCommandRunnerIgnoresStreamCapture tries to fix this issue by first using the better Java API - ProcessBuilder() 
	 * instead of Runtime.exec() to run the command. It also redirects the output and error stream to quickly read it using 
	 * one single thread. It ignores the read output (so not having to worry about the content). And, if the sub-process 
	 * completes, it quietly ignores the remaining stream data and proceeds to next steps. Thus, ensuring there is no random
	 * waits. If it encounters an error it will retry using AsyncCommandRunner just to capture the stream.
	 * 
	 * @param optionalAsyncCallbackHandler
	 * @param ignoreStreamCapture
	 * @return AsyncCommandRunner/AsyncCommandRunnerIgnoresStreamCapture
	 */
	public static CommandRunner newAsyncCommandRunner(AsyncCallbackHandler optionalAsyncCallbackHandler, 
			boolean ignoreStreamCapture) {
		
		if (ignoreStreamCapture && !LOG.isDebugEnabled())
			return new AsyncCommandRunnerIgnoresStreamCapture(optionalAsyncCallbackHandler);
		return new AsyncCommandRunner(optionalAsyncCallbackHandler);
	}
	
}
