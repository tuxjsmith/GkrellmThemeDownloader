/*
 * Copyright (c) 2017, tuxjsmith
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

import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static net.logfarm.javaFXGkrellmDownloader.Constants.WEBPAGE;

/**
 *
 * @author tuxjsmith
 */
public interface GuiConstants {
    
    Button QTY_NAMES_ONLY_BTN = new Button("Qty and names only"),
            DOWNLOAD_THEMES_BTN = new Button("Download themes"),
            STOP_BTN = new Button("Stop"),
            QUIT_BTN = new Button("Close application"),
            CHOOSE_BTN = new Button("Choose");

    TextField DOWNLOAD_LOCATION_TF = new TextField (),//new TextField (System.getProperty("user.home") + File.separator + ".gkrellm2" + File.separator + "themes"), //("/home/tuxjsmith/stuff/theme_download_test"),
            WEB_PAGE_URL_TF = new TextField(WEBPAGE),
            ONLY_GET_TF = new TextField();

    TextArea THEME_FILE_NAMES_TA = new TextArea();

    Label THEME_FILE_NAME_LBL = new Label("Theme file names"),
            QUANTITY_LBL = new Label("Qty:"),
            WEB_PAGE_WITH_LIST_OF_FILES_LBL = new Label("Web page that has list of themes"),
            WHERE_TO_DOWNLOAD_LBL = new Label("Where to download and extract gkrellm themes");

    CheckBox ONLY_GET_CB = new CheckBox("only get");

    ProgressBar DOWNLOAD_PROGRESS_PB = new ProgressBar();

    HBox THEME_NAMES_AND_QTY_HBOX = new HBox(15),
                       INDIVIDUAL_FILES_DOWNLOAD_HBOX = new HBox(20),
                       DOWNLOAD_LOCATION_HBOX = new HBox(5);

    VBox DOWNLOAD_LOAD_LOCATION_VBOX = new VBox(35);

    GridPane GRID = new GridPane();
    
    Alert ALERT = new Alert(Alert.AlertType.CONFIRMATION, "A MESSAGE");   
}
