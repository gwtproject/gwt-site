UiImageBundles
===

An _image bundle_ is a construct used to improve application performance by reducing the number of round trip HTTP requests to the server to fetch images. GWT can package
many image files into a single large file to be downloaded from the server and managed as a Java object.

## ClientBundles Make Using Images More Efficient

Typically, an application uses many small images for icons. In HTML, each image is stored in a separate file and the browser is asked to download each file from the web server
as a separate HTTP transaction. This standard way of dealing with images can be wasteful in several ways:

*   **Large overhead:** In a standard web application, an HTTP request has to be sent to the server for each image. In many cases, those images are icons and the
actual image size is very small. In that case, the size of the image is often smaller than the HTTP response header that is sent back with the image data. That means that most of
the traffic is overhead and very little of it actual content.

*   **Useless freshness checks:** Traditional image handling is wasteful in other ways too. Even when the images have been cached by the client, a 304 ("Not
Modified") request is still sent to check and see if the image has changed. Since images change infrequently, these freshness checks are also wasteful.

*   **Blocking HTTP connections:** Furthermore, HTTP 1.1 requires browsers to limit the number of outgoing HTTP connections to two per domain/port. A multitude of
image requests will tie up the browser's available connections, which blocks the application's RPC requests. In most applications, RPC requests are the real work that the
application needs to do.

The end result of sending out many separate requests and freshness checks is slow application startup.

The GWT [ImageResource](DevGuideClientBundle.html#ImageResource) solves these problems. Multiple `ImageResources` are declared in a single `ClientBundle`, which is a composition of many images into a single image, along with an interface for accessing the individual
images from within the composite. Users can define a `ClientBundle` that contains the images used by their application, and GWT will automatically create the composite image and
provide an implementation of the interface for accessing each individual image. Instead of a round trip to the server for each image, only one round trip to the server for the
composite image is needed.

Because the filename of the composite image is based on a hash of the file's contents, the filename will change only if the composite image is changed. This means that it is
safe for clients to cache the composite image permanently, which avoids the unnecessary freshness checks for unchanged images. To make this work, the server configuration needs to
specify that composite images never expire. In addition to speeding up startup, image bundles prevent the "bouncy" effect of image loading in browsers. While images are loading,
browsers put a standard placeholder for each image in the UI. The placeholder is a standard size because the browser does not know what the size of an image is until it has been
fully downloaded from the server. The result is a 'bouncy' effect, where images 'pop' into the UI once they are downloaded. With image bundles, the size of each individual image
within the bundle is discovered when the bundle is created, so the size of the image can be explicitly set whenever images from a bundle are used in an application.

See the [ImageBundle
API documentation](/javadoc/latest/com/google/gwt/user/client/ui/ImageBundle.html) for important information regarding:

*   A potential security issue with the generation of the composite image on certain versions of the JVM
*   Caching recommendations for image bundle files
*   Protecting image bundle files with web application security constraints
*   Using image bundles with the HTTPS protocol
