package com.example.numberbaseball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] comNumbers = new int[3]; // 3개의 랜덤한 숫자(=정답) 저장용 배열
    int inputTextCount = 0; // 버튼을 눌렸을때 누른 버튼 개수 카운트 용, 숫자버튼을 3개 누른후에 더 이상 누르지 못하게 한다.
    int hitCount = 1; // 몇번째 시도(정답확인)인지

    TextView[] inputTextView = new TextView[3]; // 내가 선택한 숫자 저장, 야구공 출력용
    Button[] numButton = new Button[10]; // 내가 누른 숫자 버튼

    ImageButton backSpaceButton; // 지우기 버튼
    ImageButton hitButton; // 정답입력 버튼
    ImageButton resetButton; // 게임 초기화 버

    TextView resultTextView; // 정답입력버튼 눌렀을때 나온 결과 출력용 TextView
    ScrollView scrollView; // 결과창의 기록이 넘쳤을때 스크롤을 올려서 이전 기록을 보기위한 ScrollView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // setContentView 메소드가 xml 에 있는 뷰들을 메모리상에 객체화시킨다.

        comNumbers = getComNumbers(); // 정답 숫자 랜덤 생성후 comNumbers 에 저장

        for (int i = 0; i < inputTextView.length; i++) {
            inputTextView[i] = findViewById(R.id.input_text_view_0 + i);
        }
        for (int i = 0; i < numButton.length; i++) {
            numButton[i] = findViewById(R.id.num_button_0 + i);
        }

        backSpaceButton = findViewById(R.id.backspace_button);
        hitButton = findViewById(R.id.hit_button);
        resultTextView = findViewById(R.id.result_text_view);
        scrollView = findViewById(R.id.scroll_view);
        resetButton = findViewById(R.id.reset_button);

        // 숫자버튼을 클릭했을때 야구공모양에 해당 숫자 출력되게 하기 + 중복,개수초과 방지
        for(Button getNumButton : numButton){ // getNumButton 으로 numButton 배열을 순차적으로 받고
            getNumButton.setOnClickListener(new View.OnClickListener() { // 이것을 통해
                @Override
                public void onClick(View v) {
                    if (inputTextCount < 3) {
                        // 버튼이 클릭되었을때 어떻게 될지 구현하는 부분
                        Button button = findViewById(v.getId());
                        // findViewById로 선택한 버튼을 받아와서 변수 button 에 저장한다., R.id가 아니라 여기서는 View v 로 받기 때문에 v.getId() 로 받아온다.
                    /*  String getButtonNumber = button.getText().toString();
                    *   inputTextView[inputTextCount].setText(getButtonNumber); */
                        // 위에 두줄을 간단하게 하면 아래
                        inputTextView[inputTextCount].setText(button.getText().toString()); // 선택한 숫자 버튼에 대한 숫자 텍스트를 inputTextView 의 inputTextCount 번째에 저장한다.
                        button.setEnabled(false); // 중복된 숫자 선택 방지용으로 버튼 비활성화
                        inputTextCount++; // 선택한 숫자 개수 카운트
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "정답입력 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                    // Toast 는 하단에 작은 텍스트 메세지를 잠시 보여주는 이벤트이다.
                    }
                }
            });
        }

        // 지우기 버튼 OnClick 이벤트 설정
        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTextCount > 0) {
                    int buttonEnableCount = Integer.parseInt(inputTextView[inputTextCount - 1].getText().toString()); // 좀 전에 누른 숫자버튼의 숫자의 텍스트(문자열)를 받아와 int 로 바꿈
                    numButton[buttonEnableCount].setEnabled(true); // 해당버튼을 enable true 로 바꿈
                    inputTextView[inputTextCount - 1].setText(""); // ""를 통해 야구공의 텍스트 비워줌
                    // inputTextCount-1 인 이유는 위의 for 문에서 inputTextCount 가 마지막에 1이 또 추가된상황이기 때문에 -1 을 빼줘야 인덱스 2부터 접근한다.
                    inputTextCount--;
                }
                else{
                    Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 정답입력버튼 OnClick 이벤트 설정
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTextCount < 3){
                    Toast.makeText(getApplicationContext(), "숫자를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    int[] userNumbers = new int[3]; // 내가 누른 버튼의 숫자를 저장할 배열
                    for (int i = 0; i < userNumbers.length ; i++) {
                        userNumbers[i] = Integer.parseInt(inputTextView[i].getText().toString());
                    }
                    int[] countCheck; // [0]은 strike [1]은 ball 카운트
                    countCheck = getCountCheck(comNumbers, userNumbers);
                    Log.e("hitButton", "countCheck = S : " + countCheck[0] + " B : " + countCheck[1]); // 확인용 로그

                    String resultCount;
                    if (countCheck[0] == 3){ // 정답, 아웃
                        resultCount = hitCount + "  [ " + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2] + "] 아웃 - 축하합니다!!";

                    } else { // 정답이 아닐경우 결과를 출력
                        resultCount = hitCount + "  [ " + userNumbers[0] + " " + userNumbers[1] + " " + userNumbers[2] + "]  S : " + countCheck[0] + " B : " + countCheck[1];
                    }
                    if (hitCount == 1) { // 처음으로 입력했을때
                        resultTextView.setText(resultCount + "\n");
                    } else { // 그다음엔 append 로 내용을 이어나간다.
                        resultTextView.append(resultCount + "\n");
                    }
                    if (countCheck[0] == 3){ //
                        hitCount = 1;
                        comNumbers = getComNumbers();
                    }
                    else { hitCount++;}

                    scrollView.fullScroll(View.FOCUS_DOWN); // FOCUS_DOWN 으로 설정하므로서 결과창이 밑으로 게속 내려간다.

                    inputTextCount = 0; // 선택한 숫자 카운트 초기
                    for (TextView textView : inputTextView) {
                        textView.setText(""); // 선택한 숫자 전부 텍스트 지움
                    }
                    for (Button button : numButton){
                        button.setEnabled(true); // 선택한 숫자 버튼 전부 활성화
                    }
                }
            }
        });

        // 리셋버튼
       resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTextView.setText("");
                Toast.makeText(getApplicationContext(), "게임이 다시 시작되었습니다.", Toast.LENGTH_SHORT).show();
                hitCount = 1;
                comNumbers = getComNumbers();
                inputTextCount = 0; // 선택한 숫자 카운트 초기
                for (TextView textView : inputTextView) {
                    textView.setText(""); // 선택한 숫자 전부 텍스트 지움
                }
                for (Button button : numButton){
                    button.setEnabled(true); // 선택한 숫자 버튼 전부 활성화
                }
            }
        });

    }
    // 정답 체크용 메서드, comNumbers 는 정답 배열, userNumbers 는 내가 누른 숫자 배열
    private int[] getCountCheck(int[] comNumbers, int[] userNumbers) {
        int[] setCount = new int[2]; // 스트라잌과 볼을 저장할 2칸짜리 int 형 배열
        for (int i = 0; i < comNumbers.length; i++) {
            for (int j = 0; j < userNumbers.length; j++) {
                if (comNumbers[i] == userNumbers[j]) {
                    if (i==j) {
                        setCount[0]++; // 스트롸잌
                    }
                    else {
                        setCount[1]++; // 볼
                    }
                }
            }
        }
        return setCount;
    }

    // 랜덤의 3개의 숫자인 정답을 생성하는 메소드
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