package com.example.weatherandroid.nowgson;

/**
 * Describe: LifeIndex
 * <p>
 * Created by Ervin Liu on 2021/04/16---11:56
 **/
public class LifeIndex {
    private Uv uv;

    private Xt xt;

    private Ct ct;

    private Xc xc;

    public void setUv(Uv uv){
        this.uv = uv;
    }
    public Uv getUv(){
        return this.uv;
    }
    public void setXt(Xt xt){
        this.xt = xt;
    }
    public Xt getXt(){
        return this.xt;
    }
    public void setCt(Ct ct){
        this.ct = ct;
    }
    public Ct getCt(){
        return this.ct;
    }
    public void setXc(Xc xc){
        this.xc = xc;
    }
    public Xc getXc(){
        return this.xc;
    }
}
