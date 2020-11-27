Late edit in 2020:

It's 2020, and I figured that unconditional HTTPS can safely be assumed. David Nebinger didn't think so and updated this plugin to Liferay 7.x - find [his forward-port here](https://github.com/dnebing/hsts). *This* repository won't be brought forward to 7.x, but rather stay as is.


hsts-hook
=========

This hook adds the HSTS header (RFC-6797) to https-responses.
More information about HSTS (HTTP Strict Transport Security) can be
found here:

* https://tools.ietf.org/html/rfc6797
* http://en.wikipedia.org/wiki/HTTP_Strict_Transport_Security
* https://www.owasp.org/index.php/HTTP_Strict_Transport_Security

While it's possible to configure frontend webservers to add this
header automatically, this app enables you to conditionally handle
anonymous users differently from logged in users: You can configure
the timeout for both kinds of users differently, e.g. to totally 
disable HSTS for anonymous users, while enforcing it for logged in 
users. 

Due to the nature of HSTS, this is browser based: Whenever a user 
logs on on a specific browser, that browser is forced into https 
in future, no matter if the user ever logs in again. The assumption
is that whatever browser is used to log in to the system, it might 
be used to log in again. If nobody ever logged in from a certain 
browser and your site is public, you don't need to force them into 
https.

If a request is received through https, the header
will be sent back. You will need to configure the timeout values 
in *Control Panel / Portal Settings / General*.
The timeout is configured in seconds, i.e. one year (365 days) is 
equivalent to 31536000 seconds

To deactivate this header, undeploy the plugin (it does nothing but 
add the header) or just minimize the impact by specifying the 
timeout as 0.

Note, due to the specification, this can only work properly under the 
following circumstances:

* Your server has a properly trusted - not a self-signed - certificate.
* You access your site through https. (HSTS does not work on http)
* You're running on the standard ports, 80 for http and 443 for https, e.g.
  HSTS would happily rewrite http://localhost:8080 to 
  https://localhost:8080 - and this obviously can't work.

