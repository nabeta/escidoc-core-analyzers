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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ISOLatin1AccentFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;

/**
 * Analyzer for all escidoc lucene indices.
 * 
 * @author MIH
 * @sb
 */
public class EscidocAnalyzer extends Analyzer {
    private String language = null;

    private HashSet<String> analyzableFields =
        new HashSet<String>();

    private Collection<String> analyzableMatchFields = new ArrayList<String>();

    private HashMap<String, HashMap> supportedLanguages =
        new HashMap<String, HashMap>();

    private static Log log = LogFactory.getLog(EscidocAnalyzer.class);

    /**
     * initializes the analyzer.
     * 
     * @sb
     */
    public EscidocAnalyzer() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing EscidocAnalyzer");
        }
        // fill index-fields that have to get analyzed into HashMap
        analyzableFields = Constants.ANALYZABLE_FIELDS;
        analyzableMatchFields = Constants.ANALYZABLE_MATCH_FIELDS;

        // initialize Analyzers for languages
        supportedLanguages = Constants.SUPPORTED_ANALYZING_LANGUAGES;

    }

    /**
     * Constructs a token stream. -Whitespace tokenization -Language-dependant
     * stemming with SnowballFilter
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
            log.debug("tokenizing with EscidocAnalyzer");
        }

        // Tokenize with WhitespaceTokenizer
        TokenStream result = new XmlWhitespaceTokenizer(reader);

        // apply filters
        result = new JunkFilter(result);
        result = new LowerCaseFilter(result);
        result = new ISOLatin1AccentFilter(result);

        // extract field-name
        String reducedFieldName =
            fieldName.replaceFirst(".*\\.", "").toLowerCase();

        // Do further stop-word removal,
        // stemming + normalization for certain fields
        // this can only be done if language is set.
        boolean matchField = false;
        for (Iterator iter = analyzableMatchFields.iterator(); iter.hasNext();) {
            String field = (String) iter.next();
            if (reducedFieldName.matches(".*" + field + ".*")) {
                matchField = true;
            }
        }
        if (analyzableFields.contains(reducedFieldName) || matchField) {
            // remove stop words
            if (language == null || language.equals("")) {
                language = "all";
            }
            result =
                new StopFilter(result,
                    (String[]) (supportedLanguages.get(language))
                        .get("stopwords"));
            if (language != null 
                    && supportedLanguages.get(language) != null
                    && (String) (supportedLanguages.get(language))
                    .get("snowballType") != null) {
                // stem
                result =
                    new SnowballFilter(result,
                        (String) (supportedLanguages.get(language))
                            .get("snowballType"));
            }

        }
        return result;
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

}