//package com.becoder.serviceImpl;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import com.becoder.entity.User;
//import com.becoder.exception.JwtTokenExpiredException;
//import com.becoder.service.JwtService;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
//@Service
//public class JwtServiceImpl implements JwtService {
//	
//	private String secretKey="";
//	
//	public JwtServiceImpl() {
//		try {
//			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//			SecretKey sk = keyGen.generateKey();
//			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public String generateToken(User user) {
//		
//		Map<String, Object> claims=new HashMap<>();
//		claims.put("roles", user.getRoles());
//		claims.put("status", user.getStatus().getIsActive());
//		
//		String token = Jwts.builder()
//		.claims().add(null)
//		.subject(user.getEmail())
//		.issuedAt(new Date(System.currentTimeMillis()))
//		.expiration(new Date(System.currentTimeMillis()+60*60*60*10))
//		.and()
//		.signWith(getKey())
//		.compact();
//		
//		return token;
//	}
//
//	private Key getKey() {
//		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}
//
//	@Override
//	public String extractUsername(String token) {
//		Claims claims = extractAllClaims(token);
//		return claims.getSubject();
//	}
//
//	public String role(String token) {
//		Claims claims = extractAllClaims(token);
//		String role= (String)claims.get("role");
//		return role;
//	}
//	
//	private Claims extractAllClaims(String token) {		
//		try {
//			return Jwts.parser().verifyWith(decryptKey(secretKey)).build().parseSignedClaims(token).getPayload();
//		}
//		catch (ExpiredJwtException e) {
//			throw new JwtTokenExpiredException("Token is Expired");
//		}
//		catch (JwtException e) {
//			throw new JwtTokenExpiredException("Invalid JWT Token");
//		}
//		catch (Exception e) {
//			throw e;
//		}
//	}
//
//	private SecretKey decryptKey(String secretKey2) {
//		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}
//
//	@Override
//	public Boolean validateToken(String token, UserDetails userDetails) {
//		String username = extractUsername(token);
//		Boolean isExpired=isTokenExpired(token);
//		if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
//			return true;
//		}
//		return false;
//	}
//
//	private Boolean isTokenExpired(String token) {
//		Claims claims = extractAllClaims(token);
//		Date expiration = claims.getExpiration();
//		return expiration.before(new Date());
//	}
//	
//}


package com.becoder.serviceImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.becoder.entity.Role;
import com.becoder.entity.User;
import com.becoder.exception.JwtTokenExpiredException;
import com.becoder.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        claims.put("roles", roles);

        claims.put(
                "status",
                user.getStatus() != null
                        && Boolean.TRUE.equals(user.getStatus().getIsActive())
        );

        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofHours(10))))
                .signWith(getKey())
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String role(String token) {
        Claims claims = extractAllClaims(token);

        Object rolesClaim = claims.get("roles");

        if (rolesClaim instanceof List<?> roles && !roles.isEmpty()) {
            return String.valueOf(roles.get(0));
        }

        return null;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is expired");

        } catch (JwtException e) {
            throw new JwtTokenExpiredException("Invalid JWT token");
        }
    }

    @Override
    public Boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        Claims claims = extractAllClaims(token);

        Date expiration = claims.getExpiration();

        return username.equalsIgnoreCase(userDetails.getUsername())
                && expiration.after(new Date());
    }
}

