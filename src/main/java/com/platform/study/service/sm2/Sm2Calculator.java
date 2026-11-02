package com.platform.study.service.sm2;

import com.platform.study.service.model.sm2.Sm2Result;
import org.springframework.stereotype.Component;

import static java.lang.Math.round;

@Component
public class Sm2Calculator {
    public Sm2Result calculate(int repetition, float easeFactor, int interval, int quality) {

        if(quality >= 3) {
            if(repetition == 0){
                interval = 1;
            }else
            if(repetition == 1){
                interval = 6;
            }
            else {
                interval = round(interval * easeFactor);
            }
            repetition +=1;
        }

        if(quality < 3){
            repetition = 0;
            interval = 1;
        }

        float newEaseFactor = calculateEaseFactor(easeFactor, quality);
        if (newEaseFactor < 1.3){
            newEaseFactor = 1.3f;
        }
        easeFactor = newEaseFactor;

        Sm2Result result = new Sm2Result(repetition, easeFactor, interval);

        return result;
    }

    private float calculateEaseFactor(float easeFactor, int quality){

        float ef;

        ef = (float) (easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)));

        return ef;

    }
}
