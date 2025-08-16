# Keep Firebase model classes
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep Google Identity classes
-keep class com.google.android.libraries.identity.googleid.** { *; }

# Keep Hilt generated code
-keep class dagger.hilt.internal.generated.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Compose
-dontwarn kotlin.**