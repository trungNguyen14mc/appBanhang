package edu.fpt.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import edu.fpt.appbanhang.Interface.ImageOnClickListener;
import edu.fpt.appbanhang.R;
import edu.fpt.appbanhang.model.EventBus.TingTongBus;
import edu.fpt.appbanhang.model.GioHang;
import edu.fpt.appbanhang.utils.Utils;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewholder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        GioHang gioHang=gioHangList.get(position);
        holder.txtTen_itemgiohang.setText(gioHang.getTenSp());
        holder.txtsoLuong_itemgiohang.setText(gioHang.getSlSp()+"");
        Glide.with(context).load(gioHang.getHinhSp()).into(holder.img_itemgiohang);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txtgia_itemgiohang.setText(decimalFormat.format(gioHang.getGiaSp())+" Đ");
        long gia=gioHang.getSlSp()*gioHang.getGiaSp();
        holder.txttong_itemgiohang.setText(decimalFormat.format(gia)+" Đ");
        holder.setListener(new ImageOnClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value ==1){
                    if(gioHangList.get(pos).getSlSp()>1){
                        int slmoi=gioHangList.get(pos).getSlSp()-1;
                        gioHangList.get(pos).setSlSp(slmoi);
                        // set lại tổng tiền
                        holder.txtsoLuong_itemgiohang.setText(gioHangList.get(pos).getSlSp()+"");
                        long gia=gioHangList.get(pos).getSlSp()*gioHangList.get(pos).getGiaSp();
                        holder.txttong_itemgiohang.setText(decimalFormat.format(gia)+" Đ");
                        EventBus.getDefault().postSticky(new TingTongBus());
                    }

                   else if(gioHangList.get(pos).getSlSp()==1){
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo xóa");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.gioHangList.remove(pos);
                                notifyDataSetChanged();
                                dialogInterface.dismiss();
                                EventBus.getDefault().postSticky(new TingTongBus());
                            }
                        });
                        builder.setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }

                }
                else if(value==2){
                    if(gioHangList.get(pos).getSlSp()<11){
                        int slmoi=gioHangList.get(pos).getSlSp()+1;
                        gioHangList.get(pos).setSlSp(slmoi);
                        // set lại tổng tiền
                        holder.txtsoLuong_itemgiohang.setText(gioHangList.get(pos).getSlSp()+"");
                        long gia=gioHangList.get(pos).getSlSp()*gioHangList.get(pos).getGiaSp();
                        holder.txttong_itemgiohang.setText(decimalFormat.format(gia)+" Đ");
                    }

                }

                EventBus.getDefault().postSticky(new TingTongBus());

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_itemgiohang,imgTru_itemgiohang,img_cong_itemgiohang;
        TextView txtTen_itemgiohang,txtgia_itemgiohang,txtsoLuong_itemgiohang,txttong_itemgiohang;
        ImageOnClickListener listener;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            //ánh xạ
            img_itemgiohang=itemView.findViewById(R.id.img_itemgiohang);
            imgTru_itemgiohang=itemView.findViewById(R.id._imgTru_itemgiohang);
            img_cong_itemgiohang=itemView.findViewById(R.id.img_cong_itemgiohang);
            txtTen_itemgiohang=itemView.findViewById(R.id.txtTen_itemgiohang);
            txtgia_itemgiohang=itemView.findViewById(R.id.txtgia_itemgiohang);
            txtsoLuong_itemgiohang=itemView.findViewById(R.id.txtsoLuong_itemgiohang);
            txttong_itemgiohang=itemView.findViewById(R.id.txttong_itemgiohang);
            // even bus
            imgTru_itemgiohang.setOnClickListener(this);
            img_cong_itemgiohang.setOnClickListener(this);
        }

        public void setListener(ImageOnClickListener listener) {
            this.listener = listener;
        }
        @Override
        public void onClick(View view) {
            if(view==imgTru_itemgiohang){
                listener.onImageClick(view ,getAdapterPosition(),1);
            }else if (view == img_cong_itemgiohang){
                listener.onImageClick(view ,getAdapterPosition(),2);
            }
        }
    }
}
