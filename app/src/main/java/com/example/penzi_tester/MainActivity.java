package com.example.penzi_tester;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_view;
    Button button;
    EditText input;
    public static int rem,j,total,i,k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.button_send);
        text_view = findViewById(R.id.textView);
        input = findViewById(R.id.text_input);

        text_view.setMovementMethod(new ScrollingMovementMethod());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//            MatchUserResponse();
            if(TextUtils.equals(input.getText().toString().toLowerCase(), "details"))
            {

                getBodyDetails();

            }
            else if(TextUtils.equals(input.getText().toString().toLowerCase(), "match"))
            {
                StartNext();
            }

            else if(TextUtils.equals(input.getText().toString().toLowerCase(), "next"))
            {
                Next();
            }

            else
            {
                text_view.setText("Sweet Nothing");
            }


            }
        });


    }

    public int getTotalValue()
    {
        Integer age1 = 20, age2 = 35;
        String county = "nairobi", gender = "female";

        Call<String> get_incoming_message = ApiClient.getUserService().UserNo(age1, age2, county, gender);
        get_incoming_message.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("Response", response.body().toString());
                if(response.isSuccessful()){
//                    Log.e("success", response.body().toString());
                    total = Integer.parseInt(response.body().toString());



                    text_view.setText(format("%d",total));

                }
                else {

                    String message = "An error occurred please try again later ...";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e( "Failure.", t.getLocalizedMessage());

            }
        });

        return total;
    }


    public void StartNext(){

        Integer age1 = 20, age2 = 35;
        String county = "nairobi", gender = "female";
        i = 0;
        j = 0;

        Call<String> get_incoming_message = ApiClient.getUserService().UserNo(age1, age2, county, gender);
        get_incoming_message.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Response", response.body().toString());
                if(response.isSuccessful()){
                    Log.e("Returned total is =>", response.body().toString());
                    total = Integer.parseInt(response.body().toString());



                    Call<List<MatchUserResponse>> get_user_details = ApiClient.getUserService().MatchUser(age1, age2, county, gender);
                    get_user_details.enqueue(new Callback<List<MatchUserResponse>>() {
                        @Override
                        public void onResponse(Call<List<MatchUserResponse>> call, Response<List<MatchUserResponse>> body_response) {
//                            Log.e("Response", body_response.body().toString());
                            if(body_response.isSuccessful()){

                                String details_response = body_response.body().toString();
//                                text_view.setText(details_response);
                                JsonArray arr = new Gson().fromJson(details_response, JsonArray.class);


                                String join = "", out_of_bounds = "";
                                String end_message = "There are no more partners to find a match. Try changing your matching criteria";

                                String next_message = "";

                                boolean success = true;

                                if (total <= 3 && total > 0) {

                                    try {

                                        for (; i <= 2; i++) {
                                            JsonElement str = arr.get(i);
                                            JsonObject obj = new Gson().fromJson(str, JsonObject.class);
                                            String name = obj.get("name").toString().replaceAll("^\"|\"$", "");
                                            String number = obj.get("number").toString().replaceAll("^\"|\"$", "");
                                            String new_number = number.replaceFirst("254", "0");
                                            String age = obj.get("age").toString();
                                            join += String.format("%s, aged %s, %s" + "\n", name, age, new_number);

                                        }

                                    } catch (IndexOutOfBoundsException e) {

                                        success = false;
                                        out_of_bounds = join + end_message;
//                            System.out.println(out_of_bounds);
                                        text_view.setText(out_of_bounds);

                                    }

                                    if (success) {

//                            System.out.println(join + end_message);
                                        text_view.setText(join + end_message);

                                    }

                                }

                                else if (total == 0) {
                                    //    System.out.println("There are no matches found. Try changing your match criteria");
                                    text_view.setText("There are no matches found. Try changing your match criteria");
                                }

                                else {

                                    while (true) {

                                        if (i > 2) {
                                            rem = total - j;
                                            next_message = String.format("Send NEXT to 5001 to receive details of the remaining %d matches.",
                                                    rem);
//                                System.out.println(join + next_message);
                                            text_view.setText(join+next_message);


                                            break;

                                        }

                                        else {
                                            while (true) {

                                                JsonElement str = arr.get(i);
                                                JsonObject obj = new Gson().fromJson(str, JsonObject.class);
                                                String name = obj.get("name").toString().replaceAll("^\"|\"$", "");
                                                String number = obj.get("number").toString().replaceAll("^\"|\"$", "");
                                                String new_number = number.replaceFirst("254", "0");
                                                String age = obj.get("age").toString();
                                                join += String.format("%s, aged %s, %s" + "\n", name, age, new_number);

                                                i = i + 1;
                                                j = j + 1;

                                                break;

                                            }

                                        }

                                    }

                                }





                                String message = "Success ...";
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


                            }
                            else {

                                String message = "An error occurred please try again later ...";
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<List<MatchUserResponse>> call, Throwable t) {

                            Log.e( "Failure.", t.getLocalizedMessage());


                        }
                    });


                }
                else {

                    String message = "An error occurred please try again later ...";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e( "Failure.", t.getLocalizedMessage());

            }
        });




//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Log.e("Total is =>", format("%d", total));
//
//                        try {
//                            Thread.sleep(9000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });



    }


    public void Next()
    {

        Integer age1 = 20;
        Integer age2 = 35;
        String county = "nairobi";
        String gender = "female";


        Call<String> get_incoming_message = ApiClient.getUserService().UserNo(age1, age2, county, gender);
        get_incoming_message.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Response", response.body().toString());
                if(response.isSuccessful()){
                    Log.e("Returned total is =>", response.body().toString());
                    total = Integer.parseInt(response.body().toString());

                    Call<List<MatchUserResponse>> get_user_details = ApiClient.getUserService().MatchUser(age1, age2, county, gender);
                    get_user_details.enqueue(new Callback<List<MatchUserResponse>>() {
                        @Override
                        public void onResponse(Call<List<MatchUserResponse>> call, Response<List<MatchUserResponse>> body_response) {
//                            Log.e("Response", body_response.body().toString());
                            if(body_response.isSuccessful()){

                                String details_response = body_response.body().toString();
//                                text_view.setText(details_response);
                                JsonArray arr = new Gson().fromJson(details_response, JsonArray.class);

                                String join = "", out_of_bounds = "";
                                String end_message = "There are no more partners to find a match. Try changing your matching criteria";

                                String next_message = "";
                                k = i + 2;
                                boolean success = true;

                                if (rem <= 3 && rem > 0) {

                                    try {

                                        for (; rem <= 2; i++) {

                                            JsonElement str = arr.get(i);
                                            JsonObject obj = new Gson().fromJson(str, JsonObject.class);
                                            String name = obj.get("name").toString().replaceAll("^\"|\"$", "");
                                            String number = obj.get("number").toString().replaceAll("^\"|\"$", "");
                                            String new_number = number.replaceFirst("254", "0");
                                            String age = obj.get("age").toString();
                                            join += String.format("%s, aged %s, %s" + "\n", name, age, new_number);

                                        }

                                    } catch (IndexOutOfBoundsException e) {

                                        success = false;
                                        out_of_bounds = join + end_message;
//                                        System.out.println(out_of_bounds);
                                        text_view.setText(out_of_bounds);

                                    }

                                    if (success) {

//                                        System.out.println(join + end_message);
                                        text_view.setText(join + end_message);

                                    }

                                }

                                else if (j == 0) {

//                                    System.out.println("There are no matches found. Try changing your match criteria");
                                    text_view.setText("There are no matches found. Try changing your match criteria");
                                }

                                else {

                                    while (true) {

                                        if (i > k) {

                                            rem = total - j;

                                            next_message = String.format("Send NEXT to 5001 to receive details of the remaining %d matches.",
                                                    rem);
//                                            System.out.println(join + next_message);
                                            text_view.setText(join + next_message);

                                            break;

                                        }

                                        else {
                                            while (true) {

                                                try {

                                                    JsonElement str = arr.get(i);
                                                    JsonObject obj = new Gson().fromJson(str, JsonObject.class);
                                                    String name = obj.get("name").toString().replaceAll("^\"|\"$", "");
                                                    String number = obj.get("number").toString().replaceAll("^\"|\"$", "");
                                                    String new_number = number.replaceFirst("254", "0");
                                                    String age = obj.get("age").toString();
                                                    join += String.format("%s, aged %s, %s" + "\n", name, age, new_number);

                                                    i = i + 1;
                                                    j = j + 1;

                                                    break;

                                                }

                                                catch(IndexOutOfBoundsException error)
                                                {
                                                    text_view.setText("There are no matches found. Try changing your match criteria");
                                                }

                                            }

                                        }

                                    }

                                }





                                String message = "Success ...";
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


                            }
                            else {

                                String message = "An error occurred please try again later ...";
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<List<MatchUserResponse>> call, Throwable t) {

                            Log.e( "Failure.", t.getLocalizedMessage());


                        }
                    });


                }
                else {

                    String message = "An error occurred please try again later ...";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e( "Failure.", t.getLocalizedMessage());

            }
        });

    }


    public void getBodyDetails()
    {
        Integer age1 = 20, age2 = 35;
        String county = "nairobi", gender = "female";


        Call<List<MatchUserResponse>> get_user_details = ApiClient.getUserService().MatchUser(age1, age2, county, gender);
        get_user_details.enqueue(new Callback<List<MatchUserResponse>>() {
            @Override
            public void onResponse(Call<List<MatchUserResponse>> call, Response<List<MatchUserResponse>> response) {
                Log.e("Response", response.body().toString());
                if(response.isSuccessful()){

                    String details_response = response.body().toString();
                    text_view.setText(details_response);

                    String message = "Success ...";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


                }
                else {

                    String message = "An error occurred please try again later ...";
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<MatchUserResponse>> call, Throwable t) {

                Log.e( "Failure.", t.getLocalizedMessage());


            }
        });
    }

}