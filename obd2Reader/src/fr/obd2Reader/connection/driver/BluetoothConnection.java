package fr.obd2Reader.connection.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Bluetooth connection object.
 * @author Supa Kanojo Hunta
 *
 */
public class BluetoothConnection {
	private static Object lock;
	private ArrayList<RemoteDevice> devices;
	private ArrayList<String> devicesNames;
	private ArrayList<String> urls;
	private LocalDevice localDevice;
	private DiscoveryAgent discoveryAgent;
	private StreamConnection streamConnection;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	/**
     * Default constructor for BluetoothConnection.
	 *
     */
	public BluetoothConnection(){
		lock = new Object();
		devices = new ArrayList<RemoteDevice>();
		devicesNames = new ArrayList<String>();
		urls = new ArrayList<String>();

		try {
			localDevice = LocalDevice.getLocalDevice();
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		discoveryAgent = localDevice.getDiscoveryAgent();
			
	}
	
	/**
     * Called by garbage collector. Close the streams and connection if a connection was made.
	 *
     */
	@Override
	public void finalize(){
		if(streamConnection!=null && inputStream!=null && outputStream!=null){
			try {
				inputStream.close();
				outputStream.close();
				streamConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Getter of attribut devices.
	 * 
	 * @return : list of remote devices.
	 */
	public ArrayList<RemoteDevice> getDevices(){
		return this.devices;
	}
	
	public OutputStream getOutputStream(){
		return outputStream;
	}
	
	public InputStream getInputStream(){
		return inputStream;
	}
	
	/**
	 * Getter of attribut services.
	 * 
	 * @return : list of connection urls affiliated to discovered services.
	 */
	public ArrayList<String> getUrls(){
		return this.urls;
	}
	
	 /**
     * Search for any surrounding bluetooth device and stock it.
	 *
	 *@return boolean : whether the search could wrap up or not.
     */
	public boolean searchDevices(){
		try{
			boolean inquiryStarted = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new Discovery());
			
			try{
				synchronized(lock){
					lock.wait();
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			return inquiryStarted;
			
		}catch(BluetoothStateException e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
     * Search for the services of a given bluetooth device and stock every connection URL encountered.
	 *
	 *@param deviceStr : friendly name of the device to search services from.
	 *@param protocol : UUID String value of the short-code protocol needed for the services research.
	 *@return : number of services found. -1 if error.
     */
	public int searchServices(String deviceStr, String protocol){
		if(discoveryAgent!=null){
			try{
				if(!devices.isEmpty() && devicesNames.contains(deviceStr)){
					UUID[] uuidSet = new UUID[1];
					uuidSet[0] = new UUID(protocol, true);
					RemoteDevice device = devices.get(devicesNames.indexOf(deviceStr));
					int servicesNb = discoveryAgent.searchServices(null, uuidSet, device, new Discovery());
					
					try{
						synchronized(lock){
							lock.wait();
						}
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					return servicesNb;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
     * Search for the services of a given bluetooth device and stock every connection URL encountered.
	 *
	 *@param deviceStr : friendly name of the device to search services from.
	 *@param protocol : UUID short-code protocol needed for the services research.
	 *@return : number of services found. -1 if error.
     */
	public int searchServices(String deviceStr, int protocol){
		if(discoveryAgent!=null){
			try{
				if(!devices.isEmpty() && devicesNames.contains(deviceStr)){
					UUID[] uuidSet = new UUID[1];
					uuidSet[0] = new UUID(protocol);
					RemoteDevice device = devices.get(devicesNames.indexOf(deviceStr));
					int servicesNb = discoveryAgent.searchServices(null, uuidSet, device, new Discovery());
					
					try{
						synchronized(lock){
							lock.wait();
						}
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					return servicesNb;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
     * Search for the services of a given bluetooth device and stock every connection URL encountered.
	 *
	 *@param device : device to search services from.
	 *@param protocol : UUID short-code protocol needed for the services research.
	 *@return : number of services found. -1 if error.
     */
	public int searchServices(RemoteDevice device, int protocol){
		if(discoveryAgent!=null){
			try{
				UUID[] uuidSet = new UUID[1];
				uuidSet[0] = new UUID(protocol);
				int servicesNb = discoveryAgent.searchServices(null, uuidSet, device, new Discovery());
				
				try{
					synchronized(lock){
						lock.wait();
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				return servicesNb;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
     * Open a client connection to a service plus input and output streams from the connection.
	 *
	 *@param url : connection URL needed to connect to the service.
     */
	public void clientConnection(String url){
		try {
			streamConnection = (StreamConnection) Connector.open(url);
			outputStream = streamConnection.openOutputStream();
			inputStream = streamConnection.openInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * Send a message to the other end of the connection through an opened output stream.
	 *
	 *@param message : message to send through the output stream.
	 *@return : whether dispatch of the message was successful or not.
     */
	public boolean send(String message){
		if(outputStream!=null){
			try {
				outputStream.write((message + "\r").getBytes());
				outputStream.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
     * Read every character into the input stream's buffer until the first occurrence of the delimiter.
	 *
	 *@param delimiter : character to reach before stopping to scan input stream buffer.
	 *@return : phrase read since last scan of the input stream buffer.
     */
	public String readUntil(String delimiter){
		String inBufStr = "";
		if(inputStream!=null){
			try{
				while(!inBufStr.endsWith(delimiter)){
					inBufStr += readChar();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		return inBufStr;
	}
	
	/**
     * Read one character from an input stream and cast the bytes received from int to ASCII.
	 *
	 *@return : last ASCII character contained in the buffer since last read.
     */
	private String readChar(){
		String inBuf = "";
			try {
				inBuf = Character.toString((char)inputStream.read());
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return inBuf;
	}
	
	/**
     * Inner-class implementing DiscoveryListener interface used by DiscoveryAgent's inquiry.
	 *
     */
	private class Discovery implements DiscoveryListener{
		
		/**
		 * Called when a device is found during an inquiry.
	     * Add the new device to BluetoothConnection's ArrayList of devices.
		 *
		 *@param btDevice : discovered device.
		 *@param deviceClass : classes associated to the discovered device.
	     */
		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod){
			devices.add(btDevice);
			try {
				devicesNames.add(btDevice.getFriendlyName(false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Called when an inquiry is completed.
	     * Notify the searchDevices() method when the inquiry is over by freeing lock.
	     * searchDevices() can then continue to unfold.
		 *
		 *@param inquiryState : state of the inquiry at its end.
	     */
		public void inquiryCompleted(int inquiryState){
			synchronized(lock){
				lock.notify();
			}
		}
		
		/**
		 * Called when services are found during a service search.
	     * Add new service(s) to BluetoothConnection's ArrayList of services.
		 *
		 *@param transID : transaction ID of the service search posting the result.
		 *@param services : every services found during the search.
	     */
		public void servicesDiscovered(int transID, ServiceRecord[] services){
			for(int i=0; i<services.length; i++){
				String url = services[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
				urls.add(url);
			}
			
		}
		
		/**
		 * Called when a serviceSearch ends.
	     * Notify the searchServices() method when the inquiry is over by freeing lock.
	     * searchServices() can then continue to unfold.
		 *
		 *@param transIDS : transaction ID of the service search posting the result.
		 *@param respCode : state of the search at its end.
	     */
		public void serviceSearchCompleted(int transID, int respCode){
			synchronized(lock){
				lock.notify();
			}
		}
	}
}
