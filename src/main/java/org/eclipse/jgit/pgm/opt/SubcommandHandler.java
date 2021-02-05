/*
 * Copyright (C) 2008, Shawn O. Pearce <spearce@spearce.org> and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jgit.pgm.opt;

import oolloo.gitmc.adapter.ArgReader;
import oolloo.gitmc.adapter.SubCmdSugException;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.internal.CLIText;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

/**
 * Custom Argument handler for jgit command selection.
 * <p>
 * Translates a single argument string to a
 * {@link TextBuiltin} instance which we can execute at
 * runtime with the remaining arguments of the parser.
 */
public class SubcommandHandler extends OptionHandler<TextBuiltin> {
	private final org.eclipse.jgit.pgm.opt.CmdLineParser clp;

	/**
	 * Create a new handler for the command name.
	 * <p>
	 * This constructor is used only by args4j.
	 *
	 * @param parser
	 *            a {@link CmdLineParser} object.
	 * @param option
	 *            a {@link OptionDef} object.
	 * @param setter
	 *            a {@link Setter} object.
	 */
	public SubcommandHandler(final CmdLineParser parser,
			final OptionDef option, final Setter<? super TextBuiltin> setter) {
		super(parser, option, setter);
		clp = (org.eclipse.jgit.pgm.opt.CmdLineParser) parser;
	}

	/** {@inheritDoc} */
	@Override
	public int parseArguments(ArgReader params) throws CmdLineException {
		final String name = params.readArg(0);
		final CommandRef cr = CommandCatalog.get(name);
		if (cr == null)
			throw new SubCmdSugException(clp,
					CLIText.format(CLIText.get().notAJgitCommand), name);

		// Force option parsing to stop. Everything after us should
		// be arguments known only to this command and must not be
		// recognized by the current parser.
		//
		owner.stopOptionParsing();
		setter.addValue(cr.create());
		return 1;
	}

	/** {@inheritDoc} */
	@Override
	public String getDefaultMetaVariable() {
		return CLIText.get().metaVar_command;
	}
}
