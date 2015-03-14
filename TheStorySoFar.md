# The Story So Far #

The PDF book publisher tool suite contains a number of tools, one of which ... the Book Publisher ... converts a folder conaining Open Office documents (ODT,ODP) into PDF docs.

The translation is handled by running [Open Office](http://www.openoffice.org/) or [Libre Office](http://www.libreoffice.org/) in "server mode" and uploading each file to the Open Office server using the [JOD Converter API](http://www.artofsolving.com/opensource/jodconverter/).

The Book Publisher also assembles the PDF docs into a 2-up PDF booklet using [iText](http://itextpdf.com/) with page numbers.

There are many outstanding [TODO](TODO.md) items.