package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.dto.request.IntrospectRequest;
import com.dongVu1105.identity_service.dto.response.IntrospectResponse;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    InvalidatedTokenRepository invalidatedTokenRepository;

    public IntrospectResponse introspect (IntrospectRequest request){
        return null;
    }

    public SignedJWT verifyToken (String token, boolean isRefresh) throws ParseException, JOSEException, AppException {
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
}
