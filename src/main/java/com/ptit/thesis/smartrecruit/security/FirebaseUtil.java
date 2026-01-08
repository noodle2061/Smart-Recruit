package com.ptit.thesis.smartrecruit.security;

import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.exception.InvalidTokenException;

@Component
public class FirebaseUtil {
    public FirebaseToken verifyToken(String idToken) {

        if (idToken == null) {
            throw new IllegalArgumentException("Firebase ID Token must not be empty!");
        }

        try {
            String clearIdToken = (idToken.startsWith("Bearer")) ? idToken.substring(7) : idToken;
            return FirebaseAuth.getInstance().verifyIdToken(clearIdToken);
        } catch (FirebaseAuthException e) {
            throw new InvalidTokenException("Invalid Firebase ID Token: " + e.getMessage());
        }
    }

    public UserRecord getUserByEmail(String email) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            return userRecord;
        } catch (FirebaseAuthException e) {
            return null;
        }

    }
}
