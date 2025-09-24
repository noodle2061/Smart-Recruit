package com.ptit.thesis.smartrecruit.service;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

public interface FirebaseService {
    public FirebaseToken verifyIdToken(String idToken);
    public UserRecord createFirebaseUser(String email, String password);
    public UserRecord createFirebaseUserWithOAuth(String email, String displayName);
}
