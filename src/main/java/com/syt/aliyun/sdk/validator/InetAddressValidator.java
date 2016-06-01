package com.syt.aliyun.sdk.validator;

import java.io.Serializable;

import com.syt.aliyun.sdk.common.Consts;

public class InetAddressValidator implements Serializable {
	private static final long serialVersionUID = -919201640201914789L;	
	private static final InetAddressValidator VALIDATOR = new InetAddressValidator();
	private final RegexValidator ipv4Validator = new RegexValidator(Consts.IPV4_REGEX);

	public static InetAddressValidator getInstance() {
		return VALIDATOR;
	}

	public boolean isValid(String inetAddress) {
		return isValidInet4Address(inetAddress);
	}

	public boolean isValidInet4Address(String inet4Address) {
		String[] groups = ipv4Validator.match(inet4Address);

		if (groups == null) {
			return false;
		}

		for (int i = 0; i <= 3; i++) {
			String ipSegment = groups[i];
			if ((ipSegment == null) || (ipSegment.length() <= 0)) {
				return false;
			}

			int iIpSegment = 0;
			try {
				iIpSegment = Integer.parseInt(ipSegment);
			} catch (NumberFormatException e) {
				return false;
			}

			if (iIpSegment > 255) {
				return false;
			}
		}

		return true;
	}

}
