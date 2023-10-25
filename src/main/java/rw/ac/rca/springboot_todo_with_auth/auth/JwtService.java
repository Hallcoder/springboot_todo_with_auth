package rw.ac.rca.springboot_todo_with_auth.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SECRET_KEY = "42fb171d71cb1e9b7f90ba8b95329e47ebc390f6fa6a14cf31804aa16efb0438";
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
    public String generateToken(UserDetails userDetails){
        return  generateToken(userDetails,new HashMap<>());
    }
    public String generateToken(UserDetails userDetails, Map<String,Object> extraClaims){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setExpiration(new Date(System.currentTimeMillis()+1000 * 60*24))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(userDetails.getUsername())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims c  = extractAllClaims(token);
        return claimsResolver.apply(c);
    }
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
       String username = userDetails.getUsername();
       return (username == extractUsername(jwtToken)) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        Date expiration = getExpirationFrom(jwtToken);
        return expiration.before(new Date());
    }

    private Date getExpirationFrom(String jwtToken) {
        return extractClaim(jwtToken,Claims::getExpiration);
    }
}
