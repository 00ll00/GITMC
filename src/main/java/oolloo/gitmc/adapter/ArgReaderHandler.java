package oolloo.gitmc.adapter;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.eclipse.jgit.pgm.internal.CLIText;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class ArgReaderHandler extends OptionHandler<ArgReader> {

    public ArgReaderHandler(CmdLineParser parser, OptionDef option, Setter<? super ArgReader> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(ArgReader params) throws CmdLineException, CommandSyntaxException {
        owner.stopOptionParsing();
        setter.addValue(new ArgReader(params));
        return params.length - params.pos;
    }

    @Override
    public String getDefaultMetaVariable() {
        return CLIText.get().metaVar_arg;
    }
}
