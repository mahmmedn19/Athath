package com.project.athath.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.network.ApiService;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.repository.app_repo.AthathRepositoryImpl;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.repository.auth.AuthRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(FirebaseAuth auth, FirebaseFirestore db) {
        return new AuthRepositoryImpl(auth, db);
    }

    @Provides
    @Singleton
    public AthathRepository provideAthathRepository(FirebaseAuth auth, FirebaseFirestore db, ApiService apiService) {
        return new AthathRepositoryImpl(auth, db, apiService);
    }
}
