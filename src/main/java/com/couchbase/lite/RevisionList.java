/**
 * Original iOS version by  Jens Alfke
 * Ported to Android by Marty Schoch
 *
 * Copyright (c) 2012 Couchbase, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.couchbase.lite;

import com.couchbase.lite.internal.RevisionInternal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * An ordered list of TDRevisions
 * @exclude
 */
@SuppressWarnings("serial")
public class RevisionList extends ArrayList<RevisionInternal> {

    public RevisionList() {
        super();
    }

    /**
     * Allow converting to RevisionList from List<RevisionInternal>
     * @param list
     */
    public RevisionList(List<RevisionInternal> list) {
        super(list);
    }

    public RevisionInternal revWithDocIdAndRevId(String docId, String revId) {
        Iterator<RevisionInternal> iterator = iterator();
        while(iterator.hasNext()) {
            RevisionInternal rev = iterator.next();
            if(docId.equals(rev.getDocId()) && revId.equals(rev.getRevId())) {
                return rev;
            }
        }
        return null;
    }

    public List<String> getAllDocIds() {
        List<String> result = new ArrayList<String>();

        Iterator<RevisionInternal> iterator = iterator();
        while(iterator.hasNext()) {
            RevisionInternal rev = iterator.next();
            result.add(rev.getDocId());
        }

        return result;
    }

    public List<String> getAllRevIds() {
        List<String> result = new ArrayList<String>();

        Iterator<RevisionInternal> iterator = iterator();
        while(iterator.hasNext()) {
            RevisionInternal rev = iterator.next();
            result.add(rev.getRevId());
        }

        return result;
    }

    public void sortBySequence() {
        Collections.sort(this, new Comparator<RevisionInternal>() {

            public int compare(RevisionInternal rev1, RevisionInternal rev2) {
                return Misc.TDSequenceCompare(rev1.getSequence(), rev2.getSequence());
            }

        });
    }

    /**
     * in CBL_Revision.m
     * - (void) sortByDocID
     */
    public void sortByDocID() {
        Collections.sort(this, new Comparator<RevisionInternal>() {
            public int compare(RevisionInternal rev1, RevisionInternal rev2) {
                return rev1.getDocId().compareTo(rev2.getDocId());
            }
        });
    }

    public void limit(int limit) {
        if(size() > limit) {
            removeRange(limit, size());
        }
    }

    public RevisionInternal revWithDocId(String docId) {
        Iterator<RevisionInternal> iterator = iterator();
        while(iterator.hasNext()) {
            RevisionInternal rev = iterator.next();
            if (rev.getDocId() != null && rev.getDocId().equals(docId)) {
                return rev;
            }
        }
        return null;
    }

    public RevisionInternal removeAndReturnRev(RevisionInternal rev) {
        int index = this.indexOf(rev);
        if (index == -1) {
            return null;
        }
        RevisionInternal resultRev = this.remove(index);
        return resultRev;
    }
}
