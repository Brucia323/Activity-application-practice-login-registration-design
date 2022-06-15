package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends Activity {
    String firstPwd, secondPwd;
    boolean isYes = true;
    String userSex;
    private EditText edtNewUserName;
    private EditText edtPwd1, edtPwd2;
    private EditText edtYear;
    private RatingBar rtMarks;
    private CheckBox cbRead, cbSing, cbComputer, cbSwing, cbGame, cbDancing, cbWuShu, cbRun;
    private RadioGroup rdgGender;
    private Spinner spnProvince, spnCity;
    private Button btnReg, btnYear;
    private List<String> cityList;

    //JSONObject对象，处理一个一个的对象
    //JSONObject对象，处理一个一个集合或者数组
    //保存带集合的json字符串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        edtNewUserName.setOnFocusChangeListener((v, hasFocus) -> {
            String name = edtNewUserName.getText().toString();
            if (name.length() < 4) {
                Toast.makeText(MainActivity2.this, "用户名长度不能小于4", Toast.LENGTH_LONG).show();

            }
        });
        edtPwd1.setOnFocusChangeListener((v, hasFocus) -> {
            firstPwd = edtPwd1.getText().toString();
            if (firstPwd.length() < 6 || firstPwd.length() > 12) {
                Toast.makeText(MainActivity2.this, "用户名必须在6~12个字符之间", Toast.LENGTH_LONG).show();
            }
        });
        edtPwd2.setOnFocusChangeListener((v, hasFocus) -> {
            secondPwd = edtPwd2.getText().toString();
            if (secondPwd.length() < 6 || secondPwd.length() > 12) {
                Toast.makeText(MainActivity2.this, "用户名必须在6~12个字符之间", Toast.LENGTH_LONG).show();
            }
        });
        btnReg.setOnClickListener(v -> {
            if (!(cbDancing.isChecked() || cbRun.isChecked() || cbWuShu.isChecked() || cbGame.isChecked() || cbComputer.isChecked() || cbSing.isChecked() || cbSwing.isChecked() || cbRead.isChecked())) {
                Toast.makeText(MainActivity2.this, "爱好必须选择一个", Toast.LENGTH_LONG).show();
                isYes = false;
            } else {
                isYes = true;
            }

            if (!(edtPwd1.getText().toString().equals(edtPwd2.getText().toString()))) {
                Toast.makeText(MainActivity2.this, "两次输入密码要一致", Toast.LENGTH_LONG).show();
                isYes = false;
            } else {
                isYes = true;
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
            builder.setMessage("确认注册？");
            builder.setPositiveButton("是", (dialog, which) -> {

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("username", edtNewUserName.getText().toString());
                intent.putExtra("password", edtPwd1.getText().toString());
                startActivity(intent);
            });
            builder.setNegativeButton("否", (dialog, which) -> isYes = false);
            builder.create().show();

        });

        rdgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rdMale:
                    Toast.makeText(MainActivity2.this, "男", Toast.LENGTH_LONG).show();
                    userSex = "男";
                    break;
                case R.id.rdFemale:
                    Toast.makeText(MainActivity2.this, "女", Toast.LENGTH_LONG).show();
                    userSex = "女";
                    break;
            }
        });
        rtMarks.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Toast.makeText(MainActivity2.this, rating + "", Toast.LENGTH_LONG).show());

        List<String> provinceList = new ArrayList<>();
        cityList = new ArrayList<>();

        //初始化省数据 读取省数据并显示到下拉框
        try {
            InputStream inputStream = getResources().getAssets().open("City2.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }
            Log.d("lines", stringBuffer.toString());

            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //循环这个文件数组、获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                provinceList.add(provinceName);
            }
            Log.d("lines", provinceList.toString());

            btnYear.setOnClickListener(v -> {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity2.this);
                dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> edtYear.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日"));
                dialog.show();
            });

            //定义省份显示适配器
            ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_spinner_item, provinceList);
            provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spnProvince.setAdapter(provinceAdapter);

            spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        //根据当前位置的省份所在的数组位置、获取城市的数组
                        JSONObject provinceObject = Data.getJSONObject(position);
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //更新列表数据
                        if (cityList != null) {
                            cityList.clear();
                        }

                        for (int i = 0; i < cityArray.length(); i++) {
                            cityList.add(cityArray.getString(i));
                        }

                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_spinner_item, cityList);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spnCity.setAdapter(cityAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void initView() {

        edtNewUserName = this.findViewById(R.id.edtNewUserName);
        edtPwd1 = this.findViewById(R.id.edtPwd);
        edtPwd2 = this.findViewById(R.id.edtSndPwd);
        cbRead = this.findViewById(R.id.cbRead);
        cbComputer = this.findViewById(R.id.cbCompute);
        cbSing = this.findViewById(R.id.cbSing);
        cbSwing = this.findViewById(R.id.cbSwing);
        cbGame = this.findViewById(R.id.cbGame);
        cbWuShu = this.findViewById(R.id.cbWushu);
        cbRun = this.findViewById(R.id.cbRunning);
        cbDancing = this.findViewById(R.id.cbDancing);
        btnReg = this.findViewById(R.id.btnRegister);
        rtMarks = this.findViewById(R.id.rtMarks);
        rdgGender = this.findViewById(R.id.rdgGender);
        spnProvince = this.findViewById(R.id.spnProvinces);
        spnCity = this.findViewById(R.id.spnCities);
        edtYear = this.findViewById(R.id.edtYear);
        btnYear = this.findViewById(R.id.btnYear);
        isYes = true;
    }

}

