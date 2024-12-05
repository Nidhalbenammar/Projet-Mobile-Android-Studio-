package com.example.tp7.Teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.List;

public class homeFragment extends Fragment {
    private TeacherAdapter ta;
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerview);
        rv = view.findViewById(R.id.mRecyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Teacher> teachers = dbHelper.getTeacherList();

        ta=new TeacherAdapter(teachers,dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ta);
        TextView tx = view.findViewById(R.id.header_list);
        tx.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setOnMenuItemClickListener(onPopupMenuClickListener());
                        popupMenu.inflate(R.menu.menu_pop_up);
                        popupMenu.show();
                    }
                }
        );

        return view;

    }
    public TeacherAdapter getAdapter(){return ta;}
    private PopupMenu.OnMenuItemClickListener onPopupMenuClickListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.show_hide_recycler_view) {
                    if (rv.getVisibility() == View.VISIBLE) {
                        rv.setVisibility(View.GONE);
                    } else {
                        rv.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                else if (item.getItemId() == R.id.change_background) {
                    TextView tx = getActivity().findViewById(R.id.header_list);
                    tx.setBackgroundColor(Color.WHITE);
                    tx.setTextColor(Color.BLACK);
                    return true;
                }
                return false;
            }
        };
    }
}
