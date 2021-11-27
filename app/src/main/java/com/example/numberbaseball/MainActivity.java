package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] comNumbers = new int[3]; // 3개의 랜덤한 숫자 저장용 배열

    TextView[] inputTextView = new TextView[3]; // 내가 선택한 숫자 저장
    Button[] numButton = new Button[10]; // 내가 누른 숫자 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comNumbers = getComNumbers(); // 여기서 생성하고 저장한다.
        for (int i = 0; i < inputTextView.length; i++) {
            inputTextView[i] = findViewById(R.id.input_text_view_0 + i);
        }
        for (int i = 0; i < numButton.length; i++) {
            numButton[i] = findViewById(R.id.num_button_0 + i);
        }
        
    }

    public int[] getComNumbers() {
        int[] setComNumbers = new int[3];
        for (int i = 0; i < setComNumbers.length; i++) {
            // i -> 0 , 1, 2 일때 0~9숫자 랜덤 저장
            setComNumbers[i] = new Random().nextInt(10);
            for (int j = 0; j < i; j++) {
                // 3개의 숫자가 겹치면 안되기때문에 겹치면 i를 감소시켜 다시 i번째 배열에 랜덤숫자를 저장하게함
                if (setComNumbers[i] == setComNumbers[j]) {
                    i--;
                    break;
                }
            }
        }
        // 생성된 3개의 랜덤한 숫자가 Log.e 를 통해 하단의 logcat 에서 빨간색으로 출력된다.
        Log.e("setComNumbers", "setComNumbers = " + setComNumbers[0] + ", " + setComNumbers[1] + ", " + setComNumbers[2]);
        return setComNumbers;

    }
}