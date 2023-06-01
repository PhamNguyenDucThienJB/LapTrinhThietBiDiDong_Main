package com.example.anhki.foodapp.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anhki.foodapp.CustomAdapter.AdapterHienThiDanhSachMonAn;
import com.example.anhki.foodapp.entity.MonAnDAO;
import com.example.anhki.foodapp.Detail.MonAnDTO;
import com.example.anhki.foodapp.R;
import com.example.anhki.foodapp.SoLuongActivity;

import java.util.ArrayList;
import java.util.List;

public class HienThiDanhSachMonAnFragment extends Fragment{
    GridView gridView;
    ListView listView,listView2;
    Button btn;
    MonAnDAO monAnDAO;
    List<MonAnDTO> dataList =  new ArrayList<>();
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAn adapterHienThiDanhSachMonAn;
    int maban;
    int maquyen;
    int idRemove;
    AdapterHienThiDanhSachMonAn foodAdapter;


    @Nullable
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                // Xóa món ăn được chọn
                // Thêm code xử lý xóa món ăn ở đây
                int id = monAnDTOList.get(info.position).getId();    // Lấy ID của món ăn được chọn từ danh sách dataList bằng cách sử dụng vị trí của item được chọn trên AdapterView
                foodAdapter.remove(MonAnDAO.findFoodById(id)); // Sử dụng ID của món ăn để tìm món ăn trong danh sách MONANDAO và xóa món ăn khỏi danh sách
                MonAnDAO.remove_food(id);                     // Xóa món ăn khỏi danh sách MonAnDAO
                foodAdapter.notifyDataSetChanged();// bước 5 Xóa món ăn(Cập nhật lại giao diện người dùng)
//                reloadFragment(); // Tải lại trang
                return true;
            case R.id.details:
//                 Xem chi tiết món ăn được chọn
//                 Thêm code xử lý xem chi tiết món ăn ở đây
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);

        gridView = (GridView) view.findViewById(R.id.gvHienThiThucDon);
//        Đăng ký ConNext Menu
        registerForContextMenu(gridView);
        monAnDAO = new MonAnDAO(getActivity());
        Bundle bundle = getArguments();
        if(bundle != null){
            final int maloai = bundle.getInt("maloai");
            maban = bundle.getInt("maban");

            monAnDTOList = monAnDAO.LayDanhSachMonAnTheoLoai(maloai);

            adapterHienThiDanhSachMonAn = new AdapterHienThiDanhSachMonAn(getActivity(), R.layout.custom_layout_hienthidanhsachmonan, monAnDTOList);
            gridView.setAdapter(adapterHienThiDanhSachMonAn);
            adapterHienThiDanhSachMonAn.notifyDataSetChanged();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (maban != 0){
                        Intent iSoLuong = new Intent(getActivity(), SoLuongActivity.class);
                        iSoLuong.putExtra("maban", maban);
                        iSoLuong.putExtra("mamonan", monAnDTOList.get(position).getMaMonAn());

                        startActivity(iSoLuong);
                    }
                }
            });
        }

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    getFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        return view;
    }
    //        boolean kiemtra = nhanVienDAO.XoaNhanVien(manhavien);
//        if (kiemtra){
//            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
//            HienThiDanhSachNhanVien();
//        }else {
//            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
//        }
//        break;
//    }
//        return true;
    }
//    private void reloadFragment() {
//        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
//    }


