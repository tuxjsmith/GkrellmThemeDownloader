/*
 * Copyright (c) 2017, tuxjsmith@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.logfarm.javaFXGkrellmDownloader;

import java.util.concurrent.ConcurrentHashMap;
import static net.logfarm.javaFXGkrellmDownloader.Constants.BAD_FILE_NAMES_HM;
import net.logfarm.javaFXGkrellmDownloader.Constants.FILE_NAME_STATUS;
import static net.logfarm.javaFXGkrellmDownloader.Constants.FILE_NAME_STATUS.EMPTY_STRING;
import static net.logfarm.javaFXGkrellmDownloader.Constants.FILE_NAME_STATUS.GOOD_FILENAME;
import static net.logfarm.javaFXGkrellmDownloader.Constants.USER_LIST_OF_FILES_AL;

/**
 *
 * @author tuxjsmith
 */
public class FileNameValidator {

    /**
     * Check whether a string file name meets a .tar.gz file name format I.e.:<br>
     * fileName.tar.gz
     * @param fileName string file name to validate
     * @return valid or not
     */
    public static FILE_NAME_STATUS validateGzipTarFileName(String fileName) {

        if (fileName.isEmpty()) {
            
            return FILE_NAME_STATUS.EMPTY_STRING;
        }
        
        /*
            file name ends with: .tar.gz
            alpha numeric characters
            _ (underscore)
            - (minus)
            . (full stop)
        
            The + symbol matches the preceding pattern one or more times.
            Without the + symbol matching will fail.
        
            ( for reference: fileName.matches("[a-zA-Z0-9_\\-\\.]+\\.tar\\.gz$") )
        */
        if (!fileName.endsWith(".tar.gz")) {
            
            return FILE_NAME_STATUS.MISSING_TAR_GZ;
        }
        else if (!fileName.matches("[a-zA-Z0-9_\\-\\.]+")) {
            
            return FILE_NAME_STATUS.ILLEGAL_FILENAME_CHARACTER;
        }
        
        return FILE_NAME_STATUS.GOOD_FILENAME;
    }

    /**
     * Loop through a string of space separated file names, validate each
     * and return a hashMap of file name and status.
     * @param spaceSeparatedFileNames
     * @return ConcurrentHashMap of file names and status enum value
     */
    static ConcurrentHashMap<String, FILE_NAME_STATUS> getBadGzipTarFileNames(String spaceSeparatedFileNames) {
        
        BAD_FILE_NAMES_HM.clear();
        USER_LIST_OF_FILES_AL.clear();
        
        for (String fileName : spaceSeparatedFileNames.split(" ")) {
            
            final FILE_NAME_STATUS STATUS = validateGzipTarFileName (fileName.trim());
            
            if (!STATUS.equals(GOOD_FILENAME)
                && !STATUS.equals(EMPTY_STRING)) {
                
                BAD_FILE_NAMES_HM.put(fileName, STATUS);
            }
            else if (STATUS.equals(GOOD_FILENAME)) {
                
                USER_LIST_OF_FILES_AL.add(fileName);
            }
        }
        
        return BAD_FILE_NAMES_HM;
    }
}
