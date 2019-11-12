// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.firebase.samples.apps.mlkit.java.imagelabeling;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.evergreenlabs.languagelearningbuddy.util.Translator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay.Graphic;

import java.util.List;

import static java.lang.Math.round;

/** Graphic instance for rendering a label within an associated graphic overlay view. */
public class LabelGraphic extends Graphic {

  private final Paint textPaint;
  private final GraphicOverlay overlay;

  private final List<FirebaseVisionImageLabel> labels;
  private Translator translator;

  LabelGraphic(GraphicOverlay overlay, List<FirebaseVisionImageLabel> labels) {
    super(overlay);
    this.overlay = overlay;
    this.labels = labels;
    textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(60.0f);
    translator = new Translator();
  }

  @Override
  public synchronized void draw(final Canvas canvas) {

      if (translator.isReady()) {
        float x = overlay.getWidth() / 4.0f;
        float y = overlay.getHeight() / 2.0f;
        int i = 0;


        for (FirebaseVisionImageLabel label : labels) {

          final float currentX = x;
          final float currentY = y;

          Task<String> translatedText = translator.translate(label.getText());
          if (translatedText != null) {
            translatedText.addOnCompleteListener(
                    task -> {
                      StringBuffer output = new StringBuffer(label.getText());
                      if (task.isSuccessful()) {
                        output.append(" (").append(task.getResult()).append(")");
                      }
                      canvas.drawText(output.toString(), currentX, currentY, textPaint);
                    }
            );
          }

          y = y - 62.0f;
          i++;

          if (i >= 3)         // show top 3 only
            break;
        }
    }
  }
}
