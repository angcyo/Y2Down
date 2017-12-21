package com.example.niftynotificationlib;

import com.example.niftynotificationlib.effects.BaseEffect;
import com.example.niftynotificationlib.effects.Flip;
import com.example.niftynotificationlib.effects.Jelly;
import com.example.niftynotificationlib.effects.Scale;
import com.example.niftynotificationlib.effects.SlideIn;
import com.example.niftynotificationlib.effects.SlideOnTop;
import com.example.niftynotificationlib.effects.Standard;
import com.example.niftynotificationlib.effects.ThumbSlider;

/*
 * Copyright 2014 gitonway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public enum Effects {
    standard(Standard.class),
    slideOnTop(SlideOnTop.class),
    flip(Flip.class),
    slideIn(SlideIn.class),
    jelly(Jelly.class),
    thumbSlider(ThumbSlider.class),
    scale(Scale.class);


    private Class<? extends BaseEffect> effectsClazz;

    private Effects(Class<? extends BaseEffect> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffect getAnimator() {
        BaseEffect bEffects=null;
        try {
            bEffects = effectsClazz.newInstance();
        } catch (ClassCastException e) {
            throw new Error("Can not init animatorClazz instance");
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            throw new Error("Can not init animatorClazz instance");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            throw new Error("Can not init animatorClazz instance");
        }
        return bEffects;
    }
}
