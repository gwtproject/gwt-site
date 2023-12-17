# Communication, Validation & more ... 

GWT comes with a variate of frameworks to communicate with the server.
Take a look at [Ajax Communication](/doc/latest/tutorial/clientserver.html) for more information about modules GWT offers.
Besides that, there are many useful libraries.

Check these:

## Client-Server-Communication

Besides the [communication to the server provided by GWT](/doc/latest/tutorial/clientserver.html) 
there are alternative implementations, too.

### domino-rest<a id="domino-rest"></a>

Domino-rest is a lib for generating rest clients from JaxRs compatible interfaces, and the generated clients can be used 
from both client side -browser- using GWT and on the server. The lib works with GWT2 and also with GWT3/J2CL. The 
serialization and deserialization is based on domino-jackson and service definition is based on JaxRs annotations, while 
code generation uses annotation processing APT.

For more information see: [domino-rest at GitHub](https://dominokit.com/solutions/domino-rest/v1) and [domino-jackson at GitHub](https://dominokit.com/solutions/domino-jackson/v1)

## Validation<a id="validation"></a>

There are several implementation to validate data in GWT:

### Iban4g - IBAN-Validation<a id="iban4g"></a>

A Java, GWT and J2CL ready library for generation and validation of the International Bank Account Numbers (IBAN ISO_13616)
and Business Identifier Codes (BIC ISO_9362). The library can be used on the client and server side.

For more information, see: [iban4g at GitHub](https://github.com/NaluKit/iban4g)

### Malio - POJO-Validation<a id="malio"></a>

Malio is a tiny framework to validate POJOs using annotations. It is easy to use by just adding annotations to members 
of a POJO. No writing of validations or validators. Based on the annotations inside the POJO, the processor generates
a validator. The generated validator can be used to check or validate the POJO. Malio valdators can bes used in GWT, J2CL
and Java.

For more information, see: [malio at GitHub](https://github.com/NaluKit/malio)

### gwt-bean-validators<a id="wt-bean-validators"></a>

Implementation of Bean Validation (JSR-303/JSR-349/JSR-380) for GWT as replacement of deprecated GWT internal validation
implementation.

For more information, see: [gwt-bean-validators at GitLab](https://gitlab.com/ManfredTremmel/gwt-bean-validators)

## Support

The GWT project team does not support this projects. In case you have questions or would like to open an issue, please
contact the contributor of the project!

## Missing Something?

If you are missing a library or a framework from the list, please follow the instructions [here](add-lib.html) to add it.