/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.callback.AsyncCallbackHandler;
import com.pixelsimple.commons.command.callback.DefaultAsyncCallbackHandler;

/**
 *
 * @author Akshay Sharma
 * Jul 11, 2012
 */
public class AsyncCommandRunnerIgnoresStreamCapture implements CommandRunner {
	private static Logger LOG = LoggerFactory.getLogger(AsyncCommandRunnerIgnoresStreamCapture.class);
	
	private CommandResponse commandResponse;
	private CommandRequest commandRequest;

	private AsyncCallbackHandler asyncCallbackHandler;
	
	public AsyncCommandRunnerIgnoresStreamCapture(AsyncCallbackHandler optionalAsyncCallbackHandler) {
		this.asyncCallbackHandler = optionalAsyncCallbackHandler;
		
		if (this.asyncCallbackHandler == null) {
			this.asyncCallbackHandler = new DefaultAsyncCallbackHandler();
		}
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.command.CommandRunner#runCommand(com.pixelsimple.commons.command.CommandRequest, com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public void runCommand(CommandRequest commandRequest, CommandResponse commandResponse) {
		if (commandRequest == null || commandResponse == null) {
			throw new IllegalStateException("Pass in a valid and not null CommandRequest and CommandResponse.");
		}
		this.commandResponse = commandResponse;
		this.commandRequest = commandRequest;
		
		CommandLine cmdLine = commandRequest.getCommandLine();
		
		LOG.debug("executeCommand from AsyncCommandRunner - {}, {}", cmdLine.toString(), new Date());
		ExecutorService executorThread = Executors.newSingleThreadExecutor();
		AsyncCommandCaller task = new AsyncCommandCaller(cmdLine);
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
		private CommandLine cmdLine;
		
		public AsyncCommandCaller(CommandLine cmdLine) {
			this.cmdLine = cmdLine;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Process pro = null;
			IgnoreInputStreamReader reader = null;
			Thread readerThread = null;
			try {
				asyncCallbackHandler.onCommandStart(commandRequest, commandResponse);
				
				List<String> args = new ArrayList<String>();
				
				// Add all args
				args.addAll(Arrays.asList(cmdLine.toStrings()));
				
				ProcessBuilder pb = new ProcessBuilder(args);
				pb.redirectErrorStream(true);
				LOG.debug("Starting from AsyncCommandCaller - cmdline passed {}\nprocess builder using", 
						cmdLine.toString(), pb.toString());

				pro = pb.start();
				InputStream is = pro.getInputStream();
				reader = new IgnoreInputStreamReader(is);
				readerThread = new Thread(reader);
				readerThread.start();

				// Taken from commons-exec
				int exitVal = 0xdeadbeef;
				
				try {
					exitVal = pro.waitFor();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
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
			} finally {
				if (pro != null)
					closeStreams(pro);
				if (readerThread != null && readerThread.isAlive() && reader != null)
					reader.end();
					
			}
		}
		
		private void closeStreams(final Process process) {
		    try {
		        process.getInputStream().close();
		    }
		    catch(IOException e) {
		    	// Ignore
		    }
		
		    try {
		        process.getOutputStream().close();
		    }
		    catch(IOException e) {
		    	// Ignore
		    }
		
		    try {
		        process.getErrorStream().close();
		    }
		    catch(IOException e) {
		    	// Ignore
		    }
		}
	}

	private void handleError(Throwable t) {
		// Retry with capturing errors 
		LOG.error("Encountered an error running AsyncCommandRunnerIgnoresStreamCapture, so will run it using with stream capture");
		CommandRunnerFactory.newAsyncCommandRunner(asyncCallbackHandler, false).runCommand(commandRequest, commandResponse);	
	}
	
	/**
	 * thread to read output from child
	 */
	class IgnoreInputStreamReader implements Runnable {
	    private final InputStream is;
	    private boolean end;

	    IgnoreInputStreamReader(InputStream is) {
	        this.is = is;
	    }
	    public void run() {
	    	BufferedReader br = null;	    	
	        try {
	            br = new BufferedReader( new InputStreamReader( is ));
	            while (br.readLine() != null && !end) {// Ignore output.
	            }
	            br.close();
	        } catch (IOException e) {
	            throw new IllegalArgumentException( "IOException receiving data from child process." );
	        } finally {
	        	if (br != null)
					try {
						br.close();
					} catch (IOException e) {
						// ignore as usual!
					}
	        }
	    }
		/**
		 * @param end the end to set
		 */
		public void end() {
			this.end = true;
		}
	}
}
