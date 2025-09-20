package com.ptit.thesis.smartrecruit.config;


import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-path}")
    private Resource serviceAccount;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        InputStream serviceAccountStream = serviceAccount.getInputStream();
        
        FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();
        return FirebaseApp.initializeApp(options);
    }
}
