package com.anasdidi.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

  private final String SECRET = "secret";
  private final String ISSUER = "https://anasdidi.dev";
  private final String PERMISSIONS_KEY = "pms";
  private final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;

  public String getSubject(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDate(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  @SuppressWarnings("unchecked")
  public List<SimpleGrantedAuthority> getPermissionList(String token) {
    List<String> roleList = (List<String>) getClaimFromToken(token, (claims) -> claims.get(PERMISSIONS_KEY));
    return roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getSubject(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDate(token);
    return expiration != null && expiration.before(new Date());
  }

  public String generateToken(String userId, List<String> permissionList) {
    return doGenerateToken(userId, permissionList);
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
  }

  private String doGenerateToken(String subject, List<String> permissionList) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(PERMISSIONS_KEY, permissionList);

    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuer(ISSUER)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
        .signWith(SignatureAlgorithm.HS256, SECRET).compact();
  }
}
