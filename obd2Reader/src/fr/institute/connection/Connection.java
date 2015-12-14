package fr.institute.connection;

import java.io.InputStream;
import java.io.OutputStream;

public interface Connection {
	public boolean connect();
	public void disconnect();
	public void send(String message);
	public String read();
	public OutputStream getOutputStream();
	public InputStream getInputStream();
}
