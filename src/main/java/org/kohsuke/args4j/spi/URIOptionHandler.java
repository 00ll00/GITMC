package org.kohsuke.args4j.spi;

import oolloo.gitmc.adapter.ArgReader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * {@link URI} {@link OptionHandler}.
 *
 * @author Kohsuke Kawaguchi
 */
public class URIOptionHandler extends OptionHandler<URI> {
    public URIOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super URI> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(ArgReader params) throws CmdLineException {
        String param = params.readArg(0);
        try {
            setter.addValue(new URI(param));
            return 1;
        } catch (URISyntaxException e) {
            throw new CmdLineException(owner, Messages.ILLEGAL_OPERAND, params.readArg(-1), param);
        }
    }

    @Override
    public String getDefaultMetaVariable() {
        return Messages.DEFAULT_META_URI_OPTION_HANDLER.format();            
    }
}
