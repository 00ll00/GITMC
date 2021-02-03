package oolloo.gitmc.gitcli;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import org.eclipse.jgit.lib.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class TextBuiltin {

    protected boolean hasSub = false;
    protected TextBuiltin subRunner;
    protected Repository db;
    protected CommandSource source;
    protected HashMap<String, Field> options;
    protected HashMap<String, Field> arguments;


    public static DynamicCommandExceptionType UNDEFINED_OPTION = new DynamicCommandExceptionType((option) -> {
       return new StringTextComponent("Undefined option: "+option);
    });
    public static DynamicCommandExceptionType UNDEFINED_ARGUMENT = new DynamicCommandExceptionType((arg) -> {
        return new StringTextComponent("Undefined argument: "+arg);
    });
    public static DynamicCommandExceptionType GIT_API_EXCEPTION = new DynamicCommandExceptionType((msg) -> {
        return new StringTextComponent("An error occured: "+msg);
    });

    public void setSubRunner(TextBuiltin sub) {
        subRunner = sub;
        hasSub = true;
        subRunner.db = db;
        subRunner.source = source;
    }

    public void setOption(String option) throws CommandSyntaxException {
        if (hasSub && subRunner!=null) {
            subRunner.setOption(option);
        } else {
            this.setOptionThis(option);
        }
    }

    public void setOptionThis(String option) throws CommandSyntaxException {
        throw UNDEFINED_OPTION.create(option);
    }

    public void setArgument(String name, String arg) throws CommandSyntaxException {
        if (hasSub && subRunner!=null) {
            subRunner.setArgument(name, arg);
        } else {
            this.setArgumentThis(name, arg);
        }
    }

    public void setArgumentThis(String name, String arg) throws CommandSyntaxException {
        throw UNDEFINED_ARGUMENT.create(name);
    }

    protected void run() throws Exception {
        if (hasSub && subRunner!=null) {
            subRunner.run();
        } else {
            this.runThis();
        }
    }

    protected void runThis() throws Exception {}

    public abstract boolean hasArg();
}
