/*
 * Copyright (C) 2010, Chris Aniszczyk <caniszczyk@gmail.com>
 * Copyright (C) 2008, Marek Zawirski <marek.zawirski@gmail.com> and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jgit.pgm;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.pgm.internal.CLIText;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.transport.RemoteRefUpdate.Status;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.valueOf;

@Command(common = true, usage = "usage_UpdateLocalRepositoryFromRemoteRefs")
class Pull extends TextBuiltin {
	@Option(name = "--timeout", metaVar = "metaVar_seconds", usage = "usage_abortConnectionIfNoActivity")
	int timeout = -1;

	@Argument(index = 0, metaVar = "metaVar_uriish")
	private String remote = Constants.DEFAULT_REMOTE_NAME;

//	@Argument(index = 1, metaVar = "metaVar_refspec")
//	private List<RefSpec> refSpecs = new ArrayList<>();

//	@Option(name = "--all")
//	private boolean all;

//	@Option(name = "--atomic")
//	private boolean atomic;

//	@Option(name = "--tags")
//	private boolean tags;

//	@Option(name = "--verbose", aliases = { "-v" })
//	private boolean verbose = false;

//	@Option(name = "--thin")
//	private boolean thin = Transport.DEFAULT_PUSH_THIN;

//	@Option(name = "--no-thin")
//	void nothin(@SuppressWarnings("unused") final boolean ignored) {
//		thin = false;
//	}

//	@Option(name = "--force", aliases = { "-f" })
//	private boolean force;

//	@Option(name = "--receive-pack", metaVar = "metaVar_path")
//	private String receivePack;

//	@Option(name = "--dry-run")
//	private boolean dryRun;

//	@Option(name = "--push-option", aliases = { "-t" })
//	private List<String> pushOptions = new ArrayList<>();

	private boolean shownURI;

	/** {@inheritDoc} */
	@Override
	protected void run() {
		try (Git git = new Git(db)) {
			PullCommand pull = git.pull();
//			pull.setDryRun(dryRun);
//			pull.setForce(force);
			pull.setProgressMonitor(new TextProgressMonitor(errw));
//			pull.setReceivePack(receivePack);
//			pull.setRefSpecs(refSpecs);
//			if (all) {
//				pull.setPushAll();
//			}
//			if (tags) {
//				pull.setPushTags();
//			}
			pull.setRemote(remote);
//			pull.setThin(thin);
//			pull.setAtomic(atomic);
			pull.setTimeout(timeout);
//			if (!pushOptions.isEmpty()) {
//				pull.setPushOptions(pushOptions);
//			}
			PullResult result = pull.call();
			try (ObjectReader reader = db.newObjectReader()) {
				outw.println(result.toString());
			}

		} catch (GitAPIException e) {
			throw die(e.getMessage(), e);
		}
	}
}
