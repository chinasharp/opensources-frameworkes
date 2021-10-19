package org.opensourceframework.starter.fastdfs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class FileInputStream extends InputStream {

	private final InputStream in;
	private final HttpURLConnection conn;

	@Override
	public int read() throws IOException {
		return in.read();
	}

	public FileInputStream(InputStream in, HttpURLConnection conn) {
		this.in = in;
		this.conn = conn;
	}

	@Override
    public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
    public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
    public void close() throws IOException {
		in.close();
		conn.disconnect();
	}
}
