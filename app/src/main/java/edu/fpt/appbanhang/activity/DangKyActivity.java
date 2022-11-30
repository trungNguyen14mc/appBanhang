package edu.fpt.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.fpt.appbanhang.R;
import edu.fpt.appbanhang.adapter.SpMoiAdapter;
import edu.fpt.appbanhang.retrofit.ApiAppBanHang;
import edu.fpt.appbanhang.retrofit.RetrofitClient;
import edu.fpt.appbanhang.utils.Utils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email,pass,repass,username,mobile;
    AppCompatButton btndangky;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiAppBanHang apiAppBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
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
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangky();
            }
        });
    }

    private void dangky() {
        String str_email=email.getText().toString().trim();
        String str_pass=pass.getText().toString().trim();
        String str_repass=repass.getText().toString().trim();

        String str_username=username.getText().toString().trim();
        String str_mobile=mobile.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập pass",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Repass",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Số điện thoại",Toast.LENGTH_SHORT).show();
        }else {
            if(str_pass.equals(str_repass)){
                compositeDisposable.add(apiAppBanHang.post_user(str_email,str_pass,str_username,str_mobile).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                },throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
            else {
                Toast.makeText(getApplicationContext(),"Pass chưa khớp",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initView() {
        email=findViewById(R.id.edit_query_email);
        pass=findViewById(R.id.edit_query_pass);
        repass=findViewById(R.id.edit_query_pass2);
        username=findViewById(R.id.edit_query_username);
        mobile=findViewById(R.id.edit_query_mobile);
        btndangky=findViewById(R.id.btnDangKy);
    }
}