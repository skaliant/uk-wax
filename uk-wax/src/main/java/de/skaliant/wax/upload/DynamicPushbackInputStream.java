package de.skaliant.wax.upload;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Pushback input stream which is able to increase its buffer if necessary, in
 * contrast to java.io.PushbackInputStream
 *
 * @author Udo Kastilan
 */
class DynamicPushbackInputStream extends FilterInputStream {
	private byte[] buf = null;
	private int size = 0;
	private int pos = 0;


	/**
	 * Creates a new pushback input stream starting with buffer size
	 * <code>size</code>. Every time the buffer size is insufficient while
	 * unreading, this class will increase its buffer size by this value.
	 * 
	 * @param in
	 *          Underlying input stream
	 * @param size
	 *          Initial buffer size and increment value for further buffer
	 *          allocation
	 */
	DynamicPushbackInputStream(InputStream in, int size) {
		super(in);
		this.size = size;
		buf = new byte[size];
	}


	/**
	 * Unreads one byte. This value will be the next one to be read.
	 * 
	 * @param b
	 */
	public void unread(int b) {
		if (pos == buf.length) {
			byte[] na = new byte[buf.length + size];

			System.arraycopy(buf, 0, na, 0, buf.length);
			buf = na;
		}
		buf[pos++] = (byte) b;
	}


	@Override
	public int read()
		throws IOException {
		int b = -1;

		if (pos > 0) {
			b = 0x00ff & buf[--pos];
		} else {
			b = in.read();
		}
		return b;
	}
}
