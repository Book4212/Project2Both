package com.example.project.studentnamereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void goStudentCardReader(View view) {
        Intent intent = new Intent(this, StudentCardReaderActivity.class);
        String courseID_sec = getIntent().getExtras().getString("IndexActivitySend");
        intent.putExtra("keyCourseID_sec", courseID_sec);
        //Log.d("print", courseID_sec);
        switch (view.getId()) {
            case R.id.comeClass:
                intent.putExtra("keyMenu", "come class");
                startActivity(intent);
                break;
            case R.id.comeLate:
                intent.putExtra("keyMenu", "come late");
                startActivity(intent);
                break;
            case R.id.outClass:
                intent.putExtra("keyMenu", "out class");
                startActivity(intent);
                break;
            default:
                intent.putExtra("keyMenu", "activity");
                startActivity(intent);
                break;
        }
    }

    public void goGiveScore(View view) {
        Intent intent = new Intent(this, GiveScoreActivity.class);
        String courseID_sec = getIntent().getExtras().getString("IndexActivitySend");
        intent.putExtra("keyCourseID_sec", courseID_sec);
        startActivity(intent);
    }
}
