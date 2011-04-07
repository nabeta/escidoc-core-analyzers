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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Constants for analyzing.
 * 
 * @author MIH
 */
public class Constants {

    /**
     * Constants for analyzing.
     */
    public static final String PATH_TO_JAPANESE_ANALYZER_CONFIG = 
                            System.getProperty("catalina.home") 
                                + "/conf/indexing/sen.xml";

    
    /**
     * Constants for analyzing.
     */
    public static final HashSet<String> ANALYZABLE_FIELDS = new HashSet<String>() {
        {
            add("fulltext");
            add("metadata");
            add("title");
            add("description");
            add("alternative");
            add("abstract");
            add("subject");
            add("xml_metadata");
        }
    };

    public static final Collection<String> ANALYZABLE_MATCH_FIELDS = new ArrayList<String>() {
        {
            add("stored_fulltext");
        }

    };

    /**
     * Supported snowballTypes are:
     * Danish
     * Dutch
     * English
     * Finnish
     * French
     * German
     * Italian
     * Norwegian
     * Portuguese
     * Russian
     * Spanish
     * Swedish
     */
    public static final HashMap GERMAN_ANALYZING_PARAMETERS = new HashMap() {
        {
            put("snowballType", "German");
            put("stopwords", GermanStopwords.GERMAN_STOP_WORDS);
        }
    };

    public static final HashMap ENGLISH_ANALYZING_PARAMETERS = new HashMap() {
        {
            put("snowballType", "English");
            put("stopwords", EnglishStopwords.ENGLISH_STOP_WORDS);
        }
    };

    public static final HashMap ALL_ANALYZING_PARAMETERS = new HashMap() {
        {
            put("snowballType", null);
            put("stopwords", AllStopwords.ALL_STOP_WORDS);
        }
    };

    public static final HashMap<String, HashMap> SUPPORTED_ANALYZING_LANGUAGES = new HashMap<String, HashMap>() {
        {
            put("all", ALL_ANALYZING_PARAMETERS);
            put("de", GERMAN_ANALYZING_PARAMETERS);
            put("en", ENGLISH_ANALYZING_PARAMETERS);
        }
    };

    public static final String ORG_UNIT_URL = "/oum/organizational-unit/";

    public static final String ORG_UNIT_PATH_LIST_URL_SUFFIX = "/resources/path-list";

    public static final String SEARCH_PASSWORD = "password";

}
