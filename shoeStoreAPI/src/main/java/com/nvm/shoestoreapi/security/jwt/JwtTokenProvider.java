package com.nvm.shoestoreapi.security.jwt;

import com.nvm.shoestoreapi.security.MyUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "nguyenvanmanh291201"; // Key bí mật chỉ có phía server biết
    // 14 ngày = 14 * 24 * 60 * 60 * 1000
    private final long JWT_EXPIRATION = 14 * 24 * 60 * 60 * 1000; // Thời gian có hiệu lực của chuỗi jwt (60 phút), tính bằng mili giây

    // Tạo ra jwt từ thông tin user
    public String generateToken(MyUserDetails myUserDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        Set<String> roles = myUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(myUserDetails.getUsername()) // Tạo chuỗi json web token từ username của user.
                .claim("roles", roles) // set quyền của user vào jwt
                .setIssuedAt(now) // Thời gian tạo ra token
                .setExpiration(expiryDate) // Thời gian hết hạn token
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Mã hóa bằng thuật toán HS512 và key bí mật
                .compact(); // Tạo chuỗi token
    }

    // Kiểm tra token có hợp lệ không
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    // Lấy thông tin username từ jwt
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser() // Phân tích chuỗi jwt
                .setSigningKey(JWT_SECRET) // Mã bí mật
                .parseClaimsJws(token) // Chuỗi jwt
                .getBody(); // Lấy phần body

        return claims.getSubject(); // Trả về username
    }
}