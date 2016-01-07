package institute;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fr.institute.engine.PidRequest;
import fr.institute.engine.RequestManager;

public class RequestManagerTest {

	private RequestManager manager;
	private PidRequest request;
	private PidRequest request2;
	
	public void setUp(){
		ArrayList<Integer> vehicleRef = new ArrayList<Integer>();
		vehicleRef.add(0x01);
		vehicleRef.add(0x12);
		//vehicleRef.add(0xB8);
		//vehicleRef.add(0x10);
		
		manager = new RequestManager();	
		manager.setVehicleRef(vehicleRef);
		ArrayList<PidRequest> requestTable = new ArrayList<PidRequest>();
		request = new PidRequest(0x010C);
		requestTable.add(request);
		request2 = new PidRequest(0x0121);
		requestTable.add(request2);
		
		manager.setRequestTable(requestTable);
	}
	
	@Test
	public void testRequestAlreadyCreated() {
		setUp();
		int existingRequestId = 0x010C,
			nonExistingRequestId = 0x010D;
		
		System.out.println(manager.getCompatibleRequests());
		
		assertTrue(manager.requestAlreadyCreated(existingRequestId));
		assertFalse(manager.requestAlreadyCreated(nonExistingRequestId));
	}
	
	@Test
	public void testIsCompatible(){
		setUp();
		int compatibleRequest = 0x010C,
			incompatibleRequest = 0x010B; 
		
		assertTrue(manager.isCompatible(compatibleRequest));
		assertFalse(manager.isCompatible(incompatibleRequest)); 
	} 
	
	@Test
	public void testGetIndexOf(){
		setUp();
		String existingRequestName = "Engine RPM";
		int existingRequestPID = 0x0121;
		String nonExistingRequestName = "Fuel Pressure";
		
		assertEquals(0,manager.indexOf(existingRequestName));
		assertEquals(1, manager.indexOf(existingRequestPID));
		assertEquals(-1, manager.indexOf(nonExistingRequestName));
	}
	
	@Test
	public void testAccessToRequestTable(){
		setUp();
		int requestIndex = manager.indexOf(Integer.parseInt("010C",16));
		PidRequest fromTable = manager.getRequestTable().get(requestIndex);
		
		assertEquals(0, requestIndex);
		assertEquals(request, fromTable);
	}

}
