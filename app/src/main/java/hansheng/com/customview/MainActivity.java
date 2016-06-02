package hansheng.com.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hansheng.com.customcheckbox.CustomCheckbox;

public class MainActivity extends AppCompatActivity {

    private CustomCheckbox checkBoxSample1;
    private CustomCheckbox checkBoxSample2;
    private CustomCheckbox checkBoxSample3;
    private CustomCheckbox checkBoxSample4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBoxSample1 = (CustomCheckbox) findViewById(R.id.check1);
        checkBoxSample2 = (CustomCheckbox) findViewById(R.id.check2);
        checkBoxSample3 = (CustomCheckbox) findViewById(R.id.check3);
        checkBoxSample4 = (CustomCheckbox) findViewById(R.id.check4);
        checkBoxSample1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxSample1.toggle();
            }
        });
        checkBoxSample2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxSample2.toggle();
            }
        });
        checkBoxSample3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxSample3.toggle();
            }
        });
        checkBoxSample4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxSample4.toggle();
            }
        });
    }
}
