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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.GZIPInputStream;
import javafx.application.Platform;
import net.logfarm.javaFXGkrellmDownloader.Constants.DOWNLOAD_STATUS;
import static net.logfarm.javaFXGkrellmDownloader.Constants.THEME_FILE_NAMES_AL;
import static net.logfarm.javaFXGkrellmDownloader.Constants.USER_AGENT;
import static net.logfarm.javaFXGkrellmDownloader.Constants.USER_LIST_OF_FILES_AL;
import static net.logfarm.javaFXGkrellmDownloader.Constants.WEBPAGE;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_LOCATION_TF;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_PROGRESS_PB;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.QUANTITY_LBL;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.THEME_FILE_NAMES_TA;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.WEB_PAGE_URL_TF;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import static net.logfarm.javaFXGkrellmDownloader.Constants.TEST_DOWNLOAD_DIR;

/**
 * 
 * @author tuxjsmith
 */
public class HtmlSplitter {

    private static Boolean keepGoing_b = Boolean.TRUE;

    /**
     * Break download for-loops.
     * @param b set Boolean true or false. 
     */
    public static void setKeepGoing (Boolean b) {
        
        keepGoing_b = b;
    }

    /**
     * Create a list of gzip file names that a web page references.
     * 
     * @param url_s URL string, currently required to start with: http://
     * @param reverseSortOrder we reverse sort order when downloading files so
     * their names are listed from in a descending order.
     * @return ArrayList&lt;String&gt; list of file names
     */
    public static ArrayList<String> getFileNamesFromWeb(String url_s,
                                                        Boolean reverseSortOrder) {
        
        THEME_FILE_NAMES_AL.clear();
        
        try {
        
            /*
                https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
            */
            final StringBuilder RESULT = new StringBuilder();
            
            /*
                We don't need any special behavior so we use URLConnection
                https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html

                HttpURLConnection could have been used also.

                https://docs.oracle.com/javase/8/docs/api/java/net/URL.html
             */
            final URL URL_FILE_NAMES_ONLY = new URL(url_s);

            final URLConnection CONNECTION_FILE_NAMES_ONLY = URL_FILE_NAMES_ONLY.openConnection();

            CONNECTION_FILE_NAMES_ONLY.setRequestProperty("User-Agent", USER_AGENT);

            /*
                Wrapping BufferedReader in a try statement like this will 
                automatically close the BufferedReader when the statement is 
                complete.

                https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html

                https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html#getInputStream--
             */
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CONNECTION_FILE_NAMES_ONLY.getInputStream(), "UTF-8"))) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    RESULT.append(line);
                    RESULT.append("\n");
                }

                /*
                    Based on what I found by manually reading the HTML source we 
                    split the HTML text, boiling it down to theme file names:

                    - For each 'href=' (split HTML text at each 'href=')

                        - split again at the first occurence of '>'
                          so we are left with the text inbetween 'href=' and before '>'  

                        - if what we are left with, ends with '.tar.gz'
                          we assume we have found a reference to a theme file

                        - we only want the file name so we take the extra step
                          of removing '/gkrellm/' from the resulting string but
                          if '/gkrellm/' is not part of the string, no harm is
                          done
                 */
                for (int i = 1; i < RESULT.toString().split("href=").length; i++) {
                    
                    /*
                        Not sure how useful stopping this process is.
                    */
                    if (!keepGoing_b) break;

                    if (RESULT.toString().split("href=")[i].split(">")[0].endsWith(".tar.gz")) {

                        /*
                            Add to the list of theme file names.
                         */
                        THEME_FILE_NAMES_AL.add(RESULT.toString().split("href=")[i].split(">")[0].replaceAll("/gkrellm/", ""));
                    }
                }
                
                Collections.sort(THEME_FILE_NAMES_AL, 
                                (reverseSortOrder) ? Collections.reverseOrder() : null);
            }
        } 
        catch (IOException ioe) {

            System.err.println(ioe.getMessage());
        }
        
        return THEME_FILE_NAMES_AL;
    }

    /**
     * Download theme .tar.gz files and extract both gz and tar to a specified
     * download location.
     * @param withGui false so JUnit can test this method.
     * @return DOWNLOAD_STATUS enum value.
     */
    public static DOWNLOAD_STATUS downloadAllThemes(Boolean withGui) {
      
        THEME_FILE_NAMES_AL.clear();
        
        if (withGui) {
            
            Platform.runLater(() -> {

                THEME_FILE_NAMES_TA.clear();
            });
        }
        
        if (!USER_LIST_OF_FILES_AL.isEmpty()) {
            
            THEME_FILE_NAMES_AL.addAll (USER_LIST_OF_FILES_AL);
        }
        else {

            if (withGui) {
                
                getFileNamesFromWeb(WEB_PAGE_URL_TF.getText(),
                                    Boolean.TRUE);
            }
            else {
                
                getFileNamesFromWeb(WEBPAGE,
                                    Boolean.TRUE);
            }
        }
        
        final Float QTY_FRAGMENT = 1.0f / THEME_FILE_NAMES_AL.size();
        
        if (withGui) {
            
            Platform.runLater(() -> {

                DOWNLOAD_PROGRESS_PB.setProgress (0);
            });
        }

        try {

            /*
                For each gzipped theme ...
             */
            for (int i = 0; i < THEME_FILE_NAMES_AL.size(); i++) {

                if (!keepGoing_b) break;

                /*
                    Platform.runLater requires the file name to be a constant.
                */
                final String FILE_NAME = THEME_FILE_NAMES_AL.get(i) + "\n";
                //
                if (withGui) {
                    
                    Platform.runLater(() -> {

                        THEME_FILE_NAMES_TA.insertText(0, FILE_NAME); 
                    });
                }

                final URL URL_DOWNLOAD_FILE = new URL("http://www.muhri.net/gkrellm/" + THEME_FILE_NAMES_AL.get(i));

                final URLConnection CONNECTION_DOWNLOAD_FILE = URL_DOWNLOAD_FILE.openConnection();

                CONNECTION_DOWNLOAD_FILE.setRequestProperty("User-Agent", USER_AGENT);

                /*
                    A .tar.gz is what the web page is giving us in the form of an
                    input stream which is parsed to a new GZIPInputStream.

                    The GZIPInputStream is in turn passed to a new TarArchiveStream.

                    All of which is wrapped in a try-with-resources statement

                    We use the GZIPInputStream from the Java JDK:
                    https://docs.oracle.com/javase/8/docs/api/java/util/zip/GZIPInputStream.html

                    but TarArchiveInputStream comes from: Apache Commons Compress
                    http://commons.apache.org/proper/commons-compress/index.html
                    http://commons.apache.org/proper/commons-compress/javadocs/api-release/org/apache/commons/compress/archivers/tar/TarArchiveInputStream.html
                 */
                try (final TarArchiveInputStream TAIS = new TarArchiveInputStream(new GZIPInputStream(CONNECTION_DOWNLOAD_FILE.getInputStream()))) {

                    TarArchiveEntry entry;

                    /*
                        Loop through each tar entry, reconstructing the archives
                        files and directory structure.

                        The TarArchiveInputStream (TAIS) holds each entry's bytes.
                     */
                    while ((entry = (TarArchiveEntry) TAIS.getNextEntry()) != null) {

                        final File EXTRACTED_TAR_ENTRY;
                        
                        if (withGui) {
                        
                            EXTRACTED_TAR_ENTRY = new File(DOWNLOAD_LOCATION_TF.getText(), entry.getName());
                        }
                        else {
                            
                            TEST_DOWNLOAD_DIR.mkdirs();
                            
                            EXTRACTED_TAR_ENTRY = new File(TEST_DOWNLOAD_DIR, entry.getName());
                        }

                        /*
                            Create directory structures.
                         */
                        if (entry.isDirectory()) {

                            EXTRACTED_TAR_ENTRY.mkdirs();
                        } 
                        
                        /*
                            Create files.
                         */ 
                        else {

                            try (final BufferedOutputStream BOS = new BufferedOutputStream(new FileOutputStream(EXTRACTED_TAR_ENTRY))) {

                                /*
                                    http://commons.apache.org/proper/commons-compress/javadocs/api-release/org/apache/commons/compress/utils/IOUtils.html
                                 */
                                IOUtils.copy(TAIS, BOS);
                            }
                        }
                    }
                }

                final Integer CURRENT_INDEX = i + 1;
                //
                if (withGui) {
                    
                    Platform.runLater(() -> {

                        DOWNLOAD_PROGRESS_PB.setProgress (QTY_FRAGMENT * CURRENT_INDEX);
                    });
                }

                /*
                    Increment Qty display 1 / 187, 2 / 187, etc.
                */
                final Integer QTY = i + 1,
                              TOTAL = THEME_FILE_NAMES_AL.size();
                //
                if (withGui) {
                    
                    Platform.runLater(() -> {

                        QUANTITY_LBL.setText("Qty: " + QTY + " / " + TOTAL); 
                    });
                }
            }
        } 
        catch (IOException ioe) {

            System.err.println(ioe.getMessage());

            return DOWNLOAD_STATUS.NETWORK_ERROR;
        }
        
        return DOWNLOAD_STATUS.DOWNLOAD_GOOD;
    }
    
    /**
     * The current quantity of file names from the collection that has already
     * been populated.
     * @return Integer, quantity of files.
     */
    static Integer getQty() {
        
        return THEME_FILE_NAMES_AL.size();
    }
}
