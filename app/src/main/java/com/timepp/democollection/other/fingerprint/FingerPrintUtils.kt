package com.timepp.democollection.other.fingerprint

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

@TargetApi(Build.VERSION_CODES.M)
class FingerPrintUtils private constructor(context: Context)  {
    companion object {
        private const val ANDROID_KEYSTORE_NAME = "AndroidKeyStore"
        private const val KEY_NAME = "demo_fingerprint_key_name"
        private var INSTANCE: FingerPrintUtils? = null
        fun getInstance(context: Context): FingerPrintUtils {
            val instance = INSTANCE ?: FingerPrintUtils(context)
            if (INSTANCE == null) {
                INSTANCE = instance
            }
            return instance
        }
    }

    private var mKeyStore: KeyStore? = null
    private var mCipher: Cipher? = null
    private var mFingerPrintManager = context.getSystemService(FingerprintManager::class.java)
    private val mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    private var mCancellationSignal: CancellationSignal? = null

    private fun isHardwareDetected(): Boolean {
        return mFingerPrintManager != null && mFingerPrintManager.isHardwareDetected
    }

    private fun hasEnrollFingerprints(): Boolean {
        return isHardwareDetected() && mFingerPrintManager != null && mFingerPrintManager.hasEnrolledFingerprints()
    }

    private fun createKey(): Boolean {
        if (mKeyStore == null) {
            mKeyStore = KeyStore.getInstance(ANDROID_KEYSTORE_NAME)
        }
        mKeyStore?.load(null)
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE_NAME)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
        keyGenerator.generateKey()
        return true
    }

    private fun initCipher(): Boolean {
        mKeyStore?.let {
            it.load(null)
            val key = it.getKey(KEY_NAME, null)
            if (mCipher == null) {
                mCipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" +
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                mCipher?.init(Cipher.ENCRYPT_MODE, key)
            }
            return true
        }
        return false
    }

    fun startListener(context: Context, callback: FingerprintManager.AuthenticationCallback) {
        if (createKey() && initCipher()) {
            mCancellationSignal = CancellationSignal()
            mCipher?.let {
                mFingerPrintManager?.authenticate(FingerprintManager.CryptoObject(it), mCancellationSignal, 0, callback, Handler(context.mainLooper))
            }
        }
    }

    fun stopListener() {
        mCancellationSignal?.cancel()
        mCancellationSignal = null
    }
}