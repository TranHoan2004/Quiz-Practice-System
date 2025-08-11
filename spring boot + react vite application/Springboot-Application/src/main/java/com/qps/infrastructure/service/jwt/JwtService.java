package com.qps.infrastructure.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {
    @Getter
    @Value("${jwt.expirationMs}")
    Long expirationMs;

    @Value("${jwt.signerKey}")
    String signer;

    public String createAccessToken(String id) throws JOSEException {
        return generateToken(id, "access");
    }

    public String createRefreshToken(String id) throws JOSEException {
        return generateToken(id, "refresh");
    }

    public JWTClaimsSet validateToken(String token) throws ParseException, JOSEException {
//        log.info("Validating token: {}", token);
        var signedJWT = SignedJWT.parse(token);
        var verifier = new MACVerifier(signer);
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT token");
        }

        var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (new Date().after(expirationTime)) {
            throw new JOSEException("JWT token expired");
        }

        return signedJWT.getJWTClaimsSet();
    }

    private String generateToken(String id, String type) throws JOSEException {
//        log.info("Generating JWT token for {}", id);
        var claimsSet = new JWTClaimsSet.Builder()
                .subject(id)
                .issuer("qps-be")
                .expirationTime(new Date(System.currentTimeMillis() + expirationMs))
                .claim("type", type)
                .build();

        var header = new JWSHeader(JWSAlgorithm.HS256);
        var signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(new MACSigner(signer.getBytes()));
        return signedJWT.serialize();
    }
}
