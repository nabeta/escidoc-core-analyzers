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

import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 * Analyzer for all escidoc lucene indices, japanese language.
 * 
 * @author MIH
 * @sb
 */
public class EscidocJapaneseAnalyzer extends Analyzer {
    private String language = null;

    private static Log log = LogFactory.getLog(EscidocJapaneseAnalyzer.class);

    /**
     * initializes the analyzer.
     * 
     * @sb
     */
    public EscidocJapaneseAnalyzer() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing EscidocJapaneseAnalyzer");
        }
    }

    /**
     * Constructs a token stream with JapaneseAnalyzer or WhitespaceTokenizer
     * depending if text is japanese or not.
     * 
     * @param fieldName
     *            name of the Lucene Indexfield.
     * @param reader
     *            reader with field-value
     * 
     * @return TokenStream tokenStream
     * 
     * @sb
     */
    @Override
    public TokenStream tokenStream(final String fieldName, final Reader reader) {
        if (log.isDebugEnabled()) {
            log.debug("tokenizing with EscidocJapaneseAnalyzer");
        }
        //checkJapanese ///////////////////////////////////////////////////////
        boolean isJapanese = false;
        TokenStream whitespaceTokens = new WhitespaceTokenizer(Constants.LUCENE_VERSION, reader);
        Reader reader1 = null;
        try {
            StringBuffer tokenBuffer = new StringBuffer("");
            CharTermAttribute termAtt = whitespaceTokens.addAttribute(CharTermAttribute.class);
            whitespaceTokens.reset();
            while (whitespaceTokens.incrementToken()) {
                if (tokenBuffer.length() > 0) {
                    tokenBuffer.append(" ");
                }
                tokenBuffer.append(termAtt.toString());
            }
            for (int i = 0; i < tokenBuffer.length(); i++) {
                int hexInt = Integer.parseInt(
                        charToHex(tokenBuffer.charAt(i)), 16);
                if (hexInt > 12287 && hexInt < 13328) {
                    isJapanese = true;
                    break;
                }
            }
            reader1 = new StringReader(
                    tokenBuffer.toString());
        } catch (Exception e) {
            log.error(e);
        }
        ///////////////////////////////////////////////////////////////////////
        
        //No Japanese, so return whitespace-tokens
        if (!isJapanese) {
            TokenStream result = new XmlWhitespaceTokenizer(reader1);
            result = new JunkFilter(result);
            result = new LowerCaseFilter(Constants.LUCENE_VERSION, result);
            return result;
        }
        
        //Get Japanese Tokens
        JapaneseAnalyzer analyzer = 
            new JapaneseAnalyzer(Constants.LUCENE_VERSION);
        TokenStream japaneseTokens = analyzer.tokenStream("", reader1);
    	if (analyzer != null) {
    		try {
    			analyzer.close();
    		}
    		catch (Exception e) {}
    	}
        return japaneseTokens;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            The language to set.
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Converts a byte to a HEX-String.
     * 
     * @param b byte.
     * 
     * @return String HEX-String
     * 
     * @sb
     */
    private String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char hexDigit[] = {
           '0', '1', '2', '3', '4', '5', '6', '7',
           '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
     }

    /**
     * Converts a char to a HEX-String.
     * 
     * @param c char.
     * 
     * @return String HEX-String
     * 
     * @sb
     */
     private String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
     }

}
