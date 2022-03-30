object Versions {
    const val retrofit = "2.9.0"
    const val room = "2.4.2"
    const val lifecycle = "2.4.1"
    const val paging = "3.1.1"
    const val glide = "4.13.0"
    const val navigation = "2.4.1"
    const val hilt = "2.38.1"
    const val view_binding_property_delegate = "1.4.4"
    const val moshi= "1.13.0"
    const val firebase = "29.2.0"
    const val rx = "3.0.0"
    const val swipe_refresh_layout = "1.1.0"
    const val androidx_core_ktx = "1.7.0"
    const val app_compat = "1.4.1"
    const val android_material = "1.5.0"
    const val constraint_layout = "2.1.3"
    const val desugar_jdk_libs = "1.1.5"
}

@Suppress("unused")
object DefaultConfig {
    const val minSdk = 21
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0"
}

@Suppress("unused")
object Deps {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_converter_moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val retrofit_adapter_rxJava3 = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    const val room_rxJava3 = "androidx.room:room-rxjava3:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val paging_runtime_ktx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val paging_rxJava3 = "androidx.paging:paging-rxjava3:${Versions.paging}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    const val viewbinding_property_delegate = "com.github.kirich1409:viewbindingpropertydelegate:${Versions.view_binding_property_delegate}"

    const val moshi_kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshi_kotlin_codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"

    const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val firebase_messaging = "com.google.firebase:firebase-messaging"

    const val rx3android = "io.reactivex.rxjava3:rxandroid:${Versions.rx}"
    const val rx3java = "io.reactivex.rxjava3:rxjava:${Versions.rx}"
    const val rx3kotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rx}"

    const val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipe_refresh_layout}"

    const val androidx_core_ktx = "androidx.core:core-ktx:${Versions.androidx_core_ktx}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
    const val android_material = "com.google.android.material:material:${Versions.android_material}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

    const val desugar_jdk_libs = "com.android.tools:desugar_jdk_libs:${Versions.desugar_jdk_libs}"
}