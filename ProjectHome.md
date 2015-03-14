# Overview #

This project contains a collection of PDF publishing tools and helpers :

  * Book Publisher
  * PDF Book Builder
  * Open Office Doc Converter
  * Open Office Report Builder

See [GettingStarted](GettingStarted.md), [Usage](Usage.md) and [References](References.md).

## PDF Publishing Tools ##

### Book Publisher ###

The primary publishing tool is the Book Publisher, which creates a PDF book from a collection of Open Office documents (odt,odp) and/or PDF docs (pdf) and assembles them into a 2-up book with page numbers and a Table of Contents (TOC).

### PDF Book Builder ###

This tool assembles a collection of PDF documents into a 2-up book.

### Open Office Doc Converter ###

This helper is a simple facade for [JODConverter](http://code.google.com/p/jodconverter/).

### Open Office Report Builder ###

This helper is a simple facade for [JODReports](http://jodreports.sourceforge.net/).

# Dependencies #

To build the tools, you will require JDK 1.5+.

To run the tools, you will required JRE 1.5+.

This project depends upon several key third-party libraries, including:

  * [JODConverter](http://code.google.com/p/jodconverter/)

  * [JODReports](http://jodreports.sourceforge.net/)

  * [iText](http://itextpdf.com/)

The Book Publisher is dependent at runtime upon Open Office running in "headless" mode (i.e. without a GUI). For more details on running in "headless" mode , see these links:

  * [Open Office](http://www.openoffice.org/)

  * [Using Open Office headless](http://www.oooforum.org/forum/viewtopic.phtml?t=11890)

The JODConverter library provides an interface to Open Office for converting between document formats (i.e. ODT or ODP to PDF).

# License #

This project is licensed according to the [GNU General Public License](http://www.gnu.org/copyleft/gpl.html) at http://www.gnu.org/copyleft/gpl.html.