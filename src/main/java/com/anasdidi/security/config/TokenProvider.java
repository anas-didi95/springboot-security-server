package com.anasdidi.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@PropertySource(value = "classpath:config.properties")
public class TokenProvider {

  private final String PERMISSIONS_KEY = "pms";
  private final String SECRET;
  private final String ISSUER;
  private final int ACCESS_TOKEN_VALIDITY_SECONDS;

  public TokenProvider(@Value("${config.jwt.secret}") String SECRET, @Value("${config.jwt.issuer}") String ISSUER,
      @Value("${config.jwt.accessTokenValidityMinutes}") int ACCESS_TOKEN_VALIDITY_SECONDS) {
    this.SECRET = SECRET;
    this.ISSUER = ISSUER;
    this.ACCESS_TOKEN_VALIDITY_SECONDS = ACCESS_TOKEN_VALIDITY_SECONDS;
  }

  public String getUserId(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  @SuppressWarnings("unchecked")
  public List<SimpleGrantedAuthority> getPermissionList(String token) {
    List<String> roleList = (List<String>) getClaimFromToken(token, (claims) -> claims.get(PERMISSIONS_KEY));
    return roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
  }

  public Boolean validateToken(String token) {
    final String userId = getUserId(token);
    return (userId != null && !isTokenExpired(token));
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getClaimFromToken(token, Claims::getExpiration);
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
    return Jwts.parser().requireIssuer(ISSUER).setSigningKey(SECRET).parseClaimsJws(token).getBody();
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
