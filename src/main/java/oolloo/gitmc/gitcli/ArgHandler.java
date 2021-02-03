package oolloo.gitmc.gitcli;

import oolloo.gitmc.gitcli.anns.Argument;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ArgHandler {
    private final Class<? extends TextBuiltin> ref;
    private final HashMap<Argument,Field> args = new HashMap<>();

    public ArgHandler(Class<? extends TextBuiltin> refIn) {
        this.ref = refIn;
        Field[] fs = refIn.getFields();
        for (Field f : fs) {
            Argument a = f.getAnnotation(Argument.class);
            if (a!=null){
                args.put(a,f);
            }
        }
    }

    public void isArg(String arg) {
    }
}
