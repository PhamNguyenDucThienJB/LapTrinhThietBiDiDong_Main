package com.example.anhki.foodapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.anhki.foodapp.CustomAdapter.AdapterHienThiLoaiMonAnThucDon;
import com.example.anhki.foodapp.entity.LoaiMonAnDAO;
import com.example.anhki.foodapp.Detail.LoaiMonAnDTO;
import com.example.anhki.foodapp.R;
import com.example.anhki.foodapp.ThemThucDonActivity;
import com.example.anhki.foodapp.TrangChuActicity;

import java.util.List;

public class HienThiThucDonFragment extends Fragment{
    private GridView gridView;
    ListView listView,listView2;
    private List<LoaiMonAnDTO> loaiMonAnDTOs;
    private LoaiMonAnDAO loaiMonAnDAO;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    private int maban;
    private int maquyen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);
        setHasOptionsMenu(true);
        ((TrangChuActicity) getActivity()).getSupportActionBar().setTitle(R.string.thucdon); //khi gọi getActivity thì hệ thống không hiểu là của activity nào
        //mà trong TrangChuActivity chứa tất cả Fragment nên ta ép kiểu cho nó về TrangChuActivity
        gridView = view.findViewById(R.id.gvHienThiThucDon);

        fragmentManager = getActivity().getSupportFragmentManager();
//        listView = view.findViewById(R.id.gvHienThiThucDon);
//        registerForContextMenu(listView);
        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        AdapterHienThiLoaiMonAnThucDon adapter = new AdapterHienThiLoaiMonAnThucDon(getActivity(), R.layout.custom_layout_hienloaimonan, loaiMonAnDTOs);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Bundle bDuLieuThucDon = getArguments();
        if (bDuLieuThucDon != null)
            maban = bDuLieuThucDon.getInt("maban");

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            int maloai = loaiMonAnDTOs.get(position).getMaLoai();

            HienThiDanhSachMonAnFragment hienThiDanhSachMonAnFragment = new HienThiDanhSachMonAnFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("maloai", maloai);
            bundle.putInt("maban", maban);
            hienThiDanhSachMonAnFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, hienThiDanhSachMonAnFragment).addToBackStack("hienthiloai");

            transaction.commit();
        });

        return view;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (maquyen == 0){
            MenuItem itThemThucDon = menu.add(1, R.id.itThemThucDon, 1, R.string.themthucdon);
            itThemThucDon.setIcon(R.drawable.addicon);
            itThemThucDon.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itThemThucDon) {
            Intent iThemThucDon = new Intent(getActivity(), ThemThucDonActivity.class);
            startActivity(iThemThucDon);
            getActivity().overridePendingTransition(R.anim.hieuung_activity_vao, R.anim.hieuung_activity_ra);
        }
        return super.onOptionsItemSelected(item);
    }
}
