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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * @author rha
 * 
 * JunkFilter filters out (parts of) strings that contain the specified
 * substrings.
 * 
 */
public class JunkFilter extends TokenFilter {
    private String s = "";
    
    private static final int THREE = 3;

    private static final int FOUR = 4;

    private static final int FIVE = 5;
    
    //remove ",',<,>,=,/,(,) at beginning of String
    private static Pattern startRemovals = Pattern.compile("[\"'<>=\\/\\(\\)]+(.*)");

    //remove ",',<,>,=,/,:,(,) at end of String
    private static Pattern endRemovals = Pattern.compile("(.*?)[\"'<>=\\/\\(\\):]+");

    //remove , ʻ ʼ ʽ ˆ ˇ ˊ ˋ ˌ ˎ ˏ ˚ ˛ ˜ ” “ „   
    private static Pattern specialSignsJunk = Pattern.compile(
            "[\u0098\u00a8\u00b0\u00b4\u00b8\u00ba,\u02bb\u02bc"
            + "\u02bd\u02c6\u02c7\u02ca\u02cb\u02cc\u02ce\u02cf"
            + "\u02da\u02db\u02dc\u201d\u201c\u201e]");

    /**
     * Constructor.
     * 
     * @param in
     *            TokenStream
     * 
     */
    public JunkFilter(final TokenStream in) {
        super(in);
    }

    /**
     * Returns next Token without junk-signs.
     * 
     * @return Token token
     * @throws IOException
     *             e
     * 
     */
    @Override
    public final Token next() throws IOException {
        Token t = input.next();
        if (t == null) {
            return null;
        }
        else {
            s = t.termText();
            s = substituteJunk(s);

            // If not stemmed, don't waste the time creating a new token
            if (!s.equals(t.termText())) {
                return new Token(s, t.startOffset(), t.endOffset(), t.type());
            }
            return t;
        }
    }

    /**
     * Returns substituted String.
     * 
     * @param string
     *            String string
     * @return String substituted String
     * 
     */
    private String substituteJunk(final String string) {
        String replacedString  = string;
        if (replacedString == null) {
            return "";
        }
        if (replacedString.equals("lt;") || replacedString.equals("gt;")
                || replacedString.equals("amp;") 
                || replacedString.equals("quot;") || replacedString
                .equals("apos;")) {
            replacedString = "";
        }
        if (replacedString.startsWith("[nl]")) {
            if (replacedString.length() > FOUR) {
                replacedString = replacedString.substring(FOUR);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.endsWith("[nl]")) {
            if (replacedString.length() > FOUR) {
                replacedString = 
                    replacedString.substring(0, replacedString.length() - FOUR);
            }
            else {
                replacedString = "";
            }
        }
        Matcher startMatcher = startRemovals.matcher(replacedString);
        if (startMatcher.matches()) {
            replacedString = startMatcher.group(1);
        }

        Matcher endMatcher = endRemovals.matcher(replacedString);
        if (endMatcher.matches()) {
            replacedString = endMatcher.group(1);
        }

        if (replacedString.startsWith("quot;") 
            || replacedString.startsWith("apos;")) {
            if (replacedString.length() > FIVE) {
                replacedString = replacedString.substring(FIVE);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.endsWith("quot;") || replacedString.endsWith("apos;")) {
            if (replacedString.length() > FIVE) {
                replacedString = 
                    replacedString.substring(0, replacedString.length() - FIVE);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.startsWith("amp;")) {
            if (replacedString.length() > FOUR) {
                replacedString = replacedString.substring(FOUR);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.endsWith("amp;")) {
            if (replacedString.length() > FOUR) {
                replacedString = 
                    replacedString.substring(0, replacedString.length() - FOUR);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.startsWith("gt;") || replacedString.startsWith("lt;")) {
            if (replacedString.length() > THREE) {
                replacedString = replacedString.substring(THREE);
            }
            else {
                replacedString = "";
            }
        }
        if (replacedString.endsWith("gt;") || replacedString.endsWith("lt;")) {
            if (replacedString.length() > THREE) {
                replacedString = 
                    replacedString.substring(0, replacedString.length() - THREE);
            }
            else {
                replacedString = "";
            }
        }
        
        Matcher specialSignsJunkMatcher = specialSignsJunk.matcher(replacedString);
        if (specialSignsJunkMatcher.find()) {
            replacedString = specialSignsJunkMatcher.replaceAll("");
        }

        return replacedString;
    }
}
