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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tuxjsmith
 */
public class FileNameValidatorTest {
    
    public FileNameValidatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * ID: FileNameValidator 001<br> 
     * EXPECTED RESULT: E-Tech_XSlate_GKrellM.tar.gz = FILE_NAME_STATUS.GOOD_FILENAME <br>
     * DESCRIPTION:<br>
     * Test of <b>validateGzipTarFileName</b> method.
     * - Good file name.
     */
    @Test
    public void testValidateGzipTarFileName_1() {
        
        System.out.println("FileNameValidator 001");
        
        final String GOOD_FILE_NAME = "E-Tech_XSlate_GKrellM.tar.gz";
        
        assertEquals(Constants.FILE_NAME_STATUS.GOOD_FILENAME, FileNameValidator.validateGzipTarFileName(GOOD_FILE_NAME));
    }
    
    /**
     * ID: FileNameValidator 002<br> 
     * EXPECTED RESULT: E^Tech&XSlate£GKrellM.tar.gz = FILE_NAME_STATUS.ILLEGAL_FILENAME_CHARACTER <br>
     * DESCRIPTION:<br>
     * Test of <b>validateGzipTarFileName</b> method.
     * - Illegal characters in file name.
     */
    @Test
    public void testValidateGzipTarFileName_2 () {
        
        System.out.println("FileNameValidator 002");
        
        final String BAD_FILE_NAME_ILLEGAL_FILENAME_CHARACTER =  "E^Tech&XSlate£GKrellM.tar.gz";
        
        assertEquals(Constants.FILE_NAME_STATUS.ILLEGAL_FILENAME_CHARACTER, FileNameValidator.validateGzipTarFileName(BAD_FILE_NAME_ILLEGAL_FILENAME_CHARACTER));
    }
    
    /**
     * ID: FileNameValidator 003<br> 
     * EXPECTED RESULT: E-Tech_XSlate_GKrellM.tr.gz = FILE_NAME_STATUS.MISSING_TAR_GZ <br>
     * DESCRIPTION:<br>
     * Test of <b>validateGzipTarFileName</b> method.
     * - Missing .tar.gz.
     */
    @Test
    public void testValidateGzipTarFileName_3 () {
        
        System.out.println("FileNameValidator 003");
        
        final String BAD_FILE_NAME_MISSING_TAR_GZ =  "E-Tech_XSlate_GKrellM.tr.gz";
        
        assertEquals(Constants.FILE_NAME_STATUS.MISSING_TAR_GZ, FileNameValidator.validateGzipTarFileName(BAD_FILE_NAME_MISSING_TAR_GZ));
    }

    /**
     * ID: FileNameValidator 004<br> 
     * EXPECTED RESULT: ConcurrentHashMap containing two file names and FILE_NAME_STATUS: <br>
     * - HeliX%Sweetpill.gkrellm.tar.gz, FILE_NAME_STATUS.ILLEGAL_FILENAME_CHARACTER<br>
     * - SolarE-2.gkrellm.tar.z, FILE_NAME_STATUS.MISSING_TAR_GZ<br>
     * DESCRIPTION:<br>
     * Test of <b>getBadGzipTarFileNames</b> method.
     */
    @Test
    public void testGetBadGzipTarFileNames() {
        
        System.out.println("FileNameValidator 004");
        
        String spaceSeparatedFileNames = "D.A.E.gkrellm.tar.gz Dune-GKrellM.tar.gz HeliX%Sweetpill.gkrellm.tar.gz SolarE-2.gkrellm.tar.z";
        
        ConcurrentHashMap<String, Constants.FILE_NAME_STATUS> expResult = new ConcurrentHashMap ();
        expResult.put("HeliX%Sweetpill.gkrellm.tar.gz", Constants.FILE_NAME_STATUS.ILLEGAL_FILENAME_CHARACTER);
        expResult.put("SolarE-2.gkrellm.tar.z", Constants.FILE_NAME_STATUS.MISSING_TAR_GZ);
        
        ConcurrentHashMap<String, Constants.FILE_NAME_STATUS> result = FileNameValidator.getBadGzipTarFileNames(spaceSeparatedFileNames);
        
        assertEquals(expResult, result);
    }    
}
