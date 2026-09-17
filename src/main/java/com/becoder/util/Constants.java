package com.becoder.util;

public class Constants {

	public static final String EMAIL_REGEX="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	public static final String MOBNO_REGEX="^[6-9]\\d{9}$";
	
	public static final String ROLE_ADMIN="hasRole('ROLE_ADMIN')";
	public static final String ROLE_USER="hasRole('ROLE_USER')";
	public static final String ROLE_ADMIN_USER="hasAnyRole('ROLE_USER','ROLE_ADMIN')";
	
}
