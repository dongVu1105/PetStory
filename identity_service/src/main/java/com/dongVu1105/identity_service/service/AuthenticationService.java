package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.dto.request.AuthenticationRequest;
import com.dongVu1105.identity_service.dto.request.IntrospectRequest;
import com.dongVu1105.identity_service.dto.request.LogoutRequest;
import com.dongVu1105.identity_service.dto.request.RefreshTokenRequest;
import com.dongVu1105.identity_service.dto.response.AuthenticationResponse;
import com.dongVu1105.identity_service.dto.response.IntrospectResponse;
import com.dongVu1105.identity_service.entity.InvalidatedToken;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.repository.InvalidatedTokenRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration}")
    private long ACCESS_TOKEN_DURATION;

    @NonFinal
    @Value("${jwt.refresh-token-duration}")
    private long REFRESH_TOKEN_DURATION;

    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate (AuthenticationRequest request) throws AppException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .valid(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public IntrospectResponse introspect (IntrospectRequest request){
        boolean isValid = true;
        try {
            verifyToken(request.getAccessToken(), false);
        } catch (Exception e){
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout (LogoutRequest request) throws Exception {
        try {
            SignedJWT accessToken = verifyToken(request.getAccessToken(), false);
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(accessToken.getJWTClaimsSet().getJWTID())
                    .expiryTime(accessToken.getJWTClaimsSet().getExpirationTime())
                    .build());
        } catch (Exception e) {
            log.info("access token already expired");
        }

        try {
            SignedJWT refreshToken = verifyToken(request.getRefreshToken(), true);
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(refreshToken.getJWTClaimsSet().getJWTID())
                    .expiryTime(refreshToken.getJWTClaimsSet().getExpirationTime())
                    .build());
        } catch (Exception e) {
            log.info("refresh token already expired");
        }
    }

    public AuthenticationResponse refreshToken (RefreshTokenRequest request)
            throws AppException, ParseException, JOSEException {
        SignedJWT refreshToken = verifyToken(request.getRefreshToken(), true);
        System.out.println("Da check xong verify");
        invalidatedTokenRepository.save(InvalidatedToken.builder()
                .id(refreshToken.getJWTClaimsSet().getJWTID())
                .expiryTime(refreshToken.getJWTClaimsSet().getExpirationTime())
                .build());
        User user = userRepository.findById(refreshToken.getJWTClaimsSet().getSubject()).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .valid(true)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken).build();
    }


    private String generateAccessToken (User user) throws AppException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("dongVu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(ACCESS_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("token_type", "access")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new AppException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private String generateRefreshToken (User user) throws AppException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("dongVu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(REFRESH_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("token_type", "refresh")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new AppException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private SignedJWT verifyToken (String token, boolean isRefresh) throws ParseException, JOSEException, AppException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("token_type");
        if((isRefresh && !"refresh".equals(tokenType)) || (!isRefresh && !"access".equals(tokenType))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String buildScope (User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }
}
