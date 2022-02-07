package br.com.eskaryos.rankup.utils.api;

import org.bukkit.block.banner.PatternType;

import java.util.Locale;

public enum PatternTypes {

    bl("SQUARE_BOTTOM_LEFT"),
    br("SQUARE_BOTTEM_RIGHT"),
    tl("SQUARE_TOP_LEFT"),
    tr("SQUARE_TOP_RIGHT"),
    bs("STRIPE_BOTTOM"),
    ts("STRIPE_TOP"),
    ls("STRIPE_LEFT"),
    rs("STRIPE_RIGHT"),
    cs("STRIPE_CENTER"),
    ms("STRIPE_MIDDLE"),
    drs("STRIPE_DOWNRIGHT"),
    dls("STRIPE_DOWNLEFT"),
    ss("STRIPE_SMALL"),
    cr("CROSS"),
    sc("STRAIGHT_CROSS"),
    bt("TRIANGLE_BOTTOM"),
    tt("TRIANGLE_TOP"),
    bts("TRIANGLES_BOTTOM"),
    tts("TRIANGLES_TOP"),
    ld("DIAGONAL_LEFT"),
    rd("DIAGONAL_RIGHT"),
    lud("DIAGONAL_LEFT_MIRROR"),
    rud("DIAGONAL_RIGHT_MIRROR"),
    mc("CIRCLE_MIDDLE"),
    mr("RHOMBUS_MIDDLE"),
    vh("HALF_VERTICAL"),
    hh("HALF_HORIZONTAL"),
    vhr("HALF_VERTICAL_MIRROR"),
    hhb("HALF_HORIZONTAL_MIRROR"),
    bo("BORDER"),
    cbo("CURLY_BORDER"),
    cre("CREEPER"),
    gra("GRADIENT"),
    gru("GRADIENT_UP"),
    bri("BRICKS"),
    sku("SKULL"),
    flo("FLOWER"),
    moj("MOJANG");

    private final String name;

    PatternTypes(String name){
        this.name = name;
    }

    public PatternType getPattern(){
        return PatternType.valueOf(name);
    }
    public static PatternType getTypeByName(String type){
        PatternTypes[] types = PatternTypes.values();
        for(PatternTypes t : types){
            if(t.name.equals(type) ||t.name().equals(type)){
                return t.getPattern();
            }
        }
        return null;
    }
    public String getName(){
        return name;
    }
}
