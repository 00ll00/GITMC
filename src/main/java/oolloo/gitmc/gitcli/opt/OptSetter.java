package oolloo.gitmc.gitcli.opt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class OptSetter {
    private Field refF;
    private Method refM;
    private Class type;
    private boolean f;

    public void setRef(Field refIn) {
        refF = refIn;
        f = true;
        type = refF.getType();
    }

    public void setRef(Method refIn) {
        refM = refIn;
        f = false;
    }

}
