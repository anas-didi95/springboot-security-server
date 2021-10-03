package com.anasdidi.security.domain.auth;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

final class AuthUtils {

  private static final String SECRET = "secret";
  private static final long JWT_VALIDITY = 5 * 60 * 60;

  static String getSubjectFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  static Date getExpirationFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  static Boolean isTokenExpired(String token) {
    Date expiration = getExpirationFromToken(token);
    return expiration.before(new Date());
  }

  static String generateToken(UserDetails userDetails) {
    String sub = userDetails.getUsername();
    Date iat = new Date(System.currentTimeMillis());
    Date exp = new Date(System.currentTimeMillis() + (JWT_VALIDITY * 1000));
    return Jwts.builder().setSubject(sub).setIssuedAt(iat).setExpiration(exp).signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
  }

  static Boolean validateToken(String token, UserDetails userDetails) {
    String subject = getSubjectFromToken(token);
    return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  static private <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
    final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    return resolver.apply(claims);
  }
}
