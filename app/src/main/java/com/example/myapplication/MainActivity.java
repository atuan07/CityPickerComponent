package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import com.atuan.citypicker.CityPickerActivity;
import com.atuan.citypicker.model.CityListBean;
import com.atuan.citypicker.model.Point;
import com.atuan.citypicker.utils.BarUtils;
import com.atuan.citypicker.utils.CityPickerSPUtils;


public class MainActivity extends AppCompatActivity {

    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BarUtils.setStatusBarLightMode(this, true);

        //示例
        String json ="[{\"firstLetter\":\"A\",\"cityList\":[{\"code\":152900,\"name\":\"阿拉善盟\",\"centerPoint\":{\"lat\":38.844814,\"lon\":105.70642},\"pinyin\":\"alashanmeng\"}]}]";
        CityPickerSPUtils.init(this);
        CityPickerSPUtils.saveData("Sp_Cp_Test",json);
        Intent intent = new Intent(MainActivity.this, CityPickerActivity.class);
        CityListBean currentCityBean = new CityListBean();
        currentCityBean.setName("北京");
        currentCityBean.setCode("110000");
        currentCityBean.setPinyin("beijing");
        currentCityBean.setFirst("B");
        currentCityBean.setCenterPoint(new Point("39.908799", "116.39741"));
        currentCityBean.setSP_CITY_DATA("Sp_Cp_Test");
        intent.putExtra("CityListBean",currentCityBean);
        testTV = findViewById(R.id.tv_test);
        testTV.setOnClickListener(view -> startActivityForResult(intent, 1001));

        //与本项目无关 获取 github 二次认证code
        String hexTime = null;
        try {
            hexTime = OTP.timeInHex(System.currentTimeMillis(), 30);
            String secret = "xxxxx";//替换成你自己账号的secret
            String code = OTP.create(secret, hexTime, 6, Type.TOTP);
            Log.d("github","github-code:"+code);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("data", requestCode + "====================onActivityResult============================>" + resultCode + "--->" + (data == null));
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001 && data != null) {
                CityListBean dataBase = (CityListBean) data.getSerializableExtra("picked_city");
                if (dataBase != null){
                    Log.w("data", requestCode + "======================>" + dataBase.toString());
                    testTV.setText("当前选择的城市信息：\n"+dataBase.toString());
                }else{
                    Log.w("data", requestCode + "======================>null");
                }
            }
        }
    }
}