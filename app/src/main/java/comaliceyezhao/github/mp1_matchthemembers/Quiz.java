package comaliceyezhao.github.mp1_matchthemembers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Quiz extends AppCompatActivity {
    private HashMap<String, Drawable> members = new HashMap<>();
    private int score;
    private TextView scoreView;
    private TextView timeView;
    private ImageView pic;
    private CountDownTimer timer;
    private Button[] nameOptions;
    private Button endgame;
    private String memberName;


    private void makeMap() {
        String[] memberNames = {"Aayush Tyagi", "Abhinav Koppu", "Aditya Yadav", "Ajay Merchia", "Alice Zhao", "Amy Shen", "Anand Chandra", "Andres Medrano", "Angela Dong", "Anika Bagga", "Anmol Parande", "Austin Davis", "Ayush Kumar", "Brandon David", "Candice Ye", "Carol Wang", "Cody Hsieh", "Daniel Andrews", "Daniel Jing", "Eric Kong", "Ethan Wong", "Fang Shuo", "Izzie Lau", "Jaiveer Singh", "Japjot Singh", "Jeffery Zhang", "Joey Hejna", "Julie Deng", "Justin Kim", "Kaden Dippe", "Kanyes Thaker", "Kayli Jiang", "Kiana Go", "Leon Kwak", "Levi Walsh", "Louie Mcconnell", "Max Miranda", "Michelle Mao", "Mohit Katyal", "Mudabbir Khan", "Natasha Wong", "Nikhar Arora", "Noah Pepper", "Radhika Dhomse", "Sai Yandapalli", "Saman Virai", "Sarah Tang", "Sharie Wang", "Shiv Kushwah", "Shomil Jain", "Shreya Reddy", "Shubha Jagannatha", "Shubham Gupta", "Srujay Korlakunta", "Stephen Jayakar", "Suyash Gupta", "Tiger Chen", "Vaibhav Gattani", "Victor Sun", "Vidya Ravikumar", "Vineeth Yeevani", "Wilbur Shi", "William Lu", "Will Oakley", "Xin Yi Chen", "Young Lin"};

        for (String name : memberNames) {
            String filename = name.replaceAll("\\s","").toLowerCase()+".png";
//            int photoId = getResources().getIdentifier(filename, "drawable", getPackageName());
//            pic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), photoId, null));
//            pic.setImageResource(getResources().getIdentifier(filename,"drawable", getPackageName()));
            String pathName = "/res/drawable/"+filename;
            Drawable image = Drawable.createFromPath(pathName);
            members.put(name, image);
        }
    }

    public void makeContact(View v) {
        timer.cancel();
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, memberName);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        makeMap();
        Button name1 = findViewById(R.id.name1);
        Button name2 = findViewById(R.id.name2);
        Button name3 = findViewById(R.id.name3);
        Button name4 = findViewById(R.id.name4);
        nameOptions = new Button[]{name1, name2, name3, name4};
        endgame = findViewById(R.id.endgame);
        scoreView = findViewById(R.id.scoreView);
        timeView = findViewById(R.id.timeView);
        pic = findViewById(R.id.pic);
        score = 0;

        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Long timeLeft = millisUntilFinished / 1000;
                timeView.setText(String.valueOf(timeLeft));
            }

            @Override
            public void onFinish() {
                timeView.setText(String.valueOf(0));
                changeMembers(false);
            }
        };

        changeMembers(false);

        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMembers(nameOptions[0].getText().toString().equals(memberName));
            }
        });

        name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMembers(nameOptions[1].getText().toString().equals(memberName));
            }
        });

        name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMembers(nameOptions[2].getText().toString().equals(memberName));
            }
        });

        name4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMembers(nameOptions[3].getText().toString().equals(memberName));
            }
        });

    }

    @Override
    protected void onResume() {
        timer.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    private void changeMembers(boolean correct) {
        if (correct) {
            score++;
        } else {
            incorrectChoice();
        }

        scoreView.setText(String.valueOf(score));

        ArrayList<String> nameKeys = new ArrayList<>();
        nameKeys.addAll(members.keySet());
        Collections.shuffle(nameKeys);

        memberName = nameKeys.get(3);
        Drawable memberPhoto = members.get(memberName);
        pic.setImageDrawable(memberPhoto);

        Integer[] options_ = {0, 1, 2, 3};
        ArrayList<Integer> options = new ArrayList<>(Arrays.asList(options_));
        Random generator = new Random();
        int correctOption = generator.nextInt(4);

        nameOptions[correctOption].setText(memberName);
        options.remove(correctOption);

        nameOptions[options.get(0)].setText(nameKeys.get(0));
        nameOptions[options.get(1)].setText(nameKeys.get(1));
        nameOptions[options.get(2)].setText(nameKeys.get(2));

        timer.start();
    }

    private void incorrectChoice() {
        Toast.makeText(getApplicationContext(), "Wrong answer! >:(", Toast.LENGTH_SHORT).show();
    }

    public void endGame(View v) {
        timer.cancel();
        new AlertDialog.Builder(this)
                .setTitle("END GAME")
                .setMessage("Are you sure you want to end the game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timer.start();
                    }
                }).show();
    }

}
