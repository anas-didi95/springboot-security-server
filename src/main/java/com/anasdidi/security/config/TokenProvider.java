package com.anasdidi.security.config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

  private final String SECRET = "secret";
  private final String ROLE_KEY = "scopes";
  private final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;

  public String getUsername(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDate(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public List<SimpleGrantedAuthority> getRoleList(String token) {
    @SuppressWarnings("unchecked")
    List<String> roleList = (List<String>) getClaimFromToken(token, (claims) -> claims.get(ROLE_KEY));
    return roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDate(token);
    return expiration.before(new Date());
  }

  public String generateToken(User user) {
    return doGenerateToken(user.getUsername());
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
  }

  private String doGenerateToken(String subject) {
    Claims claims = Jwts.claims().setSubject(subject);
    claims.put(ROLE_KEY, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

    return Jwts.builder().setClaims(claims).setIssuer("http://devglan.com")
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
        .signWith(SignatureAlgorithm.HS256, SECRET).compact();
  }
}
