package br.com.eskaryos.rankup.ranks;


public class Rank {

    private String name;
    private String display;
    private int order;


    public Rank(String name,String display,int order){
        this.name = name;
        this.display = display;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
