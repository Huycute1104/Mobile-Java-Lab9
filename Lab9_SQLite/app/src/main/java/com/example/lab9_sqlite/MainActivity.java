package com.example.lab9_sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = findViewById(R.id.listViewCongViec);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayCongViec = new ArrayList<>();
        adapter = new CongViecAdapter(this, R.layout.activity_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        //Tao database GhiChu
        database = new Database(this, "GhiChu.sqlite", null, 1);

        //Tao table CongViec
        database.QueryData("Create table if not exists CongViec(id Integer Primary Key Autoincrement," +
                "TenCV nvarchar(200))");

//        //Insert data
//        database.QueryData("Insert into CongViec values(null, 'Project Android')");
//        database.QueryData("Insert into CongViec values(null, 'Design app')");

        GetDataCongViec();
    }
    private void GetDataCongViec(){
        //Select data
        Cursor dataCongViec = database.GetData("Select * from CongViec");
        //Xoá mảng trước khi add dể cập  lại dữ liệu mới tránh dư thừa trung lặp
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
//            Toast.makeText(this, ten, Toast.LENGTH_SHORT).show();
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    private void DialogThem(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        EditText edtTen = dialog.findViewById(R.id.editTextTenCV);
        Button btnThem = dialog.findViewById(R.id.buttonThem);
        Button btnHuy = dialog.findViewById(R.id.buttonHuy);

        //bat su kien cho button them
        btnThem.setOnClickListener(v -> {
            String tencv = edtTen.getText().toString();
            //Kiem tra chuoi rong --> khi nguoi dung khong nhap du lieu
            if(tencv.equals("")){
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc !", Toast.LENGTH_SHORT).show();
            } else {
                database.QueryData("Insert into CongViec values(null, '" + tencv + "')");
                Toast.makeText(MainActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                dialog.dismiss();// tắt hộp thoại sau khi đã thêm xong dữ liệu
                //show dữ liệu trên listView
                GetDataCongViec();
            }
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    //Ham cap nhat cong viec
    public void DialogSuaCongViec(String ten, int id){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sua_cong_viec);

        EditText edtTen = dialog.findViewById(R.id.editTextTenCV);
        Button btnXacNhan = dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuy = dialog.findViewById(R.id.buttonHuyEdit);

        edtTen.setText(ten);
        btnXacNhan.setOnClickListener(v -> {
            String tenMoi = edtTen.getText().toString().trim();
            database.QueryData("UPDATE CongViec SET TenCV = '"+ tenMoi +"' WHERE id = '"+ id +"'");
            Toast.makeText(MainActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            GetDataCongViec();
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    public void DialogXoaCongViec(String tencv, int Id){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xoá công việc "+ tencv +" không ? ");
        dialogXoa.setPositiveButton("Yes", (dialog, which) -> {
            database.QueryData("DELETE FROM CongViec WHERE Id = '"+ Id +"' ");
            Toast.makeText(MainActivity.this, "Đã Xoá"+ tencv, Toast.LENGTH_SHORT).show();
            GetDataCongViec();
        });
        dialogXoa.setNegativeButton("No", (dialog, which) -> {
        });
        dialogXoa.show();
    }
}