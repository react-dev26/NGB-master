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

package com.epam.catgenome.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.epam.catgenome.controller.vo.UrlRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.catgenome.controller.vo.FilesVO;
import com.epam.catgenome.manager.BiologicalDataItemManager;
import com.epam.catgenome.manager.FileManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Source:      UtilsController
 * Created:     25.04.16, 16:11
 * Project:     CATGenome Browser
 * Make:        IntelliJ IDEA 14.1.4, JDK 1.8
 *
 * <p>
 * A REST controller to supply some common util data
 * </p>
 */
@Controller
public class UtilsController extends AbstractRESTController {
    @Autowired
    private FileManager fileManager;

    @Autowired
    private BiologicalDataItemManager biologicalDataItemManager;

    @Value("#{catgenome['version']}")
    private String version;

    @ResponseBody
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ApiOperation(
            value = "Returns application's version",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {@ApiResponse(code = HTTP_STATUS_OK, message = API_STATUS_DESCRIPTION)
            })
    public Result<String> loadVersion() {
        return Result.success(version);
    }

    @ResponseBody
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    @ApiOperation(
        value = "Returns directory contents",
        notes = "Returns directory contents, specified by path",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = {@ApiResponse(code = HTTP_STATUS_OK, message = API_STATUS_DESCRIPTION)
        })
    public Result<FilesVO> loadDirectoryContents(@RequestParam(required = false) String path)
        throws IOException {
        return Result.success(new FilesVO(fileManager.loadDirectoryContents(path), fileManager.getNgsDataRootPath()));
    }

    @ResponseBody
    @RequestMapping(value = "/files/allowed", method = RequestMethod.GET)
    @ApiOperation(
            value = "Checks is directory browsing is allowed",
            notes = "Returns true if directory browsing is allowed and false if not",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {@ApiResponse(code = HTTP_STATUS_OK, message = API_STATUS_DESCRIPTION)
            })
    public Result<Boolean> isFilesBrowsingAllowed()
            throws IOException {
        return Result.success(fileManager.isFilesBrowsingAllowed());
    }

    @ResponseBody
    @RequestMapping(value = "/url", method = RequestMethod.POST)
    @ApiOperation(
        value = "Generates URL postfix",
        notes = "Generates URL that displays specified files, optionally on specified interval",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = {@ApiResponse(code = HTTP_STATUS_OK, message = API_STATUS_DESCRIPTION)
        })
    public Result<String> generateUrl(
                    @RequestBody UrlRequestVO request,
                    @RequestParam(required = false) String chromosomeName,
                    @RequestParam(required = false) Integer startIndex,
                    @RequestParam(required = false) Integer endIndex) throws JsonProcessingException {
        return Result.success(biologicalDataItemManager.generateUrl(request.getDataset(),
                request.getIds() == null ? Collections.emptyList() : request.getIds(), chromosomeName,
                startIndex, endIndex));
    }

    @ResponseBody
    @RequestMapping(value = "/defaultTrackSettings", method = RequestMethod.GET)
    @ApiOperation(
            value = "Return default track settings",
            notes = "Return default track settings, which specified in catgenome.properties file",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {@ApiResponse(code = HTTP_STATUS_OK, message = API_STATUS_DESCRIPTION)
            })
    public Result<Map<String, Map<String, Object>>> getDefaultTracksSettings() throws IOException {
        return Result.success(fileManager.getDefaultTrackSettings());
    }
}
