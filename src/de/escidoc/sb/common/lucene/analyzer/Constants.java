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
import java.util.HashMap;

import org.apache.lucene.util.Version;

import de.escidoc.core.common.util.configuration.EscidocConfiguration;

/**
 * Constants for analyzing.
 * 
 * @author MIH
 */
public class Constants {

    /**
     * Path to Japanese Analyzer Config.
     */
    public static final String getPathToJapaneseAnalyzerConfig() 
                                                throws RuntimeException {
        try {
            String searchPropertiesDirectory = EscidocConfiguration.getInstance()
            .get(EscidocConfiguration.SEARCH_PROPERTIES_DIRECTORY, "search/config");
            return System.getProperty("catalina.home")
                    + "/conf/"
                    + searchPropertiesDirectory
                    + "/sen.xml";
        } catch (IOException e) {
            throw new RuntimeException(
                    "couldnt get path to search-config directory:" + e);
        }

    }
    
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
    
    public static final Version LUCENE_VERSION = Version.LUCENE_33;

}
