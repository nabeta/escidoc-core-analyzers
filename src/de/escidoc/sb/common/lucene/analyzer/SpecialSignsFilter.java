/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or http://www.escidoc.de/license.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright 2006-2007 Fachinformationszentrum Karlsruhe Gesellschaft
 * für wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Förderung der Wissenschaft e.V.  
 * All rights reserved.  Use is subject to license terms.
 */

package de.escidoc.sb.common.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * This class converts special letters to the specified equivalents.
 * 
 * 
 * Covers glyphs: 00B1 plus-minus --> 002B and 002D +- plus minus 00B7 middle
 * dot --> 005F _ underscore 2032 prime --> 0027 ' apostrophe 2033 double prime
 * --> 0027 and 0027 '' double apostrophe 2192 rightwards arrow --> - and - and >
 * 2260 not equal to --> !=
 * 
 */

public class SpecialSignsFilter extends TokenFilter {
    private CharTermAttribute termAtt;
    private StringBuffer sb = new StringBuffer();

    private String s = "";

    /**
     * Constructor.
     * 
     * @param in
     *            TokenStream
     * 
     */
    public SpecialSignsFilter(final TokenStream in) {
        super(in);
        termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
    }

    /**
     * Replaces next Term without special letters.
     * 
     * @return boolean true|false
     * @throws IOException e
     * 
     */
    public final boolean incrementToken()
            throws IOException {
        if (input.incrementToken()) {
            s = termAtt.toString();
            sb.delete(0, sb.length());
            sb.insert(0, s);
            s = substitute(sb);
            termAtt.setEmpty().append(s);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns substituted String.
     * 
     * @param buffer
     *            StringBuffer buffer
     * @return String substituted String
     * 
     */
    private String substitute(final StringBuffer buffer) {
        StringBuffer substituteString = buffer;
        for (int c = 0; c < substituteString.length(); c++) {
            if (substituteString.charAt(c) == '\u00B1') {
                substituteString.setCharAt(c, '+');
                substituteString.insert(c + 1, '-');
            }

            else if (substituteString.charAt(c) == '\u00B7') {
                substituteString.setCharAt(c, '_');
            }

            else if (substituteString.charAt(c) == '\u2032') {
                substituteString.setCharAt(c, '\'');
            }

            else if (substituteString.charAt(c) == '\u2033') {
                substituteString.setCharAt(c, '\'');
                substituteString.insert(c + 1, '\'');
            }

            else if (substituteString.charAt(c) == '\u2192') {
                substituteString.setCharAt(c, '-');
                substituteString.insert(c + 1, '-');
                substituteString.insert(c + 2, '>');
            }

            else if (substituteString.charAt(c) == '\u2260') {
                substituteString.setCharAt(c, '!');
                substituteString.insert(c + 1, '=');
            }

        }
        return substituteString.toString();
    }
}
