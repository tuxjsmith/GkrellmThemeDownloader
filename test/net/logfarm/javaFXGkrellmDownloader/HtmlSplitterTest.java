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

import utilities.LFGTD_HtmlSplitter;
import utilities.LFGTD_FileNameValidator;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static junit.framework.TestCase.assertEquals;
import utilities.LFGTD_Constants.DOWNLOAD_STATUS;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static utilities.LFGTD_Constants.TEST_DOWNLOAD_DIR;
import static utilities.LFGTD_Constants.THEME_FILE_NAMES_AL;
import static utilities.LFGTD_Constants.USER_LIST_OF_FILES_AL;
import static utilities.LFGTD_Constants.WEBPAGE;
import static org.junit.Assert.*;
import utilities.LFGTD_Constants;

/**
 *
 * @author tuxjsmith
 */
public class HtmlSplitterTest {

    public HtmlSplitterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Before each test is run.
     */
    @Before
    public void setUp() {

        System.out.println("Set up, remove all previously downloaded themes.");

        FileVisitor<Path> themePurger = new PurgeThemeDirectories();
        //
        try {

            Files.walkFileTree(TEST_DOWNLOAD_DIR.toPath(), themePurger);
        } catch (IOException ex) {

            System.err.println(ex.getMessage());
        }
    }

    /**
     * After each test is run.
     */
    @After
    public void tearDown() {
    }

    /**
     * ID: LFGTD_HtmlSplitter 001<br> 
     * EXPECTED RESULT: THEME_FILE_NAMES_AL.size() = DirectoryCount.getCount().intValue() <br>
     * DESCRIPTION:<br>
     * Test of <b>downloadAllThemes</b> method.<br>
     * Download all themes listed by the web page.
     */
    @Test
    public void testDownloadAllThemes_1 () {

        System.out.println("HtmlSplitter 001");

        /*
            All themes have been downloaded.
         */
        assertEquals(DOWNLOAD_STATUS.DOWNLOAD_GOOD, LFGTD_HtmlSplitter.downloadAllThemes(Boolean.FALSE));

        /*
            Confirm the correct number of themes have been downloaded.
         */
        FileVisitor dirCounter = new CountThemeDirectories();
        
        //
        try {

            Files.walkFileTree(TEST_DOWNLOAD_DIR.toPath(), dirCounter);
        } catch (IOException ex) {

            System.err.println(ex.getMessage());
        }
        //
        assertEquals(THEME_FILE_NAMES_AL.size(), DirectoryCount.getCount().intValue());
    }
    
    /**
     * ID: LFGTD_HtmlSplitter 002<br> 
     * EXPECTED RESULT: THEME_FILE_NAMES_AL.size() = DirectoryCount.getCount().intValue()<br> 
     * DESCRIPTION:<br> 
     * Test of <b>downloadAllThemes</b> method.<br>
     * - User defined file names.
     */
    @Test
    public void testDownloadAllThemes_2 () {

        System.out.println("HtmlSplitter 002");

        /*
            Download a select few themes
        */
        USER_LIST_OF_FILES_AL.add ("E-Tech_XSlate_GKrellM.tar.gz");
        USER_LIST_OF_FILES_AL.add ("Eazel.gkrellm.tar.gz");
        USER_LIST_OF_FILES_AL.add ("Evolution.tar.gz");
        //
        assertEquals(DOWNLOAD_STATUS.DOWNLOAD_GOOD, LFGTD_HtmlSplitter.downloadAllThemes(Boolean.FALSE));
        
        /*
            Confirm the correct number of themes have been downloaded.
        */
        FileVisitor dirCounter = new CountThemeDirectories();
        //
        try {

            Files.walkFileTree(TEST_DOWNLOAD_DIR.toPath(), dirCounter);
        } catch (IOException ex) {

            System.err.println(ex.getMessage());
        }
        //
        assertEquals(THEME_FILE_NAMES_AL.size(), DirectoryCount.getCount().intValue());
    }

    /**
     * ID: LFGTD_HtmlSplitter 003<br> 
     * EXPECTED RESULT: Number of file names = 187<br> 
     * DESCRIPTION:<br> 
     * Test of <b>getThemeGzipFilenames</b> method.<br>
     * Get file names and quantity only.
     */
    @Test
    public void testGetFileNamesFromWeb_1 () {
        
        System.out.println("HtmlSplitter 003");
        
        final String URL_S = WEBPAGE; 
        
        final Integer QTY = 187;
        
        assertEquals(QTY.intValue(), LFGTD_HtmlSplitter.getFileNamesFromWeb(URL_S, Boolean.TRUE).size());
    }
    
    /**
     * ID: LFGTD_HtmlSplitter 004<br> 
     * EXPECTED RESULT:<br> 
     * DESCRIPTION:<br> 
     * Test of <b>getThemeGzipFilenames</b> method.<br>
     * - Confirm that .tar.gz theme names have not become corrupt.<br>
     */
    @Test
    public void testGetFileNamesFromWeb_2 () {
        
        System.out.println("HtmlSplitter 004");
        
        final String URL_S = WEBPAGE; 
        
        /*
            Validate downloaded .tar.gz file names
        */
        for (String fileName : LFGTD_HtmlSplitter.getFileNamesFromWeb(URL_S, Boolean.TRUE)) {
            
            LFGTD_Constants.FILE_NAME_STATUS fileNameStatus = LFGTD_FileNameValidator.validateGzipTarFileName(fileName);
            
            if (!fileNameStatus.equals(LFGTD_Constants.FILE_NAME_STATUS.GOOD_FILENAME)) {
                
                fail ("invalid file name");
            }
            else {
                
                System.out.println (fileNameStatus.toString());
            }
        }
    }
    
    /**
     * ID: LFGTD_HtmlSplitter 005<br>
     * EXPECTED RESULT: getQty = 187<br> 
     * DESCRIPTION: Test of <b>getQty</b> method.<br>
     */
    @Test
    public void testGetQty() {
        
        System.out.println("HtmlSplitter 005");
        
        final Integer QTY = 187;
        
        assertEquals(QTY, LFGTD_HtmlSplitter.getQty());
    }
    
    /*
        Directory counter.
    */
    static class DirectoryCount {

        private static Integer count = 0;

        static void increment() {

            count++;
        }

        static Integer getCount() {

            return count;
        }
        
        static void resetDirectoryCounter () {
            
            count = 0;
        }
    }

    class CountThemeDirectories extends SimpleFileVisitor<Path> {
        
        public CountThemeDirectories () {
            
            DirectoryCount.resetDirectoryCounter();
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {

            /*
                If the directory is the root of TEST_DOWNLOAD_DIR then
                we continue without counting it otherwise we increment
                the directory counter.
             */
            if (aDir.endsWith(TEST_DOWNLOAD_DIR.getName())) {

                return FileVisitResult.CONTINUE;
            } else {

                DirectoryCount.increment ();

                return FileVisitResult.SKIP_SUBTREE;
            }
        }
    }
    
    class PurgeThemeDirectories extends SimpleFileVisitor<Path> {

        /*
            Delete files in each theme directory tree.
         */
        @Override
        public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {

            Files.delete(aFile);

            return FileVisitResult.CONTINUE;
        }

        /*
            Delete theme directories. visitFile will have emptied them.
         */
        @Override
        public FileVisitResult postVisitDirectory(Path aDir, IOException ioe) throws IOException {

            if (ioe == null) {

                if (!aDir.endsWith(TEST_DOWNLOAD_DIR.getName())) {

                    Files.delete(aDir);
                }

                return FileVisitResult.CONTINUE;
            } else {

                throw ioe;
            }
        }
    }
}
