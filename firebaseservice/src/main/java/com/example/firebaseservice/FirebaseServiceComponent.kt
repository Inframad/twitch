package com.example.firebaseservice

import android.content.Context
import com.example.twitchapp.FirebaseServiceModuleDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [
    FirebaseServiceModuleDependencies::class
])
interface FirebaseServiceComponent {

    fun inject(service: FirebaseService)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependicies(deps: FirebaseServiceModuleDependencies): Builder
        fun build(): FirebaseServiceComponent
    }
}