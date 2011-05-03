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
 * Copyright 2009 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */

package de.escidoc.sb.common.lucene.similarity;

import org.apache.lucene.search.DefaultSimilarity;

/**
 * Class calculates lucene-scoring without coord-factor. This is done because
 * srw-server adds lots of parentheses to the query when translating from cql.
 * Parentheses accect the coord-factor.
 * 
 * @author MIH
 * @sb
 */
public class SrwSimilarity extends DefaultSimilarity {

    private static final long serialVersionUID = 1L;

    public float coord(int overlap, int maxOverlap) {
        return 1.0f;
    }

}
