---
layout: post
title:  "Life of a binary"
date:   2017-04-16 00:00:00 +0530
tags: binary c object-code machine-code assembly elf
comments: true
meta:
    keywords: "c compiler assembly object-code elf-format machine code compilation binary linking loading dynamic-linking plt procedure-linkage-table got global-offset-table how-printf-is-called"
---


Almost every one of you must have written a program, compiled it and then ran it to see the fruits of your hard labour. It feels good to finally see your program working, isn't it? But to make all of this work, we have someone else to thankful too. And that is your compiler (of course, assuming that you are working in a compiled language,not an interpreted one) which also does so much hard work behind the scenes.

In this article, I will try to show you how the source code that you write is transformed into something that your machine is actually able to run. I am choosing Linux as my host machine and C as the programming language here, but the concepts here are general enough to apply to many compiled languages out there.

**Note**: If you want to follow along in this article, then you will have to make sure that you have **gcc, elfutils** installed on your local machine.

Let's start with a simple C program and see how it get's converted by the compiler.

<script src="https://gist.github.com/kishuagarwal/1a2313f8a3b4c2f6d77650a13e2e35ca.js"></script>

This program creates two variables, adds them up and print the result on the screen. Pretty simple, huh?

But let's see what this seemingly simple program has to go through to finally get executed on your system.

Compiler has usually the following five steps (with the last step being part of the OS)- 

<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>


Let's go through each of the step in sufficient detail.

<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages_preprocessing.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>

First step is the **Preprocessing** step which is done by the Preprocessor. Job of the Preprocessor is to handle all the preprocessor directives present in your code. These directives start with **#**. But before it processes them, it first removes all the comments from the code as comments are there only for the human readability. Then it finds all the **#** commands, and does what the commands says. 

In the code above, we have just used **#include** directive, which simply says to the Preprocesssor to copy the **stdio.h** file and paste it into this file at the current location.

You can see the output of the Preprocessor by passing **-E** flag to the **gcc** compiler.

{% highlight c %}
gcc -E sample.c
{% endhighlight %}

You would get something like the following-

<script src="https://gist.github.com/kishuagarwal/f2ce4e82094096bf318a1652a66613c2.js"></script>


<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages_compilation.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>


Confusingly, the second step is also called compilation. The compiler takes the output from the Preprocessor and is responsbile to do the following important tasks.

- Pass the output through a lexical analyser to identify various tokens present in the file. Tokens are just literals present in your program like 'int', 'return', 'void', '0' and so on. Lexical Analyser also associates with each token the type of the token, whether the token is a string literal, integer, float, if token, and so on.

- Pass the output of the lexical analyser to the syntax analyser to check whether the program is written in a way that satisfy the grammar rules of the language the program is written in. For example, it will raise syntax error when parsing this line of code,
{% highlight c %}
    b = a + ;
{% endhighlight %}
since **+** is a missing one operand.

- Pass the output of the syntax analyser to the semantic analyser, which checks whether the program satisfies semantics of the  language like type checking and variables are declared before their first usage, etc.

- If the program is syntactically correct, then the source code is converted into the assembly intructions for the specified target architecture. By default, it generates assembly for the machine it is running on. But suppose, you are building programs for embedded systems, then you can pass the architecture of the target machine and gcc will generate assembly for that machine.


To see the output from this stage, pass the **-S** flag to the **gcc** compiler.

{% highlight bash %}

gcc -S sample.c

{% endhighlight %}

You would get something like the following depending upon on your environment.

<script src="https://gist.github.com/kishuagarwal/0ec66d071d2fee84cf5792186dcf0a5d.js"></script>

If you don't know assembly language, it all looks pretty scary at first, but it is not that bad. It takes more time to understand the assembly code than your normal high level language code, but given enough time, you can surely read it.

Let's see what this file contains.

All the lines beginning with '.' are the assembler directives. **.file** denotes the name of the source file, which can be used for debugging purposes. The string literal in our source code **%d\n** now resides in the **.rodata** section (ro means read-only), since it is a read only string. The compiler named this string as **LC0**, to later refer to it in the code. Whenever you see a label starting with .L, it means that those labels are local to the current file and will not visible to the other files.

**.globl** tells that main is a global symbol, which means that the main can be called from other files. **.type** tells that main is a function. Then follows the assembly for the main function. You can ignore the directives starting with **cfi**. They are used for call stack unwinding in case of exceptions. We will ignore them in this article, but you can know more about them [here](https://sourceware.org/binutils/docs/as/CFI-directives.html). 


Let's try to understand now the disassembly of the main function.

<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="755px" version="1.1" content="&lt;mxfile userAgent=&quot;Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36&quot; version=&quot;6.5.2&quot; editor=&quot;www.draw.io&quot; type=&quot;google&quot;&gt;&lt;diagram name=&quot;Page-1&quot;&gt;7VxLc+o2FP41TFfJWLL8YJnQpF2kM3eGmfbepbCFUWssKkQg/fWVbBljWwYHDL6Jk0WCjmw9zneeOiIje7Lc/cbxavEHC0k8gla4G9m/jiAco7H8rQhvGQF5ICNEnIYZ6YAwpf8RTbQ0dUNDsi49KBiLBV2ViQFLEhKIEg1zzrblx+YsLs+6whGpEaYBjuvUv2goFhnVh25B/53QaJHPDFy94SXOH9Y7WS9wyLYHJPtpZE84YyL7tNxNSKx4l/Mle++5oXe/ME4S0eYFBxJ/FgZzH8/I2J/bd3qEVxxv9Gb1QsVbvnvONklI1ABgZD9uF1SQ6QoHqncr4Za0hVjGunstOPtnzyVFmdM4nrCY8XQ0+zn9kfSI45DKZed9CUuIepwlQkuAK5t6eYQLsmvcM9hzUkogYUsi+Jt8JH/B0czX0gdd3d4WWIIcoMUBjramYS0+0X7ogsPyg2ZyS4bDATLc6pPh9vAYDrw+GY4MDHdjoXde4rz774blHXfrlCcP8gG5913RKT9F6i+frfKB5JqysbKeGqCSlcKEWhWJA+A0Ccc0SmQzkKwnkv6ogKHSFTzojiUNQzWNUUwq0JZFxesGbORUrJlTw9oxQA2vAfW4xnkSSs+pm4yLBYtYguOngvpYKJtVhkjun7991/S08UM17h3Z/JsI8aY5izeCSVIx+gtjq2blNGmbWuVx9stNsQ0PyHEZF5hHRBxzrXUYOYmxoK/l+TtFBYBOYdlRkaJy7zm6+eMQpMO+PWhnQwavBhlsARnsDbIWYQFJwgcVzBbG6gClEK8Xew+mGt+wkAYsSSnQQm10o0D6CNAlnEF38IDz4Tlh+XJaaxT1DN8YTd1VbqjHlajGq1jUbHv6rcMYvDLQPq3JB0KVgTIe1AZKhWq/7TPlzGl0zusVTgqH2onDXjc77D3ZNO2M/+yevXs/Djy/lR/PEezW/HhdeoybO+sLLLZZRccVbPwK09uqei0wrw50TVX3G1W9rVqDVK2tFAkrpgm5yyUx7VUhpVNX+0cyZ5ykpx80UcctmyQQlKmPUtviEyYhpK/nWiFgtELPNLpXnadNUWnqk3bnQFYhytv6BXDMvNQM0cUZZQfWCKCypNrQkEJaBntUdV7d5Ozj0+HQR0/akXU6kbth0m4NkONWrxwfwNFrlePA65XjqMso57PkxbnmH828kGXG+fqZMTJlLIPKjHNneBZAt8mNUfVQ0jszYEZuZSB0u4AZeQZJ6ywPnny84+sOHAD0KwmQofbm3SrLRaao8n0A+yZ8X4j4Zb3PbiyqRvp+Ptbdo1Ctx/l1EJABhKuUDBxToPnOPM82odCa458uqEJ9BlVOt8WGj1QD8lr4Zae3wMn9UrQLFc313LLd7PXiiGvKF4dZV+/CJVYCE2jITG9WRvcGa0NzoT5qQ93ebKh3+V0WAEw692c6ILTYXMWNgzWodp8G1WvO9y6phbYAfPbesuhnl4NeLwh6HRTKTmOOh6rkIPdu/YA7gEpSjeOoT477V4tTj1wnGUycCgx1WWOceo0DNN8eapzqt4lTM1vTR5zqdxCnwobcML2bODS1c2FF7XIT20d66Ddf2rscXf8LXNM9gNuBe3nVqRlc9AUutOHNwAX+eA4cEPgQ20HgAuO3696H7fgL2kZo7RtCW9fbvq5XTmVoRJNIPvHCJBbpEjjFs5ise7paaX9drTyenv1MVyv95iJ4duZ0rjBM8WsmlhMcx4SnFfGDq79zjpfqMGSlrm5IbI5/A8AgqpetTosqPDHvwEUVWWeKqvN+UZXN4nvv2f2d4p8H2E//Aw==&lt;/diagram&gt;&lt;/mxfile&gt;" onclick="(function(svg){var src=window.event.target||window.event.srcElement;while (src!=null&amp;&amp;src.nodeName.toLowerCase()!='a'){src=src.parentNode;}if(src==null){if(svg.wnd!=null&amp;&amp;!svg.wnd.closed){svg.wnd.focus();}else{var r=function(evt){if(evt.data=='ready'&amp;&amp;evt.source==svg.wnd){svg.wnd.postMessage(decodeURIComponent(svg.getAttribute('content')),'*');window.removeEventListener('message',r);}};window.addEventListener('message',r);svg.wnd=window.open('https://www.draw.io/?client=1&amp;lightbox=1');}}})(this);" viewBox="0 0 755 253" style="cursor:pointer;max-width:100%;max-height:253px;"><defs/><g transform="translate(0.5,0.5)"><rect x="108" y="140" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><rect x="108" y="80" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><rect x="108" y="50" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(13.5,143.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="22" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 30px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 20px">rbp</font></div></div></foreignObject><text x="14" y="14" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><path d="M 53 154.88 L 80.76 154.88 L 101.46 154.88" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 106.71 154.88 L 99.71 158.38 L 101.46 154.88 L 99.71 151.38 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 198.41 79.59 L 198.41 79.59" fill="none" stroke="#000000" stroke-width="2" stroke-miterlimit="10" pointer-events="none"/><path d="M 198.41 79.59 L 198.41 79.59 L 198.41 79.59 L 198.41 79.59 Z" fill="#000000" stroke="#000000" stroke-width="2" stroke-miterlimit="10" pointer-events="none"/><path d="M 167.82 139.59 L 167.82 110.18" fill="none" stroke="#000000" stroke-miterlimit="10" stroke-dasharray="1 4" pointer-events="none"/><g transform="translate(13.5,54.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="22" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 28px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><span><font style="font-size: 20px">rsp</font></span><br /></div></div></foreignObject><text x="14" y="14" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><path d="M 53 65 L 101.63 65" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 106.88 65 L 99.88 68.5 L 101.63 65 L 99.88 61.5 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><rect x="98" y="200" width="100" height="40" fill="#ffffff" stroke="none" pointer-events="none"/><g transform="translate(88.5,192.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="119" height="54" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 24px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap; font-weight: bold; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px ; line-height: 120%">Before main function call</font><div><font style="font-size: 10px">Fig. 1</font></div></div></div></foreignObject><text x="60" y="39" fill="#000000" text-anchor="middle" font-size="24px" font-family="Helvetica" font-weight="bold">[Not supported by viewer]</text></switch></g><rect x="363" y="145" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><rect x="363" y="85" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><rect x="363" y="55" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><path d="M 452.53 85.47 L 452.53 85.47" fill="none" stroke="#000000" stroke-width="2" stroke-miterlimit="10" pointer-events="none"/><path d="M 452.53 85.47 L 452.53 85.47 L 452.53 85.47 L 452.53 85.47 Z" fill="#000000" stroke="#000000" stroke-width="2" stroke-miterlimit="10" pointer-events="none"/><path d="M 423.12 145.47 L 423.12 114.88" fill="none" stroke="#000000" stroke-miterlimit="10" stroke-dasharray="1 4" pointer-events="none"/><g transform="translate(241.5,36.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="62" height="22" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 64px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 20px">rsp,rbp</font></div></div></foreignObject><text x="31" y="14" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">&lt;font style="font-size: 20px"&gt;rsp,rbp&lt;/font&gt;</text></switch></g><g transform="translate(9.5,166.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="45" height="7" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 8px">Let's call it X</font></div></div></foreignObject><text x="23" y="7" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="363" y="25" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(417.5,32.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="9" height="13" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 9px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 13px">X</font></div></div></foreignObject><text x="5" y="10" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><path d="M 308 47.82 L 336.06 47.82 L 336.06 39.59 L 356.75 39.59" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 362 39.59 L 355 43.09 L 356.75 39.59 L 355 36.09 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><rect x="634" y="140" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(688.5,147.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="9" height="13" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 9px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 13px">X</font></div></div></foreignObject><text x="5" y="10" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(548.5,153.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="22" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 30px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 20px">rbp</font></div></div></foreignObject><text x="14" y="14" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><path d="M 588 165.47 L 611.35 165.47 L 611.35 154.88 L 627.34 154.88" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 632.59 154.88 L 625.59 158.38 L 627.34 154.88 L 625.59 151.38 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><rect x="634" y="110" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(668.5,118.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="49" height="11" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 49px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 11px">Value of c</font></div></div></foreignObject><text x="25" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="634" y="80" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(668.5,88.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="49" height="11" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 49px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><span><font style="font-size: 11px">Value of b</font></span></div></div></foreignObject><text x="25" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="634" y="51" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(668.5,59.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="49" height="11" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 49px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 11px">Value of a</font></div></div></foreignObject><text x="25" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="634" y="21" width="120" height="30" rx="4.5" ry="4.5" fill="#ffffff" stroke="#000000" pointer-events="none"/><g transform="translate(548.5,0.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="22" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 28px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 20px">rsp</font></div></div></foreignObject><text x="14" y="14" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><path d="M 588 12.53 L 611.35 12.53 L 611.35 36.06 L 627.34 36.06" fill="none" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><path d="M 632.59 36.06 L 625.59 39.56 L 627.34 36.06 L 625.59 32.56 Z" fill="#000000" stroke="#000000" stroke-miterlimit="10" pointer-events="none"/><g transform="translate(585.5,59.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="34" height="12" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 36px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 12px">rbp-12</font></div></div></foreignObject><text x="17" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(588.5,88.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="12" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 30px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 12px">rbp-8</font></div></div></foreignObject><text x="14" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(588.5,115.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="28" height="12" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 30px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 12px">rbp-4</font></div></div></foreignObject><text x="14" y="9" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><g transform="translate(591.5,217.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="22" height="8" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 6px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; width: 22px; white-space: nowrap; word-wrap: normal; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 9px">rbp-4</font></div></div></foreignObject><text x="11" y="7" fill="#000000" text-anchor="middle" font-size="6px" font-family="Helvetica">[Not supported by viewer]</text></switch></g><rect x="628" y="200" width="100" height="40" fill="#ffffff" stroke="none" pointer-events="none"/><g transform="translate(622.5,192.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="109" height="54" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 24px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap; font-weight: bold; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><font style="font-size: 10px ; line-height: 120%">Setting Local variables</font><div><font style="font-size: 10px">Fig. 3</font></div></div></div></foreignObject><text x="55" y="39" fill="#000000" text-anchor="middle" font-size="24px" font-family="Helvetica" font-weight="bold">[Not supported by viewer]</text></switch></g><rect x="358" y="200" width="100" height="50" fill="#ffffff" stroke="none" pointer-events="none"/><g transform="translate(317.5,197.5)"><switch><foreignObject style="overflow:visible;" pointer-events="all" width="180" height="54" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-size: 24px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; vertical-align: top; white-space: nowrap; font-weight: bold; text-align: center;"><div xmlns="http://www.w3.org/1999/xhtml" style="display:inline-block;text-align:inherit;text-decoration:inherit;"><span style="font-size: 10px">Saving Caller's function frame pointer</span><div><span style="font-size: 10px">Fig. 2</span></div></div></div></foreignObject><text x="90" y="39" fill="#000000" text-anchor="middle" font-size="24px" font-family="Helvetica" font-weight="bold">[Not supported by viewer]</text></switch></g></g></svg>



**11**. You must be knowing that when you call a function, a new stack frame is created for that function. To make that possible, we need some way of knowing the start of the caller's function frame pointer when the new function returns. That's why we push the current frame pointer which is stored in the **rbp** register onto the stack. 

**14** Move the current stack pointer into the base pointer. This becomes our current function frame pointer. Fig. 1 depicts the state before pushing the rbp register and Fig. 2 shows after the previous frame pointer is pushed and the stack pointer is moved to the current frame pointer.

**16** We have 3 local variables in our program, all of types int. On my machine, each int occupies 4 bytes, so we need 12 bytes of space on the stack to hold our local variables. The way we create space for our local variables on the stack, is decrement our stack pointer by the number of bytes we need for our local variables. Decrement, because stack grows from higher addresses to lower addresses. But here you see we are decrementing by 16 instead of 12. The reason is, space is allocated in the chunks of 16 bytes. So, even if you have 1 local variable, space of 16 bytes would be allocated on the stack. This is done for performance reasons on some architecures. See Fig. 3 to see how the stack is laid out right now.

**17-22** This code is pretty straight forward. The compiler has used the slot **rbp-12** as the storage for variable **a**, **rbp-8** for **b** and **rbp-4** for **c**. It moves the the values 1 and 2 to address of variable **a** and **b** respectively. To prepare for the addition, it moves the **b** value to **edx** register and the value of the **a** register to the **eax** register. The result of the addition is stored in the **eax** register which is later transferred to the address of the **c** variable.

**23-27** Then we prepare for our printf call. Firstly, the value of the **c** variable is moved to the **esi** register. And then address of our string constant **%d\n** is moved to the **edi** register. **esi** and **edi** registers now hold the argument of our printf call. **edi** holds the first argument and **esi** holds the second argument. Then we call the printf function to print the value of the variable **c** formatted as the integer value. Point to note here is that **printf** symbol is undefined at this point. We would see how this **printf** symbol gets resolved later on in this article.

**.size** tells the size of the main function in bytes. "**.-main**" is an expression where the **.** symbol means the address of the current line. So this expression evaluates to **current_address_of the line - address of the main function** which gives us the size of the main function in bytes.

**.ident** just tell the assembler to add the following line in the **.comment** section.
**.note.GNU-stack** is used for telling whether the stack for this program is executable or not. Mostly the value for this directive is null string, which tells the stack is not executable. 


<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages_assembly.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>

What we have right now is our program in the assembly language, but it is still in the language which is not understood by the processors. We have to convert the assembly language to the machine language, and that work is done by the Assembler. Assembler takes your assembly file and produces an object file which is a binary file containing the machine instructions for your program.

Let's convert our assembly file to the object file to see the process in action. To get the object file for your program, pass the **c** flag to the **gcc** compiler.

{% highlight bash %}
gcc -c sample.c
{% endhighlight%}

You would get a object file with an extension of **.o**. Since, this is a binary file, you won't be able to open it in a normal text editor to view it's contents. But we have tools at our disposal, to find out what is lying inside in those object files.

Object files could have many different file formats. We will be focussing on one in particular which is used on the Linux and that is the [ELF](https://en.wikipedia.org/wiki/Executable_and_Linkable_Format) file format.

ELF files contains following information-
- ELF Header 
- Program header table
- Section header table
- Some other data referred to by the previous tables 

**ELF Header** contains some meta information about the object file such as type of the file, machine against which binary is made, version, size of the header, etc. To view header, just pass **-h** flag to **eu-readelf** utility.

<script src="https://gist.github.com/kishuagarwal/df3c04b51211ede7bddbb8324c0068df.js"></script>

From the above listing, we see that this file doesn't have any **Program Headers** and that is fine. **Program Headers** are only present in the executable files and shared libraries. We will see **Program Headers** when we link the file in the next step.

But we do have 13 sections. Let's see what are these sections. Use the **-S** flag.

<script src="https://gist.github.com/kishuagarwal/afeef93343641ee40de78fdff2ba48d2.js"></script>

You don't need to understand whole of the above listing. But essentially, for each section, it lists various information, like the name of the section, size of the section and the offset of the section from the start of the file. Important sections for our use are the following-

- **text** section contains our machine code
- **rodata** section contains the read only data in our program. It may be constants or string literals that you may have used in your program. Here it just contains **%d\n**
- **data** sections contains the initialized data of our program. Here it is empty, since we don't have any initialized data
- **bss** section is like the **data** section but contains the uninitialized data of our program. Uninitalized data could be a array declared like **int arr[100]**, which becomes part of this section. One point to note about the bss section is that, unlike the other sections which occupy space depending upon their content, bss section just contain the size of the section and nothing else. The reason being at the time of loading, all that is needed is the count of bytes that we need to allocate in this section. In this way we reduce the size of the final executable 
- **strtab** section list all the strings contained in our program
- **symtab** section is the symbol table. It contains all the symbols(variable and function names) of our program.
- **rela.text** section is the relocation section. More about this later.

You can also view the contents of these sections, just pass the corresponding section number to the **eu-readelf** program. You can also use the **objdump** tool. It can also provide you with the dissembly for some of the sections.

Let's talk in little more detail about the **rela.text** section. Remember the **printf** function that we used in our program. Now, **printf** is something that we haven't defined by ourself, it is part of the C library. Normally when you compile your C programs, the compiler will compile them in a way so that C functions that you call are not bundled in with your executable, which thus reduces the size of the final executable. Instead a table is made of all those symbols, called a, **relocation table**, which is later filled by something in called the **loader**. We will discuss more about the **loader** part later on, but for now, the important thing is that the if you look at the **rela.text** section, you would find the **printf** symbol listed down there. Let's confirm that once here.

<script src="https://gist.github.com/kishuagarwal/4173bcae76d5b050523427617f45e5a7.js"></script>

You can ignore the second relocation section **.rela.eh_frame**. It has to do with exception handling, which is not of much interest to us here. Let's see the first section there. There we can see two entries, one of which is our **printf** symbol. What does this entry mean is that, there is a symbol used in this file with a name of **printf** but has not been defined, and that symbol is located in this file at the offset 0x31 from the start of the **.text** section. Let's check what is at the offset 0x31 right now in the **.text** section.

<script src="https://gist.github.com/kishuagarwal/83cf53bbe21fa03faee61151105eb6f4.js"></script>


Here you can see the call instruction at offset 0x30. **e8** stands for the opcode of the call instruction followed by the 4 bytes from offset 0x31 to 0x34, which should correspond to our **printf** function actual address which we don't have right now, so they are just 00's. (Later on, we will see that is location doesn't actually hold the printf address, but indirectly calls it using something called **plt** table. We will cover this part later)


<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages_linking.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>

All the things that we have done till now have worked on a single source file. But in reality, that is the rarely the case. In real production code, you have hundred's of thousand's of source code files which you would need to compile and create a executable. Now how the steps that we followed till now would compare in that case?

Well, the steps would all remain the same. All the source code files would individually get preprocessed, compiled, assembed and we would get separate object code files at the end. 

Now each source code file wouldn't have been written in isolation. They must have some functions, global variables which must be defined in some file and used at different locations in other files.

It is the job of the linker to gather all the object files, go through each of them and track which symbol does each file defines and which symbols does it uses. It could find all these information in the symbol table in each of the object files. After gathering all these information, the linker creates a single object file combining all the sections from each of the individual object files into their corresponding sections and relocating all the symbols that can be resolved.

In our case, we don't have collection of source files, we have just one file, but since we use printf function from the C library, our source file will by dynamically linked with the C library. Let's now link our program and further investigate the output.

{% highlight bash %}
gcc sample.c
{% endhighlight %}

I won't go into much detail here, since it is also a ELF file that we saw above, with just some new sections. One thing to note here is, when we saw the object file that we got from the assembler, the addresses that we saw were relative. 
But after having linked all the files, we have pretty much idea, where all the pieces go and thus, if you examine the output of these stage, it contains absolute addresses also.

At this stage, linker has identified all the symbols that are being used in our program, who uses those symbols, and who has defined those symbols. Linker just maps the address of the definition of the symbol to the usage of the symbol. But after doing all this, there still exists some symbols that are not yet resolved at this point, one of the being our **printf** symbol. In general, these are such symbols which are either externally defined variables or externally defined functions. Linker also creates a relocation table, the same as that was created by the Assembler, with those entries which are still unresolved.

At this point, there is one thing you should know. The functions and data you use from other libraries, can be statically linked or dynamically linked. Static linking means that the functions and data from those libraries would be copied and pasted into your executable. Whereas, if you do dynamic linking, then those functions and data are not copied into your executable, thus reducing your final executable size. 

For a libray to have facility of dyamic linking against it, the library must be a shared library (**so** file). Normally, the common libraries used by many programs comes as shared libraries and one of them is our libc library. **libc** is used by so many programs that if every program started to statically link against it, then at any point, there would be so many copies of the same code occupying space in your memory. Having dynamic linking saves this problem, and at any moment only one copy of the libc would be occupying space in the memory and all the programs would be referencing from that shared library. 


To make the dynamic linking possible, the linker creates two more sections that weren't there in the object code generated by the assembler. These are the **.plt** (Procedure Linkage table) and the **.got**  (Global Offset Table) sections. We will cover about these sections when we come to loading our executable, as these sections come useful when we actually load the executable.


<div class="img_container" style="height:100px">

<img src="/assets/images/compiling_stages_loading.png" style="position:absolute; left:0; right:0; display: block; margin: 10px auto" />
</div>

Now it is time to actually run our executable file.

When you click on the file in your GUI, or run it from the command line, indirectly **execev** system call is invoken. It is this system call, where the kernel starts the work of loading your executable in the memory. 

Remember the **Program Header Table** from above. This is where it is very useful. 
<script src="https://gist.github.com/kishuagarwal/a71434e035c88a6ca47f51fd92d7faee.js"></script>

How would the kernel know where to find this table in the file? Well, that information could be found in the **ELF Header** which always starts at offset 0 in the file.  Having done that, kernel looks up all the entries which are of type **LOAD** and loads them into the memory space of the process.

As you can see from the above listing, there are two entries of type **LOAD**. You can also see which sections are contained in each segment.

Modern operating systems and processors manage memory in terms of pages. Your computer memory is divided into fixed size chunks and when any process asks for some memory, the operating system allots some number of pages to that process.
Apart from the benefit of managing memory efficiently, this also has the benefit of providing security. Operating systems and kernel can set protection bits for each page. Protection bits specifies whether the particular page is Read only, could be written or can be executed. A page whose protection bit is set as Read only, can't be modified and thus prevents from intentional or unintentional modification of data.

Read only pages have also one benefit that multiple running processes for the same program can share the same pages. Since the pages are read only, no running process can modify those pages and thus, every process would work just fine.

To set up these protection bits we somehow would have to tell the kernel, which pages have to be marked Read only and which could be Written and Executed. These information is stored in the Flags in each of the entries above.

Notice the first **LOAD** entry. It is marked as R and E, which means that these segment could be read and executed but can't be modified and if you look down and see which sections come in these segment, you can see two familiar sections there, **.text** and **.rodata**. Thus our code and read-only data can only be read and executed but can't be modified which is what should happen.

Similary, second **LOAD** entry contains the initialized and non initialized data, **GOT** table (more on this later) which are marked as RW and thus can be read and written but can't be executed. 

After loading these segments and setting up their permissions, the kernel checks if there is **.intrep** segment or not. In the statically linked executable, there is no need for this segment, since the executable contains all the code that it needs, but for the dynamically linked executable, this segment is important. This segment contains the **.intrep** section which contains the path to the dynamic linker. (You can check that there is no **.intrep** segment in the statically linked executable by passing **-static** flag to the gcc compiler and checking the header table in the resulting executable)


In our case, it would find one and it points to the dynamic linker at this path **/lib64/ld-linux-x86-64.so.2**. Similarly to our executable, the kernel would start loading these shared object by reading the header, finding its segments and loading them in the memory space of the our current program. In statically linked executable where all this is not needed, the kernel would have given control to our program, here the kernel gives control to the dynamic linker and pushes the address of our main function to be called on the stack, so that after dynamic linker finishes it's job, it knows where to hand over the control to.

We should now understand the two tables we have been skipping over for too long now, **Procedure Linkage Table** and **Global Offset Table** as these are closely related to the function of dynamic linker.

There might be two types of relocations needed in your program. Variables relocations and function relocations. For a variable which is externally defined, we include that entry in the **GOT** table and the functions which are externally defined we include those entries in both the tables. So, essentially, **GOT** table has entries for all the externally defined variables as well as functions, and **PLT** table has entries for only the functions. The reason why we have two entries for functions will be clear by the following example.

Let us take an example of printf function to see how these tables works. In our main function, let's see the call instruction to printf function.

{% highlight bash %}
400556:    e8 a5 fe ff ff           callq   0x400400
{% endhighlight %} 

This call instruction is calling an address which is part of the **.plt** section. Let's see what is there.

<script src="https://gist.github.com/kishuagarwal/b3e8029ce7c119ab655cd404d50d891b.js"></script>

For each externally defined function, we have an entry in the **plt** section and all look the same and have three instructions, except the first entry. This is a special entry which we will see the use of later.

There we find a jump to the value contained at the address **0x601018**. These address is an entry in the GOT table. Let's see the content of these address.

<script src="https://gist.github.com/kishuagarwal/3b30740bb5d9d4dcdec10a60a0fbf45a.js"></script>

This is where the magic happens. Except the first time when the printf function is called, the value at this address would be the actual address of the printf function from the C library and we would simply jump to that location. But for the first time, something else happens. 

When printf function is called for the first time, value at this location is the address of the next instruction in the **plt** entry of the printf function. As you can see from the above listing, it is **400406** which is stored in little endian format. At this location in the **plt** entry, we have a push instruction which pushes 0 onto the stack. Each **plt** entry have same push instruction but they push different numbers. 0 here denotes the offset of the printf symbol in the relocation table. The push instruction is then followed by the jump instruction which jumps to the first instruction in the first **plt** entry. 

Remember from above, when I told you that the first entry is special. It is because here where the dynamic linker is called to resolve the external symbols and relocate them. To do that, we jump to the address contained in the address **601010** in the **got** table. These address should contain the address of the dynamic linker routine to handle the relocation. Right now these entry is filled with 0's, but this address is filled by the linker when the program actually runs and the kernel calls the dynamic linker. 

When the routine is called, linker would resolve the symbol that was pushed earlier(in our case, 0), from external shared objects and put the correct address of the symbol in the **got** table. So, from now on, when the printf function is called, we don't have to consult the linker, we can directly jump from plt to our printf function in the C library.

This process is called lazy loading. A program may contain many external symbols, but it may not call of them in one run of the program. So, the symbols resolution is deferred till the actual use and this save us some program startup time.

As you can see from the above discussion, we never had to modify the **plt** section, but only the **got** section. And that is why **plt** section is in the first **LOAD** segment and marked as Read only, while **got** section is in the second **LOAD** segment and marked Write.

And this is how dynamic linker works. I have skipped over lots of gory details but if you are interested in knowing in more detail, then you can checkout this [article](https://www.akkadia.org/drepper/dsohowto.pdf).


Let's go back to our program loading. We have already done most of the work. Kernel has loaded all the loadable segments, has invoked the dynamic linker. All that is left is to invoke our main function. And that work is done by the linker after it finishes. And when it calls our main function we get the following output in our terminal-

{% highlight bash %}
3
{% endhighlight %}

And that my friend, is bliss.

Thank you for reading my article. Let me know if you liked my article or any other suggestions for me, in the comments section below. And please, feel free to share :)