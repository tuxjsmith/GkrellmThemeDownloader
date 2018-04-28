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
package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author tuxjsmith
 */
public interface LFGTD_Constants {
    
    /*
        List of theme file names I.e. themeNames.tar.gz, anotherTheme.tar.gz
     */
    ArrayList<String> THEME_FILE_NAMES_AL = new ArrayList();
    
    /*
        List of invalid file names and their file name status.
    */
    ConcurrentHashMap<String, FILE_NAME_STATUS> BAD_FILE_NAMES_HM = new ConcurrentHashMap ();
    
    /*
        List of files to download manually entered by the user.
    */
    ArrayList<String> USER_LIST_OF_FILES_AL = new ArrayList();
    
    /*
        We declare our intentions here so the administrator can decide whether
        or not to block its use.
        USER_AGENT
    
        Application title and the user agent text.
        APPLICATION_TITLE
    
        Default web page that lists .tar.gz theme files. Also used for testing.
        WEBPAGE
     */
    String VERSION = "0.2",
           APPLICATION_TITLE = "Gkrellm Theme Downloader " + VERSION,
           USER_AGENT = APPLICATION_TITLE,
           WEBPAGE = "http://www.muhri.net/gkrellm/nav.php3?node=gkrellmall&sort=added&conf=DESC";
    
    /*
        Download location for testing.
    */
    File TEST_DOWNLOAD_DIR = new File (System.getProperty("user.home") + File.separator + "temp" + File.separator + "gkrellm_themes");
    
    /*
        When validating file names we return one of these values.
        EMPTY_STRING is currently not useful.
    */
    enum FILE_NAME_STATUS {
        
        GOOD_FILENAME,
        EMPTY_STRING,
        MISSING_TAR_GZ,
        ILLEGAL_FILENAME_CHARACTER;
        
        FILE_NAME_STATUS () {}
    }
    
    /*
        When downloading themes one of these values is return.
        DOWNLOAD_LOCATION_DOES_NOT_EXIST is currently not useful.
    */
    enum DOWNLOAD_STATUS {
        
        DOWNLOAD_GOOD,
        DOWNLOAD_LOCATION_DOES_NOT_EXIST,
        NETWORK_ERROR;
        
        DOWNLOAD_STATUS () {}
    }
}
