import DisableSecurity;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.vmware.vim25.InvalidLocaleFaultMsg;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VimService;

public class HelloVworld {

	public static void main(String[] args) {
		String url = "https://yourhostname/sdk/vimService";
		String user = "username";
		String password = "password";

		// Create a VimService object to obtain a VimPort binding provider.
		// The BindingProvider provides access to the protocol fields
		// in request/response messages. Retrieve the request context
		// which will be used for processing message requests.
		
		VimService vimService = new VimService();
		VimPortType vimPort = vimService.getVimPort();

		// Store the Server URL in the request context and specify true
		// to maintain the connection between the client and server.
		// The client API will include the Server's HTTP cookie in its
		// requests to maintain the session. If you do not set this to true,
		// the Server will start a new session with each request.
		
		Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();
		ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

		ManagedObjectReference serviceInstance = new ManagedObjectReference();
		serviceInstance.setType("ServiceInstance");
		serviceInstance.setValue("ServiceInstance");

		try {
			// Disable all SSL trust security
			DisableSecurity.trustEveryone();
			
			ServiceContent serviceContent = 
					vimPort.retrieveServiceContent(serviceInstance);
			vimPort.login(serviceContent.getSessionManager(), user, password, null);
			
			// print out the product name, server type, and product version
			System.out.println(serviceContent.getAbout().getFullName());
			System.out.println("Server type is " + 
					serviceContent.getAbout().getApiType());
			System.out.println("API version is " + 
					serviceContent.getAbout().getVersion());
			
			vimPort.logout(serviceContent.getSessionManager());

		} catch (RuntimeFaultFaultMsg e) {
			e.printStackTrace();
		} catch (InvalidLocaleFaultMsg e) {
			e.printStackTrace();
		} catch (InvalidLoginFaultMsg e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}


	}

}
