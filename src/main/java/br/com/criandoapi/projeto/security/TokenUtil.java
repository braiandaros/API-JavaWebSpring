package br.com.criandoapi.projeto.security;

import java.security.Key;
import java.util.Date;

import br.com.criandoapi.projeto.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


public class TokenUtil {
	private static final String HEADER = "Authorization";
	public static final String PREFIX ="Bearer";
	private static final long EXPIRATION = 12*60*60*1000;
	private static final String SECRET_KEY = "MyK3Yt0T0k3nP4r@S3CuriTY@Sp3c14L";
	private static final String EMISSOR = "DevNice";
	
	private static String createToken(Usuario usuario) {
		Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		
		String token = Jwts.builder()
						   .setSubject(usuario.getNome())
						   .setIssuer(EMISSOR)
						   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
						   .signWith(secretKey, SignatureAlgorithm.HS256)
						   .compact();
		
		return PREFIX + token;
	}
	
	private static boolean isExpirationValid(Date expiration) {
		return expiration.after(new Date(System.currentTimeMillis()));
	}
}
