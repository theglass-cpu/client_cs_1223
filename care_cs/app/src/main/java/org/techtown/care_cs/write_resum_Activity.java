package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class write_resum_Activity extends AppCompatActivity {

    Button resum_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_resum);
        resum_bt=findViewById(R.id.resum_bt);

        resum_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),resum_board_Activity.class);
                                                startActivity(intent);
            }
        });


    }
}