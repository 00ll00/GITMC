package oolloo.gitmc.old;

import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

public class CmdResponse {
    private int value = 1;
    private final IFormattableTextComponent component = new StringTextComponent("");

    public CmdResponse() {}
    public CmdResponse(String text) {
        component.func_230529_a_(new StringTextComponent(text));
    }
    public CmdResponse(String text,Style style) {
        component.func_230529_a_(new StringTextComponent(text).func_230530_a_(style));
    }
    public CmdResponse(String text, int value) {
        component.func_230529_a_(new StringTextComponent(text));
        this.value = value;
    }
    public CmdResponse(Exception e) {
        component.func_230529_a_(new StringTextComponent("An error occurred: \n"+e.getMessage()).func_230530_a_(Styles.ERROR.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_,new StringTextComponent(e.getClass().getName())))));
        value = -1;
    }

    public CmdResponse append(String text) {
        component.func_230529_a_(new StringTextComponent(text));
        return this;
    }
    public CmdResponse append(int num) {
        component.func_230529_a_(new StringTextComponent(Integer.toString(num)));
        return this;
    }
    public CmdResponse append(String text, Style style) {
        component.func_230529_a_(new StringTextComponent(text).func_230530_a_(style));
        return this;
    }
    public CmdResponse append(int num, Style style) {
        component.func_230529_a_(new StringTextComponent(Integer.toString(num)).func_230530_a_(style));
        return this;
    }

    public CmdResponse setValue(int value) {
        this.value=value;
        return this;
    }
    public CmdResponse setStyle(Style style){
        component.func_230530_a_(style);
        return this;
    }
    public int getValue() {
        return this.value;
    }
    public boolean isSucceed() {
        return this.value > 0;
    }
    public boolean isFinished() {
        return this.value >= 0;
    }
    public ITextComponent getComponent(){
        return component;
    }
}
