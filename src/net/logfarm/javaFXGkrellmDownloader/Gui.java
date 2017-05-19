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

 /*
    TODO: COLOUR CODE EACH TEXT FIELD 
    TODO: GENERATE JAVA DOC
 */
package net.logfarm.javaFXGkrellmDownloader;

import java.io.File;
import java.util.Enumeration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import static net.logfarm.javaFXGkrellmDownloader.Constants.APPLICATION_TITLE;
import static net.logfarm.javaFXGkrellmDownloader.Constants.BAD_FILE_NAMES_HM;
import net.logfarm.javaFXGkrellmDownloader.Constants.DOWNLOAD_STATUS;
import static net.logfarm.javaFXGkrellmDownloader.Constants.USER_LIST_OF_FILES_AL;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.ALERT;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.CHOOSE_BTN;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_LOAD_LOCATION_VBOX;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_LOCATION_HBOX;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_LOCATION_TF;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_PROGRESS_PB;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.DOWNLOAD_THEMES_BTN;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.GRID;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.INDIVIDUAL_FILES_DOWNLOAD_HBOX;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.ONLY_GET_CB;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.ONLY_GET_TF;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.QTY_NAMES_ONLY_BTN;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.QUANTITY_LBL;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.QUIT_BTN;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.STOP_BTN;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.THEME_FILE_NAMES_TA;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.THEME_FILE_NAME_LBL;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.THEME_NAMES_AND_QTY_HBOX;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.WEB_PAGE_URL_TF;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.WEB_PAGE_WITH_LIST_OF_FILES_LBL;
import static net.logfarm.javaFXGkrellmDownloader.GuiConstants.WHERE_TO_DOWNLOAD_LBL;

/**
 *
 * @author tuxjsmith
 */
public class Gui extends Application {

    private Fetch fetch;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

//        GRID.setGridLinesVisible(true);

        /*
            GUI title bar text
         */
        primaryStage.setTitle(APPLICATION_TITLE);

        /*
            grid configuration
         */
        GRID.setAlignment(Pos.CENTER);
        GRID.setHgap(5);
        GRID.setVgap(8);
        GRID.setPadding(new Insets(5, 5, 5, 5));

        /*
            List names and quantity of available theme gzip files 
            that are linked by the selected web page.
         */
        final Tooltip QTY_NAMES_ONLY_TT = new Tooltip();
        QTY_NAMES_ONLY_TT.setText(
            "Get a list and quantity of themes\n"
          + "referenced by the web page without\n"
          + "downloading any."
        );
        QTY_NAMES_ONLY_BTN.setTooltip(QTY_NAMES_ONLY_TT);
        QTY_NAMES_ONLY_BTN.setPrefWidth(175);
        QTY_NAMES_ONLY_BTN.setOnAction((ActionEvent event) -> {
            
            /*
                Fetch files, file names only.
            */
            fetch = new Fetch(Boolean.TRUE);
            fetch.start();
        });
        //
        GRID.add(QTY_NAMES_ONLY_BTN, 0, 0);

        /*
            Two text fields are to be displayed side by side:
            THEME_FILE_NAME_LBL and QUANTITY_LBL
            so we add them to a HBox (horizontal box)
            and then add the HBox to a grid location on the GUI (0, 1).
         */
        THEME_NAMES_AND_QTY_HBOX.setAlignment(Pos.CENTER_LEFT);
        THEME_NAMES_AND_QTY_HBOX.getChildren().add(THEME_FILE_NAME_LBL);
        THEME_NAMES_AND_QTY_HBOX.getChildren().add(QUANTITY_LBL);
        //
        GRID.add(THEME_NAMES_AND_QTY_HBOX, 0, 1);

        /*
            A TextArea is useful for displaying plain, unformatted text.
            This is the graphical object that will display a list of gzip files.
        
            Make the TextArea read only.
        
            We set its preferred width and add it to the GUI at location 0, 2.
         */
        THEME_FILE_NAMES_TA.setPromptText("List of available\rtheme files."); 
        THEME_FILE_NAMES_TA.setMaxWidth(240);
        THEME_FILE_NAMES_TA.setEditable(false);
        GRID.add(THEME_FILE_NAMES_TA, 0, 2);

        /*
            A a label another label stating what the web location text field
            is for at GUI location 0, 4.
        
            We use 4 instead of 3 to add a bigger space between the GUI objects
            above
         */
        GRID.add(WEB_PAGE_WITH_LIST_OF_FILES_LBL, 0, 4);

        /*
            Set the preferred size of the web location text field.
            Then add it to the GUI at location 0, 5 and force it
            to span across two columns and one row.
         */
        final Tooltip WEBPAGE_TT = new Tooltip();
        WEBPAGE_TT.setText(
            "Web page listing .tar.gz files."
        );
        WEB_PAGE_URL_TF.setTooltip(WEBPAGE_TT); 
        WEB_PAGE_URL_TF.setPromptText("http://www.muhri.net/gkrellm/nav.php3?node=gkrellmall&sort=added&conf=DESC"); 
        WEB_PAGE_URL_TF.setPrefWidth(590);

        /*
            location: 0, 5
            span: 2 columns
                  1 row
         */
        GRID.add(WEB_PAGE_URL_TF, 0, 5, 2, 1);

        final Tooltip STOP_TT = new Tooltip();
        STOP_TT.setText(
            "Cancel the downloading process."
        );
        STOP_BTN.setTooltip(STOP_TT);
        STOP_BTN.setPrefWidth(175);
        STOP_BTN.setDisable(true);
        STOP_BTN.setOnAction((ActionEvent event) -> {
            
            /*
                Fetch files, file names only.
            */
            fetch.stopKeepGoing();
        });
        //
        GRID.add(STOP_BTN, 0, 6);

        /*
            Set the preferred size of the button that will do the downloading.
            Add it to the GUI at location: 1, 0
         */
        final Tooltip DOWNLOAD_THEMES_TT = new Tooltip();
        DOWNLOAD_THEMES_TT.setText(
            "Download either 'only get' themes\n"
          + "or all themes referenced by\n"
          + "the web page (default behavior)."
        );
        DOWNLOAD_THEMES_BTN.setTooltip(DOWNLOAD_THEMES_TT);
        DOWNLOAD_THEMES_BTN.setPrefWidth(175);
        DOWNLOAD_THEMES_BTN.setOnAction((ActionEvent event) -> {
            
            /*
                We do this otherwise the download location text field will get
                the focus and it looks odd.
            */
            THEME_FILE_NAMES_TA.requestFocus();
            
            /*
                Download files.
            */
            fetch = new Fetch(Boolean.FALSE);
            fetch.start();
        });
        //
        GRID.add(DOWNLOAD_THEMES_BTN, 1, 0);

        /*
            Add a label stating what the download location text field is for
            and add it to the GUI at location: 1, 1
         */
        GRID.add(WHERE_TO_DOWNLOAD_LBL, 1, 1);

        final Tooltip CHOOSE_TT = new Tooltip();
        CHOOSE_TT.setText(
            "Select a download location. Recommend:\n"
            + System.getProperty("user.home") + File.separator + ".gkrellm2" + File.separator + "themes"
        );
        CHOOSE_BTN.setTooltip(CHOOSE_TT); 
        CHOOSE_BTN.setPrefWidth(75);
        CHOOSE_BTN.setOnAction((ActionEvent event) -> {
            
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Theme Download Location");
            directoryChooser.setInitialDirectory(new File (System.getProperty("user.home")));
            File dir = directoryChooser.showDialog(primaryStage);
            if (dir != null) DOWNLOAD_LOCATION_TF.setText(dir.getAbsolutePath());
        });
        
        final Tooltip DOWNLOAD_LOCATION_TT = new Tooltip();
        DOWNLOAD_LOCATION_TT.setText(
                "Download location.\n"
                + "Click the Choose button.\n"
                + "Recommend:\n"
                + System.getProperty("user.home") + File.separator + ".gkrellm2" + File.separator + "themes"
        );
        DOWNLOAD_LOCATION_TF.setTooltip(DOWNLOAD_LOCATION_TT);
        DOWNLOAD_LOCATION_TF.setEditable(false);
//        DOWNLOAD_LOCATION_TF.setPromptText (System.getProperty("user.home") + File.separator + ".gkrellm2" + File.separator + "themes"); 
        DOWNLOAD_LOCATION_TF.setPrefWidth(270);
        DOWNLOAD_LOCATION_HBOX.setAlignment(Pos.CENTER);
        DOWNLOAD_LOCATION_HBOX.getChildren().add(DOWNLOAD_LOCATION_TF);
        DOWNLOAD_LOCATION_HBOX.getChildren().add(CHOOSE_BTN);
        
        /*
            We want the download text field to appear top left, under its
            description label so we add it to a VBox (vertical box) and 
            set the alignment of the VBox to top left. Then we add the VBox
            to the GUI at: 1, 2
         */
        DOWNLOAD_LOAD_LOCATION_VBOX.setAlignment(Pos.TOP_LEFT);
        DOWNLOAD_LOAD_LOCATION_VBOX.getChildren().add(DOWNLOAD_LOCATION_HBOX);

        final Tooltip CB_TT = new Tooltip();
        CB_TT.setText(
            "When selected, only those themes\n"
          + "listed here will be downloaded."
        );
        ONLY_GET_CB.setTooltip(CB_TT); 
        ONLY_GET_CB.setOnAction((ActionEvent event) -> {
            
            ONLY_GET_TF.setDisable(!ONLY_GET_CB.isSelected());
        });

        INDIVIDUAL_FILES_DOWNLOAD_HBOX.setAlignment(Pos.CENTER);
        INDIVIDUAL_FILES_DOWNLOAD_HBOX.getChildren().add(ONLY_GET_CB);
        final Tooltip ONLY_GET_TT = new Tooltip();
        ONLY_GET_TT.setText(
            "To download specific themes, type\n"
          + "their file names here, separated\n"
          + "by a space."
        );
        ONLY_GET_TF.setTooltip(ONLY_GET_TT);
        ONLY_GET_TF.setPromptText("theme1.tar.gz theme2.tar.gz"); 
        ONLY_GET_TF.setPrefWidth(250);
        ONLY_GET_TF.setDisable(true);
        INDIVIDUAL_FILES_DOWNLOAD_HBOX.getChildren().add(ONLY_GET_TF);
        DOWNLOAD_LOAD_LOCATION_VBOX.getChildren().add(INDIVIDUAL_FILES_DOWNLOAD_HBOX);

        final Tooltip DOWNLOAD_PROGRESS_TT = new Tooltip();
        DOWNLOAD_PROGRESS_TT.setText(
            "Download progress."
        );
        DOWNLOAD_PROGRESS_PB.setTooltip(DOWNLOAD_PROGRESS_TT);
        DOWNLOAD_PROGRESS_PB.setPrefWidth(350);
        DOWNLOAD_PROGRESS_PB.setPrefHeight(50);
        DOWNLOAD_PROGRESS_PB.setProgress(0);
        DOWNLOAD_LOAD_LOCATION_VBOX.getChildren().add(DOWNLOAD_PROGRESS_PB);
        GRID.add(DOWNLOAD_LOAD_LOCATION_VBOX, 1, 2);

        final Tooltip QUIT_TT = new Tooltip();
        QUIT_TT.setText(
            "Stop all downloads and close\n"
          + "this application."
        );
        QUIT_BTN.setTooltip(QUIT_TT); 
        QUIT_BTN.setPrefWidth(175);
        QUIT_BTN.setOnAction((ActionEvent event) -> {
            
            /*
                Fetch files, file names only.
            */
            if (fetch != null) {
            
                fetch.stopKeepGoing();
            }
            
            ALERT.setAlertType(AlertType.CONFIRMATION);
            ALERT.setTitle("Confirm message");
            ALERT.setContentText("Please confirm close");
            ALERT.showAndWait().ifPresent(response -> {
                
                if (response == ButtonType.OK) {
                    
                    System.exit(0);
                }
            });
        });
        //
        GRID.add(QUIT_BTN, 1, 6);
        
        /*
            Adds the css styling to alert messages (css .dialog-pane).
        */
        DialogPane dialogPane = ALERT.getDialogPane();
            dialogPane.getStylesheets().add(
               getClass().getResource("gui.css").toExternalForm());

        /*
            Set the size of the application.
         */
        Scene scene = new Scene(GRID, 600, 330);
        scene.getStylesheets().add(Gui.class.getResource("gui.css").toExternalForm());

        primaryStage.getIcons().add(new Image(Gui.class.getResourceAsStream( "icon64.png")));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class Fetch extends Thread {

        final Boolean LIST_ONLY;

        public Fetch(Boolean b) {

            LIST_ONLY = b;
        }

        public void stopKeepGoing() {

            HtmlSplitter.setKeepGoing(Boolean.FALSE);
        }

        @Override
        public void run() {

            QTY_NAMES_ONLY_BTN.setDisable(true);
            DOWNLOAD_THEMES_BTN.setDisable(true);
            CHOOSE_BTN.setDisable(true);
            STOP_BTN.setDisable(false);
            //
            Platform.runLater(() -> {

                QUANTITY_LBL.setText("Qty: ");
            });

            HtmlSplitter.setKeepGoing(Boolean.TRUE);

            if (LIST_ONLY) {

                THEME_FILE_NAMES_TA.clear();

                final StringBuilder FILENAMES_SB = new StringBuilder();

//                for (String s : HtmlSplitter.getThemeGzipFilenames(WEB_PAGE_URL_TF.getText())) {
                for (String s : HtmlSplitter.getFileNamesFromWeb(WEB_PAGE_URL_TF.getText(), Boolean.FALSE)) { 

                    FILENAMES_SB.append(s);
                    FILENAMES_SB.append("\n");
                }

                THEME_FILE_NAMES_TA.setText(FILENAMES_SB.toString());

                Platform.runLater(() -> {

                    QUANTITY_LBL.setText("Qty: " + HtmlSplitter.getQty());
                });
            } 
            else {

                /*
                    Test whether download location exists.
                */
                if (!DOWNLOAD_LOCATION_TF.getText().trim().isEmpty()
                    && new File (DOWNLOAD_LOCATION_TF.getText().trim()).exists()) {
                
                    /*
                        User has chosen to type in list of files to downloaded.
                    */
                    if (ONLY_GET_CB.isSelected()
                        && !FileNameValidator.getBadGzipTarFileNames (ONLY_GET_TF.getText().trim()).isEmpty()) {

                        StringBuilder msg = new StringBuilder ();

                        for (Enumeration<String> e = BAD_FILE_NAMES_HM.keys(); e.hasMoreElements(); ) {

                            final String KEY = e.nextElement();

                            switch (BAD_FILE_NAMES_HM.get(KEY)) {

                                case MISSING_TAR_GZ:

                                    msg.append (KEY);
                                    msg.append ("\n\t :: missing .tar.gz");
                                    msg.append ("\n");

                                    break;

                                case ILLEGAL_FILENAME_CHARACTER:

                                    msg.append (KEY);
                                    msg.append ("\n\t :: contains illegal characters");
                                    msg.append ("\n");

                                    break;
                            }
                        }

                        ALERT.setAlertType(AlertType.ERROR);
                        ALERT.setTitle("Error message");
                        ALERT.setContentText(msg + "\n"
                                             + "Type a list of individual .tar.gz file names.\n\n"
                                             + "Valid file names:\n\n"
                                             + "end with: .tar.gz\n" 
                                             + "use alpha numeric characters not: !£$# etc\n" 
                                             + "can use _ (underscores)\n" 
                                             + "can use - (minus symbols)\n" 
                                             + "can use . (full stops)\n\n"
                                             + "separate file names with a space e.g.\n\n"
                                             + "CaiusWM.tar.gz Cheese.tar.gz");

                        Platform.runLater(() -> {

                            ALERT.show();
                        });
                    }

                    else  {

                        if (!HtmlSplitter.downloadAllThemes(Boolean.TRUE).equals(DOWNLOAD_STATUS.DOWNLOAD_GOOD)) {
                            
                            Platform.runLater(() -> {

                                ALERT.setAlertType(AlertType.ERROR);
                                ALERT.setTitle("Error message");
                                ALERT.setContentText("There was a problem connecting to the web page.");
                                ALERT.show();
                            });
                        }
                    }
                }
                else {
                    
                    Platform.runLater(() -> {

                        ALERT.setAlertType(AlertType.ERROR);
                        ALERT.setTitle("Error message");
                        ALERT.setContentText("Please click the Choose button\n"
                                + "and select a download location.\n\n"
                                + "Recommend:\n"
                                + System.getProperty("user.home") + File.separator + ".gkrellm2" + File.separator + "themes");
                        ALERT.show();
                    });
                }
            }

            QTY_NAMES_ONLY_BTN.setDisable(false);
            DOWNLOAD_THEMES_BTN.setDisable(false);
            CHOOSE_BTN.setDisable(false);
            STOP_BTN.setDisable(true);
            
            USER_LIST_OF_FILES_AL.clear();
        }
    }
}
