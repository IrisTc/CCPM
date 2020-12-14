package com.iris.ccpm.ui.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.LoginActivity;
import com.iris.ccpm.MainActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.Members;
import com.iris.ccpm.adapter.MypagerAdapter;
//import com.iris.ccpm.adapter.Posts;
import com.iris.ccpm.adapter.ProjectMembersAdapter;
//import com.iris.ccpm.adapter.ProjectPostsAdapter;
import com.iris.ccpm.model.GlobalData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private MyViewModel myViewModel;
    TabLayout tbSelect;
    ViewPager vpChosen;
    SimpleDraweeView ivAvatar;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    TextView tvNickname;
    TextView tvEmail;
    GlobalData app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        findView(root);
        app = (GlobalData) getActivity().getApplication();

        tvNickname.setText(app.getName());
        tvEmail.setText(app.getUsername());
        Uri uri = Uri.parse(app.getAvatarUrl());
        ivAvatar.setImageURI(uri);

        initTabContent();

        tbSelect.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpChosen.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Button btLogout = root.findViewById(R.id.bt_logout);
        btLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("logout");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void findView(View root) {
        tvNickname = root.findViewById(R.id.tv_nickname);
        tvEmail = root.findViewById(R.id.tv_email);
        ivAvatar = root.findViewById(R.id.iv_avatar);
        vpChosen = (ViewPager) root.findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) root.findViewById(R.id.tb_detail);
    }

    private void initTabContent() {
        LayoutInflater li = getActivity().getLayoutInflater();
        View intro_view = li.inflate(R.layout.my_detail_intro, null, false);
        View posts_view = li.inflate(R.layout.project_detail_posts, null, false);
        View members_view = li.inflate(R.layout.project_detail_member, null, false);

        TextView tvUsername = intro_view.findViewById(R.id.tv_username);
        TextView tvNickname = intro_view.findViewById(R.id.tv_nickname);
        TextView tvRealname = intro_view.findViewById(R.id.tv_realname);
        TextView tvPhonenum = intro_view.findViewById(R.id.tv_phoneNum);
        TextView tvPosition = intro_view.findViewById(R.id.tv_position);
        TextView tvSynopsis = intro_view.findViewById(R.id.tv_synopsis);

        tvUsername.setText(app.getUsername());
        tvNickname.setText(app.getNickName());
        tvRealname.setText(app.getRealName());
        tvPhonenum.setText(app.getPhoneNum());
        tvPosition.setText(app.getPosition());
        tvSynopsis.setText(app.getSynopsis());

//        List<Posts> postList = new ArrayList<Posts>();
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
//        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
////        ProjectPostsAdapter adapter = new ProjectPostsAdapter(getActivity(),R.layout.list_item,postList);
//        ListView list = (ListView)posts_view.findViewById(R.id.post_list);
//        list.setAdapter(adapter);

        List<Members> memberList = new ArrayList<Members>();
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        ProjectMembersAdapter member_adapter = new ProjectMembersAdapter(getActivity(),R.layout.member_item,memberList);
        ListView member_list = (ListView)members_view.findViewById(R.id.member_list);
        member_list.setAdapter(member_adapter);

        viewList = new ArrayList<View>();
        viewList.add(intro_view);
        viewList.add(posts_view);
        viewList.add(members_view);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }
}