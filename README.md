hsts-hook
=========

This hook adds the HSTS header (RFC-6797) to https-responses.
More information about HSTS (HTTP Strict Transport Security) can be
found here:

* https://tools.ietf.org/html/rfc6797
* http://en.wikipedia.org/wiki/HTTP_Strict_Transport_Security
* https://www.owasp.org/index.php/HTTP_Strict_Transport_Security

While it's possible to configure frontend webservers to add this
header automatically, this solves the problem on the application
server side: If a request is received through https, the header
will be sent back. Without any configuration - to minimize damage -
the instruction will be valid for 30 seconds. If you're sure to want
to send this header, the plugin can be configured through 
portal-ext.properties.

Just add this section and restart:

      # HTTP Strict Transport Security
      # specify the max-age, until the HSTS header is valid, 
      # in seconds. E.g. one year is 31536000 seconds
      hsts-max-age=31536000

To deactivate this header, undeploy the plugin (it does nothing but 
add the header) or just minimize the impact by specifying the 
max-age as 0

Note, due to the specification, this can only work properly under the 
following circumstances:

* Your server has a properly trusted - not a self-signed - certificate
* You access your site through https (HSTS does not work on http)
* You're running on the standard ports, 80 for http and 443 for https, e.g.
  HSTS would happily rewrite http://localhost:8080 to 
  https://localhost:8080 - and this obviously can't work.

