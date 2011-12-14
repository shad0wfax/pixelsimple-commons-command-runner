/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.command.handler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;

/**
 * A Stream reader using datainputstream that reads of a pipedinputstream. Not an elegant approach as the writer 
 * thread could die once done, and the reader thread receives an IOEXception with the write-end-dead message: 
 * http://techtavern.wordpress.com/2008/07/16/whats-this-ioexception-write-end-dead/
 * 
 * The better and cleaner approach is to use the CommandResponseHandler class, which uses the logoutputstream to do all this grunt work.
 * 
 * Usage: <code>
 *			PipedOutputStream output = new PipedOutputStream();
 *			PumpStreamHandler psh = new PumpStreamHandler(output);
 *			CommandStreamReader reader = new CommandStreamReader(output, commandResponse);
 *			executor.setStreamHandler(psh);
 *			
 *			Thread readerThread = new Thread(reader);
 * 			readerThread.start();
 *  
 * @deprecated - Just a testing / poc approach. The better and cleaner approach is to use the CommandResponseHandler
 * @author Akshay Sharma
 * @Nov 13, 2011
 */
public class CommandStreamReader implements Runnable {
	static final Logger LOG = LoggerFactory.getLogger(CommandStreamReader.class);
			
	private PipedOutputStream pos;
	private CommandResponse response;
	
	/**
	 * 
	 */
	public CommandStreamReader(PipedOutputStream pos, CommandResponse response) {
		this.pos = pos;
		this.response = response;
	}

	
//	private static final int READ_BUFFER_SIZE = PipedInputStream;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		BufferedReader br = null;
		DataInputStream dis = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (pos != null)
			{
				dis = new DataInputStream(new PipedInputStream(pos));
				br = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
				String line = null;
				
				while ((line = br.readLine()) != null)
				{
					sb.append(line);
					response.gatherSuccessResponseOutputStream(line + "\n");
					LOG.debug("{}", line);
				}
				
			}
			
		} catch (Exception e) {
			
			LOG.error("Error occurred", e);
			
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (dis != null) {
					dis.close();
				}
			} catch (IOException e) {
				LOG.error("Error occurred", e);
			}
				
		}
	}
}
