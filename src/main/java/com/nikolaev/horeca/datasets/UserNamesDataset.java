package com.nikolaev.horeca.datasets;

import java.util.HashMap;
import java.util.Map;

public class UserNamesDataset{
    public Map<String, String> usernames = new HashMap<>();
    public int size = 0;
    public UserNamesDataset(){
        addData("anAndreev","Андрей Андреев");
        addData("seSergeev","Сергей Сергеев");
        addData("kiKirillov","Кирилл Кириллов");
        addData("ivIvanov", "Иван Иванов");
        addData("pePetrov","Петр Петров");
        addData("dmDmitrov","Дмитрий Дмитров");
        addData("alAlekseev","Алексей Алексеев");
        addData("evEvgeniev","Евгений Евгеньев");
        addData("miMikhailov","Михаил Михайлов");
        addData("anAntonov","Антон Антонов");
        addData("ekAndreeva","Екатерина Андреева");
        addData("alSergeeva","Алина Сергеева");
        addData("maKirillova","Мария Кириллова");
        addData("maIvanova","Марина Иванова");
        addData("daPetrova","Дарья Петрова");
        addData("anDmitrova","Анна Дмитрова");
        addData("vaAlekseeva","Валерия Алексеева");
        addData("kaEvgenieva","Карина Евгеньева");
        addData("elMikhailova","Элеонора Михайлова");
        addData("yuAntonova","Юлия Антонова");
    }

    private void addData(String username, String userfullname){
        usernames.put(username, userfullname);
        this.size++;
    }
}
