package com.becoder.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretGenerator {
	
	public static void main(String[] args) {
        byte[] secret = new byte[32];

        new SecureRandom().nextBytes(secret);

        String base64Secret = Base64.getEncoder()
                .encodeToString(secret);

        System.out.println("JWT Secret: "); // VXzOQbBpi6fgwIbzXfiEDm1qJzmw4s+nhdW6g7eSv78=
        System.out.println(base64Secret);
    }
	
}
