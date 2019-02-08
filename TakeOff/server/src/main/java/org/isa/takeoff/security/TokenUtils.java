package org.isa.takeoff.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils 
{
	@Value("takeoff")
	private String APP_NAME;

	@Value("somesecret")
	public String SECRET;

	@Value("86400")
	private int EXPIRES_IN;

	@Value("172800")
	private int ACTIVATION_LINK_EXPIRES_IN;
	
	@Value("Authorization")
	private String AUTH_HEADER;

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	public String generateToken(String username) 
	{
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate(EXPIRES_IN))
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}

	private Date generateExpirationDate(int expiresIn)
	{
		return new Date(new Date().getTime() + expiresIn * 1000);
	}

	public String refreshToken(String token) 
	{
		String refreshedToken;
		try 
		{
			Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder()
					.setClaims(claims)
					.setExpiration(this.generateExpirationDate(EXPIRES_IN))
					.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
		}
		catch (Exception e) 
		{
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public Boolean canTokenBeRefreshed(String token)
	{
		Date expiration = this.getExpirationDateFromToken(token);
		return !expiration.before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) 
	{
		String username = getUsernameFromToken(token);
		return (username != null && username.equals(userDetails.getUsername()));
	}

	private Claims getAllClaimsFromToken(String token) 
	{
		Claims claims;
		try
		{
			claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
		}
		catch (Exception e) 
		{
			claims = null;
		}
		return claims;
	}

	public String getUsernameFromToken(String token) 
	{
		String username;
		try 
		{
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		}
		catch (Exception e) 
		{
			username = null;
		}
		return username;
	}

	public Date getIssuedAtDateFromToken(String token)
	{
		Date issueAt;
		try 
		{
			Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		}
		catch (Exception e)
		{
			issueAt = null;
		}
		return issueAt;
	}

	public Date getExpirationDateFromToken(String token) 
	{
		Date expiration;
		try 
		{
			Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		}
		catch (Exception e)
		{
			expiration = null;
		}
		return expiration;
	}

	public String getToken(HttpServletRequest request)
	{
		String authHeader = request.getHeader(AUTH_HEADER);

		if (authHeader != null && authHeader.startsWith("Bearer "))
		{
			return authHeader.substring(7);
		}

		return null;
	}
	
	public String generateActivationToken(String username) 
	{
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(this.generateExpirationDate(ACTIVATION_LINK_EXPIRES_IN))
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}
}