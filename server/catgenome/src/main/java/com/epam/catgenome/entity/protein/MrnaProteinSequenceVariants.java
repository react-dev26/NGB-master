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

package com.epam.catgenome.entity.protein;

import java.util.List;
import java.util.Map;

import com.epam.catgenome.entity.track.Block;

/**
 * Created: 2/24/2016
 * Project: CATGenome Browser
 * <p>
 * This class represents structure to display on UI results of protein sequence reconstruction with considering
 * variations.
 * </p>
 */
public class MrnaProteinSequenceVariants extends Block {

    /**
     * Map of mRNA to it's variants of reconstructed protein sequences.
     * 1 mRNA correspond to many reconstructed protein sequences, depends on variation, taking into consideration.
     */
    private Map<String, List<List<ProteinSequence>>> proteinSequences;

    public MrnaProteinSequenceVariants(final Integer startIndex, final Integer endIndex,
            final Map<String, List<List<ProteinSequence>>> proteinSequences) {
        super(startIndex, endIndex);
        this.proteinSequences = proteinSequences;
    }

    public Map<String, List<List<ProteinSequence>>> getProteinSequences() {
        return proteinSequences;
    }

    public void setProteinSequences(
            final Map<String, List<List<ProteinSequence>>> proteinSequences) {
        this.proteinSequences = proteinSequences;
    }
}
