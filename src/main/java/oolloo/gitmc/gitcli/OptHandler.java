package oolloo.gitmc.gitcli;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.gitcli.anns.Option;
import oolloo.gitmc.gitcli.opt.OptSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class OptHandler {
    private static final DynamicCommandExceptionType UNKNOWN_OPTION = new DynamicCommandExceptionType((name) -> {
        return new StringTextComponent("unknown option: "+name);
    });

    private final Class<? extends TextBuiltin> ref;
    private final TextBuiltin instance;
    private final HashMap<Option, OptSetter> opts = new HashMap<>();

    public OptHandler(Class<? extends TextBuiltin> refIn,TextBuiltin instanceIn) {
        this.ref = refIn;
        this.instance = instanceIn;
        Field[] fs = refIn.getFields();
        for (Field f : fs) {
            Option o = f.getAnnotation(Option.class);
            if (o!=null){
                opts.put(o,f);
            }
        }
        Method[] ms = refIn.getMethods();
        for (Method m : ms) {
            Option o = m.getAnnotation(Option.class);
            if (o!=null){
                opts.put(o,m);
            }
        }

    }

    public void put(String opt,String value) throws CommandSyntaxException {

    }

}
