package com.iris.ccpm.ui.project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class ProjectViewModel extends ViewModel {

    private MutableLiveData<List<Project>> myProjectList;
    private MutableLiveData<List<Project>> otherProjectList;

    public ProjectViewModel() {
        myProjectList = new MutableLiveData<>();
        otherProjectList = new MutableLiveData<>();

        update();
    }

    public void update() {
        Request.clientGet("project?asManager=yes&asMember=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Project> projectList = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                myProjectList.setValue(projectList);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("project?asManager=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Project> projects = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                otherProjectList.setValue(projects);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public MutableLiveData<List<Project>> getMyProjectList() {
        return myProjectList;
    }

    public MutableLiveData<List<Project>> getOtherProjectList() {
        return otherProjectList;
    }
}