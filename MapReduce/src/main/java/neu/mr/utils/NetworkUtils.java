package neu.mr.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Network utilities.
 * @author raiden
 *
 */
public class NetworkUtils {

	/**
	 * method to get list of ips.
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getIp () throws SocketException {
		
		Enumeration<NetworkInterface> nics_itr = NetworkInterface.getNetworkInterfaces();
		Enumeration<InetAddress> addr_itr;
		List<String>  address = new ArrayList<String>();
		while(nics_itr.hasMoreElements())
		{
			addr_itr = ((NetworkInterface)nics_itr.nextElement()).getInetAddresses();
			while (addr_itr.hasMoreElements())
			{
				address.add(((InetAddress) addr_itr.nextElement()).getHostAddress());
			}
		}
		return address;
	}
	
	/**
	 * get ip address.
	 * @param inet
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getIp (int inet) throws UnknownHostException, SocketException {
		
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		return null;
	}

	/**
	 * get subnet.
	 * @param inet
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getSubnet (int inet) throws UnknownHostException, SocketException {
		
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		networkInterface.getInterfaceAddresses().get(inet).getNetworkPrefixLength();
		return null;
	}
	
	/**
	 * method to get broadcast address.
	 * @return
	 */
	public static InetAddress getBroadcastIpAddress ()
	{
		InetAddress address = null;
		try {
			Enumeration<NetworkInterface> interfaces =
				    NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
				  NetworkInterface networkInterface = interfaces.nextElement();
				  if (networkInterface.isLoopback())
				    continue;    // Don't want to broadcast to the loopback interface
				  for (InterfaceAddress interfaceAddress :
				           networkInterface.getInterfaceAddresses()) {
				    address = interfaceAddress.getBroadcast();
				    if (address == null)
				      continue;
				    // Use the address
				  }
				}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	/**
	 * method to get ip address
	 * @return
	 */
	public static InetAddress getIpAddress ()
	{
		InetAddress address = null;
		
		try {
			Enumeration<NetworkInterface> interfaces =
				    NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
				  NetworkInterface networkInterface = interfaces.nextElement();
				  if (networkInterface.isLoopback())
				    continue;    // Don't want to broadcast to the loopback interface
				  for (InterfaceAddress interfaceAddress :
				           networkInterface.getInterfaceAddresses()) {
				    address = interfaceAddress.getAddress();
				    if (address == null)
				      continue;
				  }
				}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return address;
	}
}
