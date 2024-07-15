package com.fcerio.local.di

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext application: Context): SharedPreferences {
        val filename = application.packageName + "_preferences"
        return if (BuildConfig.DEBUG) {
            application.getSharedPreferences(filename, Context.MODE_PRIVATE)
        } else {
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .build()

            val masterKey = MasterKey.Builder(
                application.applicationContext,
                MasterKey.DEFAULT_MASTER_KEY_ALIAS
            )
                .setKeyGenParameterSpec(keyGenParameterSpec)
                .build()

            EncryptedSharedPreferences
                .create(
                    application,
                    filename,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
        }
    }

    private fun getDefaultPreferencesName(application: Context): String {
        return application.packageName + "_preferences"
    }

}
