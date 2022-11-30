package edu.fpt.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

import edu.fpt.appbanhang.R;
import edu.fpt.appbanhang.adapter.GioHangAdapter;
import edu.fpt.appbanhang.model.EventBus.TingTongBus;
import edu.fpt.appbanhang.model.GioHang;
import edu.fpt.appbanhang.utils.Utils;

public class GioHangActivity extends AppCompatActivity {
    TextView TvgioHangTRong, TvtongTien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnMuahang;
    GioHangAdapter adapter;
    List<GioHang> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinTongTien();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
        public void eventTingTong(TingTongBus event){
            if(event!=null){
                tinTongTien();
                if(Utils.gioHangList.size()==0){
                    TvgioHangTRong.setVisibility(View.VISIBLE);

                }
            }

    }

    private void tinTongTien() {
        long tongtien=0;
        for(int i=0;i<Utils.gioHangList.size();i++){
            tongtien=tongtien+(Utils.gioHangList.get(i).getSlSp()*Utils.gioHangList.get(i).getGiaSp());
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        TvtongTien.setText(decimalFormat.format(tongtien)+"Đ");
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.gioHangList.size()==0){
            TvgioHangTRong.setVisibility(View.VISIBLE);

        }else {
            adapter=new GioHangAdapter(getApplicationContext(),Utils.gioHangList);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initView() {
        TvgioHangTRong=findViewById(R.id.txtgiohangtrong);
        TvtongTien=findViewById(R.id.txtTongTien);
        toolbar=findViewById(R.id.toolbargiohang);
        recyclerView=findViewById(R.id.reycGioHang);
        btnMuahang=findViewById(R.id.btnMuaHang);
    }
}