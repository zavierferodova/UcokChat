package com.robbies.ucokchat.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.robbies.ucokchat.data.FirebaseRepository
import com.robbies.ucokchat.data.GroupChatRepository
import com.robbies.ucokchat.ui.screen.home.HomeViewModel
import com.robbies.ucokchat.ui.screen.join.JoinGroupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseDatabaseModule = module {
    single {
        Firebase.firestore
    }
}

val repositoryModule = module {
    single {
        FirebaseRepository(get(), androidContext())
    }
    single {
        GroupChatRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel() {
        HomeViewModel(get())
    }
    viewModel {
        JoinGroupViewModel(get())
    }
}