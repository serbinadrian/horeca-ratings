package com.nikolaev.horeca.datasets;


import java.util.HashMap;
import java.util.Map;

public class UserAvatarColorsDataset {
    public int key = 0;
    public int size = 0;
    public Map<Integer, String> userColors = new HashMap<>();
    public Map<Integer, String> userSecondaryColors = new HashMap<>();

    public UserAvatarColorsDataset(){
        addData("#B9B95E", "#FFFFBE");
        addData("#968EE8", "#D1CDFF");
        addData("#4165E4", "#D6D4FF");
        addData("#6843A6", "#BF9AFF");
        addData("#556FC1", "#BECEFF");
        addData("#BD91A7", "#FFBEDF");
        addData("#3895C1", "#A6E3FF");
        addData("#A18F11", "#FFF298");
        addData("#BC6262", "#FFA8A8");
        addData("#E88181", "#FFA8A8");
        addData("#2F6859", "#51BDA1");
        addData("#DDDD70", "#9D9D84");
        addData("#C7A044", "#FACD60");
        addData("#BE7A71", "#FFD9D4");
        addData("#678FBC", "#BEDDFF");
        addData("#AF472D", "#FF967C");
        addData("#6843A6", "#9870DC");
        addData("#157376", "#1AC0C6");
        addData("#A18F11", "#EEDE70");
        addData("#678FBC", "#FFFFFF");
        addAdminData();
    }

    private void addData(String userColor, String userSecondaryColor){
        userColors.put(key, userColor);
        userSecondaryColors.put(key, userSecondaryColor);
        this.key++;
        this.size++;
    }
    private void addAdminData(){
        userColors.put(-1, "#353535");
        userSecondaryColors.put(-1, "#FACD60");
    }
}
