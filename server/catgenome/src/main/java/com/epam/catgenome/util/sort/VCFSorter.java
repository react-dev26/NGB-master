/*
 * MIT License
 *
 * Copyright (c) 2017 EPAM Systems
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

package com.epam.catgenome.util.sort;

import htsjdk.tribble.readers.AsciiLineReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class VCFSorter extends AbstractFeatureSorter {

    private static final Logger LOG = LoggerFactory.getLogger(VCFSorter.class);

    public VCFSorter(File inputFile, File outputFile, File tmpDir) {
        super(inputFile, outputFile, tmpDir);
    }

    @Override
    Parser getParser() {
        return new Parser(0, 1);
    }

    @Override
    String writeHeader(AsciiLineReader reader, PrintWriter writer) {
        try {
            String nextLine = reader.readLine();
            while (nextLine.startsWith("#")) {
                writer.println(nextLine);
                nextLine = reader.readLine();
            }

            return nextLine;
        } catch (IOException e) {
            LOG.error("Error writing header", e);
            return null;
        }


    }
}
