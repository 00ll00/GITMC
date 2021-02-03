package oolloo.gitmc.gitcli;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.gitcli.anns.Command;

import java.util.Locale;

public class CmdRef {

    private static final DynamicCommandExceptionType NULL_INSTANCE = new DynamicCommandExceptionType((name) -> {
        return new StringTextComponent("null instance of command: "+name);
    });
    private static final DynamicCommandExceptionType CANNOT_CREATE_COMMAND = new DynamicCommandExceptionType((name) -> {
        return new StringTextComponent("cannot create command: "+name);
    });
    private String name;
    private final String usage;
    private final boolean common;

    private final Class<? extends TextBuiltin> ref;
    private TextBuiltin instance;

    private ArgHandler argHandler;
    private OptHandler optHandler;

    public CmdRef(Class<? extends TextBuiltin> refIn) {
        this.ref = refIn;
        Command c = refIn.getAnnotation(Command.class);
        this.name = c.name();
        if (name.equals("")){
            name = refIn.getName().toLowerCase(Locale.ROOT);
        }
        this.usage = c.usage();
        this.common = c.common();
    }

    public void parse(StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) return;
        try {
            instance = ref.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw CANNOT_CREATE_COMMAND.create(name);
        }
        optHandler = new OptHandler(ref,instance);

        while (reader.canRead()) {
            String str = reader.readString();
            if (str.equals("--")) {
                break;
            }
            if (str.startsWith("--")) {
                final int eq = str.indexOf('=');
                if (eq > 0) {
                    String opt = str.substring(0, eq);
                    String value = str.substring(0, eq);
                    optHandler.put(opt,value);
                }
            }
        }
    }

    public String getName() {
        return name;
    }
    public String getUsage() {
        return usage;
    }
    public boolean isCommon() {
        return common;
    }

    public void run() throws Exception {
        if (instance==null){
            throw NULL_INSTANCE.create(name);
        }
        instance.run();
    }

}
