package com.syt.aliyun.sdk.kit;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

import com.syt.aliyun.sdk.validator.InetAddressValidator;

public class ToolsKit {

	private static SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);


	/***
	 * 判断传入的对象是否为空
	 * 
	 * @param obj
	 *            待检查的对象
	 * @return 返回的布尔值,为空或等于0时返回true
	 */
	public static boolean isEmpty(Object obj) {
		return checkObjectIsEmpty(obj, true);
	}

	/***
	 * 判断传入的对象是否不为空
	 * 
	 * @param obj
	 *            待检查的对象
	 * @return 返回的布尔值,不为空或不等于0时返回true
	 */
	public static boolean isNotEmpty(Object obj) {
		return checkObjectIsEmpty(obj, false);
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
		if (null == obj)
			return bool;
		else if (obj == "")
			return bool;
		else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
			// if((Integer.parseInt(obj.toString()))==0) return bool;
			try {
				Double.parseDouble(obj + "");
			} catch (Exception e) {
				return bool;
			}
		} else if (obj instanceof String) {
			if (((String) obj).length() <= 0)
				return bool;
			if ("null".equals(obj))
				return bool;
		} else if (obj instanceof Map) {
			if (((Map) obj).size() == 0)
				return bool;
		} else if (obj instanceof Collection) {
			if (((Collection) obj).size() == 0)
				return bool;
		} else if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0)
				return bool;
		}
		return !bool;
	}

	public static boolean isIpAddress(String str) {
		Pattern pattern = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}");
		return pattern.matcher(str).matches();
	}

	public static String getLocalMachineIp() {
		InetAddressValidator validator = new InetAddressValidator();
		String candidate = new String();
		try {
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				if (iface.isUp()) {
					Enumeration<InetAddress> addresses = iface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress address = (InetAddress) addresses.nextElement();
						if ((!address.isLinkLocalAddress()) && (address.getHostAddress() != null)) {
							String ipAddress = address.getHostAddress();
							if (!ipAddress.equals("127.0.0.1")) {
								if (validator.isValidInet4Address(ipAddress)) {
									return ipAddress;
								}
								if (validator.isValid(ipAddress)) {
									candidate = ipAddress;
								}
							}
						}
					}
				}
			}
		} catch (SocketException localSocketException) {
		}
		return candidate;
	}

	public static String parseRfc822Date(Date date) {
		return getRfc822DateFormat().format(date);
	}

	private static DateFormat getRfc822DateFormat() {
		rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return rfc822DateFormat;
	}
	
	public static String getMapValue(Map<String, String> map, String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return "";
		}
	}
}
