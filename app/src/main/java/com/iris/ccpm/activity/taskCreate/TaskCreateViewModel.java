package com.iris.ccpm.activity.taskCreate;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.model.Report;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import static com.iris.ccpm.model.GlobalData.getGlobalData;

public class TaskCreateViewModel extends ViewModel {
    MutableLiveData<List<Member>> memberList;
    String project_id;

    public TaskCreateViewModel() {
        memberList = new MutableLiveData<>();
        GlobalData app = (GlobalData) getGlobalData();
        project_id = app.getNow_project_id();
        Request.clientGet( "project/" + project_id + "/member", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Member> members = JSONObject.parseArray(liststring, Member.class);//把字符串转换成集合
                memberList.setValue(members);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public MutableLiveData<List<Member>> getMemberList() {
        return memberList;
    }
}
