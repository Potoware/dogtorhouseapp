package com.dogtorhouse.app.util;

import java.util.Base64;

public class Base64Utils {
	public static String byteArrayToBase64(byte[] bytes) {
	    return Base64.getEncoder().encodeToString(bytes);
	}

}
