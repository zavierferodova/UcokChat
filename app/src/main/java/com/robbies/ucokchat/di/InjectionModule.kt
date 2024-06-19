package com.robbies.ucokchat.di

import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.robbies.ucokchat.data.FirebaseRepository
import com.robbies.ucokchat.data.GroupChatRepository
import com.robbies.ucokchat.ui.screen.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseDatabaseModule = module {
    single {
        Firebase.database
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
}