package com.evergreenlabs.languagelearningbuddy.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class Translator {

    private static final String TAG = Translator.class.getSimpleName();
    private FirebaseTranslator translatorEnZh;

    public boolean isReady() {
        return ready;
    }

    private boolean ready = false;

    public Translator() {
        super();
        init();
    }

    private void init() {

        // Create an English-German translator:
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.ZH)
                        .build();
        translatorEnZh =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        Log.d(TAG, "downloading model");

        translatorEnZh.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        v -> {
                            Log.d(TAG, "downloaded model");
                            // Model downloaded successfully. Okay to start translating.
                            this.ready = true;
                        })
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, e.getLocalizedMessage());
                        });


    }

    public Task<String> translate(String originalText) {

        if (ready)
            return translatorEnZh.translate(originalText);
        else {
            Log.e(TAG, "translation model not ready yet");
            return null;                // not ready
        }


    }
}
