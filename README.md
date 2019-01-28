AIS: iText
============

Java source code and command line tool to sign PDF with iText.

### Usage

````
Usage: com.swisscom.ais.itext.SignPDF [OPTIONS]

OPTIONS

  -infile=VALUE           - Source Filename, PDF to be signed
  -outfile=VALUE          - Target Filename, signed PDF

  ### TIMESTAMP SIGNATURES ###
  -type=timestamp         - Signature Type RFC 3161

  ### SIGNATURES WITH STATIC CERTIFICATES ###
  -type=sign              - Signature Type RFC 3369

  ### SIGNATURES WITH ON DEMAND CERTIFICATES ###
  -type=sign              - Signature Type RFC 3369
  -dn=VALUE               - Subject Distinguished Name for the On Demand Certificate
                            Supported attributes, separated by a comma:
                            [mandatory]
                             - cn or CommonName
                             - c or CountryName
                            [optional]
                             - EmailAddress
                             - FivenName
                             - l or LocalityName
                             - ou or OrganizationalUnitName
                             - o or OrganizationName
                             - SerialNumber
                             - st or StateOrProvinceName
                             - sn or Surname
  Optional Step-Up Authorization (Declaration of Will):
  -stepUpMsisdn=VALUE        - Phone number (requires -dn -stepUpMsg -stepUpLang)
  -stepUpMsg=VALUE           - Message to be displayed (requires -dn -stepUpMsisdn -stepUpLang)
                            A placeholder #TRANSID# may be used anywhere in the message to include a unique transaction id
  -stepUpLang=VALUE          - Language of the message to be displayed (requires -dn -stepUpMsisdn -stepUpMsg)
                            supported values:
                             - en (english)
                             - de (deutsch)
                             - fr (français)
                             - it (italiano)
  -stepUpSerialNumber=VALUE  - Optional: Verify the MobileID / PwdOTP SerialNumber (16 chars; starting with 'MIDCHE' or 'SAS01')
                            Document will only be signed if it matched the actual SerialNumber                        

  ### ADOBE PDF SETTINGS ###
  -reason=VALUE           - Signing Reason
  -location=VALUE         - Signing Location
  -contact=VALUE          - Signing Contact
  -certlevel=VALUE        - Certify the PDF, at most one certification per PDF is allowed
                             Supported values:
                             - 1 (no further changes allowed)
                             - 2 (form filling and further signing allowed)
                             - 3 (form filling, annotations and further signing allowed)

  ### DEBUG OPTIONS ###
  -v                      - Verbose output
  -vv                     - More Verbose output
  -config=VALUE           - Custom path to the properties file (signpdf.properties)

EXAMPLES

  [timestamp]
    java com.swisscom.ais.itext.SignPDF -type=timestamp -infile=sample.pdf -outfile=signed.pdf
    java com.swisscom.ais.itext.SignPDF -v -type=timestamp -infile=sample.pdf -outfile=signed.pdf

  [sign with static certificate]
    java com.swisscom.ais.itext.SignPDF -type=sign -infile=sample.pdf -outfile=signed.pdf
    java com.swisscom.ais.itext.SignPDF -v -config=/tmp/signpdf.properties -type=sign -infile=sample.pdf -outfile=signed.pdf -reason=Approved -location=Berne -contact=alice@acme.com

  [sign with on demand certificate]
    java com.swisscom.ais.itext.SignPDF -type=sign -infile=sample.pdf -outfile=signed.pdf -dn='cn=Alice Smith,c=CH'

  [sign with on demand certificate and mobile id authorization]
    java com.swisscom.ais.itext.SignPDF -v -type=sign -infile=sample.pdf -outfile=signed.pdf -dn='cn=Alice Smith,c=CH' -stepUpMsisdn=41792080350 -stepUpMsg='acme.com: Sign the PDF? (#TRANSID#)' -stepUpLang=en
    java com.swisscom.ais.itext.SignPDF -v -type=sign -infile=sample.pdf -outfile=signed.pdf -dn='cn=Alice Smith,c=CH' -stepUpMsisdn=41792080350 -stepUpMsg='acme.com: Sign the PDF? (#TRANSID#)' -stepUpLang=en -stepUpSerialNumber=MIDCHE2EG8NAWUB3
````   

#### Dependencies

This java application has external dependencies (libraries). They are located in the `./lib` subfolder.
The latest version may be downloaded from the following source:

1: http://mvnrepository.com/artifact/com.google.code.findbugs/jsr305

Version 2.0.2 has been successfully tested

2: http://sourceforge.net/projects/itext

Version 5.4.5 has been successfully tested

3: http://www.bouncycastle.org/latest_releases.html

bcprov-jdk15on-150.jar has been successfully tested
bcpkix-jdk15on-150.jar has been successfully tested

#### Paths & Placeholders

The following placeholder will be used in this README (see sections below)
```
<JAR>   = Path to the ./jar subfolder containing the latest Java Archive
<SRC>   = Path to the ./src subfolder containing the *.java source files
<LIB>   = Path to the ./lib subfolder containing the libraries
<CLASS> = Path to the directory where class files will be created
<CFG>   = Path to the signpdf.properties file
<DOC>   = Path to the ./doc subfolder containing the JavaDoc
```

#### Configuration

Refer to `signpdf.properties` configuration file and modify the configuration properties accordingly.

#### Run the JAR archive

You may use the latest Java Archive (JAR) `signpdf-x.y.z.jar` located in the `./jar` subfolder.

Run the JAR (Unix/OSX): `java -cp "<JAR>/signpdf-x.y.z.jar:<LIB>/*" com.swisscom.ais.itext.SignPDF`

Run the JAR (Unix/OSX) with custom path to the properties file:
`java -DpropertyFile.path=<CFG> -cp "<JAR>/signpdf-x.y.z.jar:<LIB>/*" com.swisscom.ais.itext.SignPDF`

Run the JAR (Unix/OSX) with DEBUG enabled:
`java -Djavax.net.debug=all -Djava.security.debug=certpath -cp "<JAR>/signpdf.jar:<LIB>/*" com.swisscom.ais.itext.SignPDF`

Create the latest JAR: `jar cfe <JAR>/signpdf-x.y.z.jar com.swisscom.ais.itext.SignPDF -C <CLASS> .`

If you're on Windows then use a semicolon ; instead of the colon : 

#### Compile & Run the Java Classes

The source files can be compiled as follows. 

Compile the sources: `javac -d <CLASS> -cp "<LIB>/*" <SRC>/*.java`

Note: The class files are generated in a directory hierarchy which reflects the given package structure: `<CLASS>/swisscom/com/ais/itext/*.class`

The compiled application can be run as follows.

Run the application (Unix/OSX):
`java -cp "<CLASS>:<LIB>/*" com.swisscom.ais.itext.SignPDF`

Run the application (Unix/OSX) with custom path to the properties file:
`java -DpropertyFile.path=<CFG> -cp "<CLASS>:<LIB>/*" com.swisscom.ais.itext.SignPDF`

Run the application (Unix/OSX) with DEBUG enabled:
`java -Djavax.net.debug=all -Djava.security.debug=certpath -cp "<CLASS>:<LIB>/*" com.swisscom.ais.itext.SignPDF`

If you're on Windows then use a semicolon ; instead of the colon : 

#### JavaDoc

The latest JavaDoc is located in the `./doc` subfolder.

Create the latest JavaDoc: `javadoc -windowtitle "Swisscom All-in Signing Service vx.y.z" -doctitle "<h1>Swisscom All-in Signing Service vx.y.z</h1>" -footer "Swisscom All-in Signing Service vx.y.z" -d <DOC> -private -sourcepath <SRC> com.swisscom.ais.itext`

#### Certificate Handling

PKCS12 certificate file consisting of public certificate and private key.

Extraction: 
1. Extract public client certificate:

   `openssl pkcs12 -in <yourPKCS12>.p12 -clcerts -nokeys | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > public.crt`

2. Extract password protected private key

   `openssl pkcs12 -in <yourPKCS12>.p12 -nocerts -out encpriv.key`

3. Extract decrypted private key

   `openssl rsa -in encpriv.key -out decpriv.key`