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

package com.epam.catgenome.manager.bam.filters;

import com.epam.catgenome.entity.bam.BasePosition;
import com.epam.catgenome.manager.bam.sifters.DownsamplingSifter;

import java.io.IOException;
import java.util.List;

/**
 * Represents a filter for read objects. This interface filters read data before it is given
 * to the (@code DownsamplingSifter) for downsampling.
 */
public interface Filter<T> {
    /**
     * @return filter's wrapped Sifter
     */
    DownsamplingSifter<T> getSifter();

    /**
     * @param record representing a read
     * @param start of the read
     * @param end of the read
     * @param differentBase list of read bases that differ from the reference nucleotides
     * @param headStr soft clipped start of the read
     * @param tailStr soft clipped end of the read
     * @throws IOException
     */
    void add(T record, int start, int end, List<BasePosition> differentBase, String headStr,
             String tailStr) throws IOException;

}
