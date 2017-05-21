# GkrellmThemeDownloader

WHY

From the first time I used GKrellM I was blown away but what I enjoyed most was its ability to be themed/skinned. A bit involved but once you know how it's easy yet a bit fiddly; my opinion.


SO GIVE US A SOLUTION

Gkrellm Theme Downloader scrapes .tar.gz hrefs from a web page listing clickable screenshots of GKrellM themes:

- downloads each .tar.gz file
- un-compresses and un-tars the .tar.gz file

This application is a JavaFX application, which, for our purposes, means the GUI can be skinned in various ways. In this case I used a Cascading Style Sheet, a .css file, similar to what we would use for skinning web pages. The engine of the application is Java 8.


PLANS

A configuration file.
  - store download location
  - store web page URL

Option to backup and restore .gkrellm2 directory. Although a home directory back procedure aught to be in place anyway.

Select individual themes. At the moment the user can type in space separated theme file names and just download those but it would be more interesting to list the file names and allow the user to select the ones they want to download.

This process would include a screenshot of the theme to help the user decide. Links to screenshots are also listed on the same web from which we scrape the .tar.gz file names and hrefs.

The GUI has a default selection colour, stock light blue, this should be a brown-red-orange-yellowish colour.

Make the application less brittle. There are too many assumptions made around the current web page URL and no chance that the application will work if another URL is used.


REQUIREMENTS

Linux

Java 8

  http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html

  http://www.webupd8.org/2014/03/how-to-install-oracle-java-8-in-debian.html


WHAT IS IT

This application downloads all Gkrellm theme files, .tar.gz files listed on a web page and unpacks them.

The recommended download location is:
~/.gkrellm2/themes


HOW TO

To download and unpack all Gkrellm theme files listed on the www.muhri.net web page:

1. Click the 'Choose' button and select a directory where unpacked
   themes should reside.

   - You may want to choose a temporary location just to test.

   - Otherwise Gkrellm expects to find themes in: ~/.gkrellm2/themes

2. Click the 'Download themes' button.

   - File names will appear on the right as they are downloaded.

   - The Qty: field will tell you how many files are being downloaded.

   - The progress bar will indicate completion time.

   - Complete downloading of all themes will take about 1 or 2 minutes.

3. To theme Gkrellm:

   - Right click Gkrellm and select: Themes

G+ community:
https://plus.google.com/u/0/communities/115910376404287329970

