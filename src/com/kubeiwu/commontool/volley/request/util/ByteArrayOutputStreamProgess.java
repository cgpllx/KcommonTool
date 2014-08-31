//2014-8-13
package com.kubeiwu.commontool.volley.request.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//Administrator
public class ByteArrayOutputStreamProgess extends ByteArrayOutputStream{
	public ByteArrayOutputStreamProgess() {
	}
	@Override
	public void write(byte[] buffer) throws IOException {
		super.write(buffer);
	}
	@Override
	public synchronized void write(byte[] buffer, int offset, int len) {
		super.write(buffer, offset, len);
	}
}
