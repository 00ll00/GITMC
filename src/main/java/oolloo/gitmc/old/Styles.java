package oolloo.gitmc.old;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;

public final class Styles {
    public static final Style NORMAL = style(codeColor("#e3e3e3"),false,false,false,false,false,null,null,null,null);
    public static final Style INFO = style(codeColor("#fafa52"),false,false,false,false,false,null,null,null,null);
    public static final Style ERROR = style(codeColor("#fb5454"),false,false,false,false,false,null,null,null,null);
    public static final Style CLICKABLE = style(codeColor("#d794f9"),false,false,false,false,false,null,null,null,null);

    public static final Style URL = style(codeColor("#60f7ae"),false,true,false,false,false,null,null,null,null);
    public static final Style PATH = style(codeColor("#7fc8fb"),false,true,false,false,false,null,null,null,null);
    public static final Style FILE = style(codeColor("#8189f3"),false,false,false,false,false,null,null,null,null);
    public static final Style REPO = style(codeColor("#7df362"),true,false,false,false,false,null,null,null,null);
    public static final Style BRANCH = style(codeColor("#f3a661"),true,false,false,false,false,null,null,null,null);
    public static final Style ID = style(codeColor("#7ffbd4"),false,true,false,false,false,null,null,null,null);

    public static final Style STATUS_CHANGED = style(codeColor("#57a7a7"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_ADDED = style(codeColor("#56a656"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_REMOVED = style(codeColor("#a65656"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_MODIFIED = style(codeColor("#6efafa"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_UNTRACKED = style(codeColor("#6efa6e"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_MISSING = style(codeColor("#fb6e6e"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_UNDELETED = style(codeColor("#a6a656"),false,false,false,false,false,null,null,null,null);
    public static final Style STATUS_CONFLICTED = style(codeColor("#fafa6e"),false,false,false,false,false,null,null,null,null);

    public static Style style(@Nullable Color color,@Nullable Boolean bold,@Nullable Boolean italic,@Nullable Boolean underlined,@Nullable Boolean strikethrough,@Nullable Boolean obfuscated,@Nullable ClickEvent clickEvent,@Nullable HoverEvent hoverEvent,@Nullable String insertion,@Nullable ResourceLocation location){
        return Style.field_240709_b_.func_240718_a_(color)
                .func_240713_a_(bold)
                .func_240722_b_(italic)
                .setUnderlined(underlined)
                .setStrikethrough(strikethrough)
                .setObfuscated(obfuscated)
                .func_240715_a_(clickEvent)
                .func_240716_a_(hoverEvent)
                .func_240714_a_(insertion)
                .func_240719_a_(location);
    }
    public static Color codeColor(String code){
        return Color.func_240745_a_(code);
    }
}
