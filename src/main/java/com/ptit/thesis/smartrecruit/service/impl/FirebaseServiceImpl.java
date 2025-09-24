package com.ptit.thesis.smartrecruit.service.impl;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.exception.InvalidTokenException;
import com.ptit.thesis.smartrecruit.service.FirebaseService;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public FirebaseToken verifyIdToken(String idToken) {
        if (idToken == null || idToken.isEmpty()) {
            throw new InvalidTokenException("Firebase ID Token must not be empty!");
        }

        try {
            String clearIdToken = (idToken.startsWith("Bearer")) ? idToken.substring(7) : idToken;
            return FirebaseAuth.getInstance().verifyIdToken(clearIdToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid Firebase ID Token: " + e.getMessage());
        }
    }

    @Override
    public UserRecord createFirebaseUser(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setEmailVerified(false)
                    .setPassword(password)
                    .setDisabled(false);
            return FirebaseAuth.getInstance().createUser(request);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Error creating Firebase user: " + e.getMessage());
        }
    }

    @Override
    public UserRecord createFirebaseUserWithOAuth(String email, String displayName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFirebaseUserWithOAuth'");
    }
    
}
