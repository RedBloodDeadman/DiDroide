package com.mrdolphin.red.didroide;

import java.util.List;

/**
 * Created by mrdolphin on 02.10.16.
 */
public class Var {
    private static Var ourInstance = new Var();

    public static Var getInstance() {
        return ourInstance;
    }

    private Var() {
    }

    private int maxI = 0;
    private String ID;
    private String Ser;
    private List list;
    private String IP;

    public void setList (List s) { list = s;}

    public List getList () {return list;}

    public void setMaxImagesOnSeries (int m){
        maxI = m;
    }

    public int getMaxImagesOnSeries (){
        return maxI;
    }

    public void setip (String m){
        IP = m;
    }

    public String getip (){
        return IP;
    }

    public void setpID (String m){
        ID = m;
    }

    public String getpID (){
        return ID;
    }

    public void setnSer (String m){
        Ser = m;
    }

    public String getnSer (){
        return Ser;
    }
}
