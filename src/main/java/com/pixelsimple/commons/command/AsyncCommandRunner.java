/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;
import com.pixelsimple.commons.command.callback.DefaultAsyncCallbackHandler;




/**
 * 
 * Note: Non-public class. Other packages cannot get hold of this class directly. Get a reference via the Factory.
 *
 * @author Akshay Sharma
 * @Nov 11, 2011
 */
class AsyncCommandRunner extends AbstractCommandRunner {
	static {
		 LOG = LoggerFactory.getLogger(AsyncCommandRunner.class);

	}
	
	private AsyncCallbackHandler asyncCallbackHandler;
	
	public AsyncCommandRunner(AsyncCallbackHandler optionalAsyncCallbackHandler) {
		this.asyncCallbackHandler = optionalAsyncCallbackHandler;
		
		if (this.asyncCallbackHandler == null) {
			this.asyncCallbackHandler = new DefaultAsyncCallbackHandler();
		}
	}

	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.AbstractCommandRunner#executeCommand()
	 */
	@Override
	protected void executeCommand(Executor executor, CommandLine cmdLine) throws ExecuteException, IOException {
		LOG.debug("executeCommand from AsyncCommandRunner - {}, {}", cmdLine.toString(), new Date());
		ExecutorService executorThread = Executors.newSingleThreadExecutor();
		AsyncCommandCaller task = new AsyncCommandCaller(executor, cmdLine);
		executorThread.submit(task);
		executorThread.shutdown();
		LOG.debug("executeCommand from AsyncCommandRunner, delegated task. Now returning the main thread");
	}
	
	/**
	 * Inner class thread to run the command.
	 *
	 * @author Akshay Sharma
	 * Nov 14, 2011
	 */
	final class AsyncCommandCaller implements Runnable {
		private Executor executor;
		private CommandLine cmdLine;
		
		public AsyncCommandCaller(Executor executor, CommandLine cmdLine) {
			this.executor = executor;
			this.cmdLine = cmdLine;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				LOG.debug("Starting from AsyncCommandCaller - {}", cmdLine.toString());
				
				DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
				this.executor.execute(cmdLine, resultHandler);
				
				//boolean exitValue = resultHandler.hasResult();
				LOG.debug("Execute called with AsyncCommandCaller");
			
				// TODO: Suppose we want to wait for the results, the calling thread can be made to wait (Not the thread that 
				// is running the process, but the thread that called AsyncCommandRunner that will be made to wait.
				resultHandler.waitFor();
				
				int exitVal = resultHandler.getExitValue();
				
				LOG.debug("The exit value obtained running the command:: {}", exitVal);
				commandResponse.setCommandExitValueObtained(exitVal);
				
				if (exitVal != commandRequest.getCommandExitValue()) {
					throw new ExecuteException("Process exited with an error: " + exitVal, exitVal);
				}

				LOG.debug("Waited so long with AsyncCommandCaller, final response is - {}. Now going to run callback handler.",
					commandResponse);
				
				// Run the command complete handler once the results have been accumulated. 
				asyncCallbackHandler.onCommandComplete(commandRequest, commandResponse);
			} catch (Exception e) {
				handleError(e);
				LOG.debug("Error running task AsyncCommandCaller for command line: - {}", cmdLine);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.AbstractCommandRunner#handleError(java.lang.Throwable)
	 */
	@Override
	protected void handleError(Throwable t) {
		this.commandResponse.markFailure().gatherFailureResponse("Error running task AsyncCommandCaller for command line: " 
			+ this.commandRequest.getCommand()).storeFailureCause(t);
			asyncCallbackHandler.onCommandFailed(this.commandRequest, this.commandResponse);
	}
	
}
