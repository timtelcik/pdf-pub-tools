# PDF Publishing Tools - Usage
 
 * Created: 20-Nov-2010, Tim Telcik <tim.telcik@permeance.com.au>
 * Modified: 30-Nov-2010, Tim Telcik <tim.telcik@permeance.com.au>
 * Modified: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
 * Modified: 4-Mar-2011, Tim Telcik <telcik@gmail.com>
 * Modified: 20-Jul-2011, Tim Telcik <telcik@gmail.com>


## Tools

### Command Line Interfaces (CLI)

#### BookPublisherCLI

purpose: creates final PDF book
class: net.mitnet.tools.pdf.book.publisher.ui.cli.BookPublisherCLI
usage: BookPublisherCLI -v -indir <input-dir> -outdir <output-dir> -outbook <output-pdf-book>

#### OpenOfficeDocConverterCLI

purpose: converts ODT to PDF (by default)
class net.mitnet.tools.pdf.openoffice.ui.cli.OpenOfficeDocConverterCLI
usage: OpenOfficeDocConverterCLI -v -indir <input-dir> -outdir <output-dir>

#### PdfBookBuilderCLI

purpose: builds PDF book from PDF files
net.mitnet.tools.pdf.pdf.builder.ui.cli.PdfBookBuilderCLI
usage: PdfBookBuilderCLI -v -indir <input-dir> -outbook <output-pdf-book-file>


### Graphical User Interface (GUI)

#### BookPublisherGUI

purpose: creates PDFs from ODT only at this stage; a bit more work to make this equivalent to BookPublisherCLI
class: net.mitnet.tools.pdf.book.publisher.ui.gui.BookPublisherGUI
usage: launch from Eclipse
