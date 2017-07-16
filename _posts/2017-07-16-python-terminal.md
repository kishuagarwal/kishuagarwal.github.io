---
layout: post
title:  "Python & Terminal"
date:   2017-07-16 00:00:00 +0530
tags: terminal python utf-8
comments: true
meta:
    keywords: "python and terminal, python interpreter ascii, shell and terminal encoding, python stdin and stdout"
---
*(Note: All of these is related to Python 2. Python 3 may show different results.)*

While doing some office work, I came across this. 

I had written below line in my Python 2 interpreter.

{% highlight python %}
    >>> s = '©'
    >>> s #for checking the value of s
{% endhighlight %}

Surprisingly, I got the following value.

{% highlight python %}
    '\xc2\xa9'
{% endhighlight %}

The reason I was surprised, was because, © is a non-ASCII character and my Python interpreter has the default encoding of **ASCII**. So I was hoping that since **ASCII** can't encode
this value, it should throw me some kind of encode/decode error. But I was wrong. Instead I 
got the above value. But the fact that was even more suprising was the value that
was printed was none other than the **UTF-8** encoded value of the © character.

Just to be sure, I checked my Python default encoding.
{% highlight python %}
    >>> import sys
    >>> sys.getdefaultencoding()
    'ascii'
{% endhighlight %}

Yup. I was right. Default is **ASCII**. Then why it was printing the **UTF-8** encoding.

To find the reason for this strange result, I did some digging on internet. 

And finally I got the reason for this strange result.

The reason was my **TERMINAL**. I was using the GNOME terminal running **bash** shell. Guess what 
was the default encoding of the terminal? 

It was **UTF-8**.

But what terminal encoding has to do with Python? Well in this case, everything.

Following illustration would make things clear.

<div class="img_container">

<svg class="extend" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" content="&lt;mxfile userAgent=&quot;Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36&quot; version=&quot;6.8.15&quot; editor=&quot;www.draw.io&quot; type=&quot;google&quot;&gt;&lt;diagram id=&quot;bc040632-4c97-1c4d-cc18-ffc72b2cc837&quot; name=&quot;Page-1&quot;&gt;7Vlbb5swGP01qE+ZuITbY5um26RNmtRO2x4dcMAbsZlxbvv1+wzmFqCNkpJJzRKJ2MefjX2Oj22IZs1Wu/ccpfFnFuJEM/Vwp1n3mmlOHQOuEtgXgOnqBRBxEhaQUQOP5A9WYBm2JiHOWoGCsUSQtA0GjFIciBaGOGfbdtiSJe27pijCHeAxQEkX/UZCEReoZzo1/gGTKC7vbDh+UbJCZbAaSRajkG0bkDXXrBlnTBSp1W6GE8ldyUtR72GgtOoYx1QcU8GxTd9zFlPPRy62rXBiqDFkYl+OFocweJVlXMQsYhQl8xq942xNQyyb1CEXi1UCSQOS0Au+/67wPPNDZt7ZkP2JhdgrbdFaMIDq1j8xlqo2MsHZr4pniSwZFaqiA9miw7KXgyQoKGNrHqgoFyPL87yl5djecmH6E1tNJMQjLIbYmVYawdzGbIVhSFCJ4wQJsml3AKlZFlVxtRCQUFr06zLUuw1K1uouHZ1qFSRJ25gI/JiifLxbsGFbmYLVGUsYz2tbTv7p55skSSNyactvV4eIo5AA5Y3QRf6tNNpgLvDueZW65KoKtq88oxYNw1b5bW1BQ1e0xA37lXHn6NGZCdaV2KQzcLNrk/7JegGXDHVuRJcEHl4sj3FJiLC3DIZkOMsJU+MYJ1gXcsK0h3MnEWrkLfKd32tWFkyynJNbCDD0dFcXQiqSvxmU5LX0G82e7QJTXpF/U7YOHS1uUIR3hAZ+RVtNjuGeaJEHSKeljFCRU2HfafY9INJgRb/yCighEYV0gpeyKSkagVPArYKFNN9dBrOH0OgpdyKwMYbidlmlUtzuKF6eoZqCG2MI7nQEn9OAhcBBKU1INqUsX58eJl5DskbRm1Zs6r3s0WmPYuYYinlnW9TrdagA0a/SjodHEbcrrnUpO/ojrb9f9nDKAHn1j1RgnnIM12sU29GP2W0vpXbZ6BlyuwNeZmtxur4j8O4e8O509zyzbwW1x6DdGmcJrZGUk6KdVtD1ua18ifLM0tq3b45jtuGzLXBBGyK+wnoLv2glaT4ip2szU7v1BydIBbe7+fYehVz7xMV5lEchw/1/NH5ZM++Ix9eLHY2N88/G/XZ+rYfWETxzxIHGvhj/Yx1fnzBfEYqSa9xEp4eb6JEKj7OJjiRw/X6o2h3pIktz6+W74xmma292lFF8sKspqFQ8wPIZqUfzFQnD/N1v3+46gvCWe3Bm9v6dtc2xlL9ma7vOaYv3CdaGbP1HZF7W+DfXmv8F&lt;/diagram&gt;&lt;/mxfile&gt;" onclick="(function(svg){var src=window.event.target||window.event.srcElement;while (src!=null&amp;&amp;src.nodeName.toLowerCase()!='a'){src=src.parentNode;}if(src==null){if(svg.wnd!=null&amp;&amp;!svg.wnd.closed){svg.wnd.focus();}else{var r=function(evt){if(evt.data=='ready'&amp;&amp;evt.source==svg.wnd){svg.wnd.postMessage(decodeURIComponent(svg.getAttribute('content')),'*');window.removeEventListener('message',r);}};window.addEventListener('message',r);svg.wnd=window.open('https://www.draw.io/?client=1&amp;lightbox=1&amp;edit=_blank');}}})(this);" viewBox="0 0 441 51" style="cursor:pointer;"><defs><linearGradient x1="0%" y1="0%" x2="0%" y2="100%" id="mx-gradient-f5f5f5-1-b3b3b3-1-s-0"><stop offset="0%" style="stop-color:#f5f5f5"/><stop offset="100%" style="stop-color:#b3b3b3"/></linearGradient></defs><g transform="translate(0.5,0.5)"><path d="M 282 25.14 L 311.29 25.14 L 333.49 25.14" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 338.74 25.14 L 331.74 28.64 L 333.49 25.14 L 331.74 21.64 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><rect x="180" y="0" width="102" height="50" rx="7.5" ry="7.5" fill="url(#mx-gradient-f5f5f5-1-b3b3b3-1-s-0)" stroke="#666666" pointer-events="none"/><path d="M 103 25.14 L 141.57 25.14 L 173.49 25.14" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 178.74 25.14 L 171.74 28.64 L 173.49 25.14 L 171.74 21.64 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><rect x="0" y="0" width="103" height="50" rx="7.5" ry="7.5" fill="#dae8fc" stroke="#6c8ebf" pointer-events="none"/><g transform="translate(112.5,7.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="57" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">s = '\xc2\xa9'</font></div></div></foreignObject><text x="29" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(71.5,2.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="25" height="12" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;">Encoding<div>UTF-8</div></div></div></foreignObject><text x="13" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(181.5,22.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="17" height="7" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 8px">stdin</font></div></div></foreignObject><text x="9" y="7" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(191.5,2.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="80" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">Python Interpreter</font></div></div></foreignObject><text x="40" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">&lt;font style="font-size: 10px"&gt;Python Interpreter&lt;/font&gt;</text></switch></g><g transform="translate(261.5,21.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="19" height="6" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 7px">stdout</font></div></div></foreignObject><text x="10" y="6" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(217.5,22.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="27" height="7" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 8px">"print s"</font></div></div></foreignObject><text x="14" y="7" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="340" y="0" width="100" height="50" rx="7.5" ry="7.5" fill="#dae8fc" stroke="#6c8ebf" pointer-events="none"/><g transform="translate(376.5,19.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="27" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 29px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><span><font style="font-size: 10px">&gt;&gt;&gt; ©</font></span></div></div></foreignObject><text x="14" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(401.5,2.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="25" height="12" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;">Encoding<div>UTF-8</div></div></div></foreignObject><text x="13" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(291.5,6.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="41" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">'\xc2\xa9'</font></div></div></foreignObject><text x="21" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(17.5,2.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="38" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">Terminal</font></div></div></foreignObject><text x="19" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(36.5,22.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="30" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 32px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">s =  '©'</font></div></div></foreignObject><text x="15" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(351.5,2.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="38" height="10" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px">Terminal</font></div></div></foreignObject><text x="19" y="8" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g></g></svg>
</div>


When we typed **s = '©'** on the terminal and pressed enter, the terminal would pass these string
as input to the **stdin** of the Python interpreter. As you already know, these glyphs are just for human display purposes and internally all these characters are just stored in some or other encoding.
Here the encoding of the terminal is UTF-8, so when it send these string, it will actually send the UTF-8 encoded string as series of bytes to **stdin**.

Just to confirm, here is a session with Python interpreter that verifies this.

{% highlight python %}
>>> import sys
>>> inp = sys.stdin.read()
s = '©'
>>> inp
"s = '\xc2\xa9'\n"
>>> len(inp)
9

{% endhighlight %}

As can be seen from above listing, when we manually called the **read** method on **stdin** and entered our string containing the '©' character, what we actually got were the same two bytes '\xc2\xa9' which are nothing but the UTF-8 encoding of ©.

Python then stores these two bytes in the 's' variable. And when we ask to print it's value, the same thing happens but in reverse. Here is another session with Python interpreter that shows this.

{% highlight python %}
>>> import sys
>>> s = '©'
>>> sys.stdout.write(s)
©>>> 
>>> sys.stdout.write('\xc2\xa9')
©>>>
{% endhighlight %}

As can be seen from this, whether we print 's' directly or write the UTF-8 encoding of '©', we get the same result. The reason being, that after getting these two bytes, the terminal tried to decode them as UTF-8 encoding and successfully decoded them to be representing the '©' Unicode Character and hence we get the nice copyright symbol glyph instead of the actual bytes being displayed.


**NOTE**

One thing that needs to be mentioned here is that shell encoding and the terminal encoding are two different things. By default, almost all the shells and terminals nowadays, have the default encoding set to UTF-8. But you can change both of them independently of each other.

Here is one session where you can see how these two different encodings come into play. 

{% highlight python %}
>>> import sys
>>> sys.stdout.encoding
'UTF-8'
>>> print u'\xa9'
Β©


{% endhighlight %} 


For this session, I have left the shell encoding to be UTF-8 which can be seen from the **stdout** encoding and changed the terminal encoding to **Western-1253**.

To understand this example, there is one thing that you must know. When you print normal **str** type strings, Python writes them as it is, a sequence of bytes. But in case of unicode strings, Python first tries to convert the Unicode string into the encoding of the **stdout** and then writes that converted sequence of bytes.

So, now having understood that, we can see more clearly what is happening in the above example.
When we asked Python to print **u'\xa9'**, since it is a Unicode string, Python would first convert it into the UTF-8 encoding because that it's **stdout** encoding and output that converted string to the terminal, which in our case would be **'\xc2\xa9'**. 

Now here comes the interesting part. Since we have changed the terminal encoding to **Western-1253**, terminal would try to decode these two bytes using this encoding instead of UTF-8. **\xc2** corresponds to symbol 
**Β** in these encoding and **\xa9** to **©**, which is what terminal shows instead of just **©**.

This result may seem surprising to some of you. It sure as hell, was surprising to me.


Thank you for reading my article. Let me know if you liked my article or any other suggestions for me, in the comments section below. And please, feel free to share :)
