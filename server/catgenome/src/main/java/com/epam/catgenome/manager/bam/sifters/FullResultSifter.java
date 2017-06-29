/*
 * MIT License
 *
 * Copyright (c) 2016 EPAM Systems
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.epam.catgenome.manager.bam.sifters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.epam.catgenome.entity.bam.BasePosition;
import com.epam.catgenome.entity.bam.Read;
import com.epam.catgenome.entity.wig.Wig;
import com.epam.catgenome.util.BamUtil;
import htsjdk.samtools.SAMRecord;

/**
 * Source:
 * Created:     7/4/2016
 * Project:     CATGenome Browser
 * Make:        IntelliJ IDEA 14.1.1, JDK 1.8
 * Represents (@code DownsamplingSifter) without actual downsampling: all records will be added to the
 * records list and coverage result.
 */
public class FullResultSifter implements DownsamplingSifter<SAMRecord> {
    private final List<Read> readList = new ArrayList<>();
    private int filteredReadsCount = 0;
    private boolean exceedsMaxReadCount = false;

    //private int maxReadCount;

    public FullResultSifter(boolean coverageOnly) {
        // TODO: int maxReadCount - decide reads or coverage by by read count
        //this.maxReadCount = maxReadCount;
        this.exceedsMaxReadCount = coverageOnly;
    }

    @Override
    public void add(final SAMRecord record, final int start, final int end,
                    final List<BasePosition> differentBase, final String headStr, final String tailStr)
            throws IOException {
        if (!exceedsMaxReadCount) {
            readList.add(BamUtil.createReadFromRecord(record, start, end, differentBase, headStr, tailStr));
        }

        filteredReadsCount++;
        /*if (!exceedsMaxReadCount && filteredReadsCount > maxReadCount) { //TODO: uncomment
            exceedsMaxReadCount = true;
            readList.clear();
        }*/
    }

    @Override
    public List<Read> getReadListResult() {
        return readList;
    }

    @Override
    public List<Wig> getDownsampleCoverageResult() {
        return Collections.emptyList();
    }

    @Override
    public int getFilteredReadsCount() {
        return filteredReadsCount;
    }
}
