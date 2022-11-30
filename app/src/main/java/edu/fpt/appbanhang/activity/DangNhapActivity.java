package edu.fpt.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import edu.fpt.appbanhang.MainActivity;
import edu.fpt.appbanhang.R;
import edu.fpt.appbanhang.model.user;
import edu.fpt.appbanhang.retrofit.ApiAppBanHang;
import edu.fpt.appbanhang.retrofit.RetrofitClient;
import edu.fpt.appbanhang.utils.Utils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdK;
    EditText edit_email,edit_pass;
    AppCompatButton btnDangNhap;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiAppBanHang apiAppBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        Paper.init(this);
        apiAppBanHang= RetrofitClient.getInstane(Utils.BASE_URL_fpt).create(ApiAppBanHang.class);
        initView();
        initControl();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
    private void initControl() {
        txtdK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(i);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email=edit_email.getText().toString().trim();
                String str_pass=edit_pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập pass",Toast.LENGTH_SHORT).show();
                }else {
                    compositeDisposable.add(apiAppBanHang.getUser(str_email,str_pass).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_current=userModel.getResult().get(0);
                                            if(Utils.user_current.getEmail().equals(str_email)&&Utils.user_current.getPass().equals(str_pass)){
                                                Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                                Paper.book().write("email",str_email);
                                                Paper.book().write("pass",str_pass);
                                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(i);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"email hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    },throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        txtdK=findViewById(R.id.txtDangKyUser);
        edit_email=findViewById(R.id.emailDN);
        edit_pass=findViewById(R.id.passDN);
        btnDangNhap=findViewById(R.id.btnDangNhap);
        edit_email.setText(Paper.book().read("email"));
//        edit_pass.setText(Paper.book().read("pass"));
    }
}