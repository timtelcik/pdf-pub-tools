# PDF Publishing Tools - How To Run

 * Created: 11-Feb-2012, Rich Sezov <sezovr@gmail.com>
 * Modified: 15-Feb-2012, Tim Telcik <telcik@gmail.com>


## Command Line

### Start Open Office

start Open Office on localhost loopback (127.0.0.1) to listen on port 8100.

e.g.

```
$ soffice "-accept=socket,host=127.0.0.1,port=8100,tcpNoDelay=1;urp;" -headless -nodefault -nofirststartwizard -nolockcheck -nologo -norestore
```


### Run Book Publisher Command Line Tool

e.g.

```
$ ./bin/run-book-publisher-cli.sh -v -indir input-folder -outdir output-folder -outbook outbook -ps A4
```
