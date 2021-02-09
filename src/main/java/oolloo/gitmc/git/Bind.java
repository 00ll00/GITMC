package oolloo.gitmc.git;

import org.eclipse.jgit.pgm.Command;
import org.eclipse.jgit.pgm.GitHandler;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.kohsuke.args4j.Argument;

@Command(common = true, usage = "usage_SetDefaultRepo")
class Bind extends TextBuiltin {
    @Argument(index = 0, metaVar = "metaVar_gitDir", usage = "usage_SetDefaultRepo")
    private String gitdir;

    /** {@inheritDoc} */
    @Override
    protected void run() {
        GitHandler.setDefaultGitDir(gitdir);
    }
}
