---
layout: post
title:  "Python 3 and Unicode"
date:   2017-03-06 10:56:08 +0530
tags: unicode ascii character-set python
comments: true
meta:
    keywords: "python2 , python3 , encoding , decoding , unicode, utf-8,  character-set, character-encoding, utf-16, ascii , bom, byte-order-mark, u+fffe, u+feff, endianness, big-endian, little-endian, what-is-utf8, what-is-utf-16, what-is-unicode"
---

If you don't know what is Unicode, UTF-8 or UTF-16 or if need to refresh your memory on these topics, then before reading this article, check this one [What everyone should know about Unicode][Unicode].


I have divided this article in two parts, one for Python 2 and one for Python 3, since there are some differences in how each handles string and unicode in general. You can either read both of them or read just for the language you are interested in. This article is for Python 3. For Python 2, check out this [article]({{ site.url}}/unicode-in-python2.html).

Python 3 has two built-in types for handling strings. One is the `str` type and the other one is `bytes`. All the string literals that you normally create in your python program like

{% highlight python %}
    str_type_string = 'Some random string'
{% endhighlight %}

are the `str` type string literals. These are also the unicode strings. There is no separate `unicode` type that we have in Python 2.

Other type is the `bytes` type. They are just a series of bytes. To create them, you would have to use `b` prefix, like 

{% highlight python %}
    bytes_string = b'\x45\x89'
    print type(bytes_string)
{% endhighlight %}

You would get the type of the string as `bytes`. These is the same as the `str` type that we have in Python 2. So, in a nutshell, if you are moving from Python 2 to Python 3, you would find that the normal `str` type is replaced by `bytes` type and the `unicode` type is replaced by the `str` type.


To use unicode characters in Python 3, you have several options-
- Directly writing them as the unicode literal string as shown above

{% highlight python %}
    unicode_string_literal = 'omega\u03a9'
{% endhighlight %}

This would give you a `str` type string.

- Using the `str` function to convert the `bytes` string into unicode string.

{% highlight python %}
    bytes_type_string = b'\xce\xa9'
    unicode_string = str(bytes_type_string, encoding='utf8')
{% endhighlight %}

The `encoding` argument specifies the encoding of the input string given as argument to the `str` function. Here, I am passing 'utf8' as I am using `\xce\xa9` which is the utf-8 encoding for the Ω character. `str` function would give decoded unicode string.

- Third way is use to the `bytes` decode function. You can call the decode function on the `bytes` string object and pass in the encoding of the string being called upon to get the unicode string.

{% highlight python %}
    bytes_type_string = 'omega\xce\xa9'
    unicode_string = bytes_type_string.decode('utf8')
{% endhighlight %}


Finally to convert the unicode string into `bytes` type, use the decode counterpart function encode.

{% highlight python %}
    unicode_string = u'\u03a9'
    bytes_string = unicode_string.encode('utf16')
{% endhighlight %}

Just pass in the encoding and it will give you the encoded byte string. On my machine it gave the ouput 

{% highlight python %}
 '\xff\xfe\xa9\x03'
{% endhighlight %}

`\xfe\xff` is the BOM character followed by the utf-16 encoding of the Ω character `\x03\xa9`, but in the reverse order since my machine is a little-endian machine. You may get different result on your machine depending upon your machine endianess.


### Working with unicode in files

Now suppose, you want to read in a file which is encoded using UTF-8 or UTF-16. Unlike Python 2, where you have to use the `codecs` module `open` function to handle the unicode files, in Python 3, you can use the system's normal `open` function. This function accepts an encoding of the file as an argument and would automatically convert the encoded bytes from the file to the unicode characters for you.

{% highlight python %}

    unicode_file = 'unicode_test.txt'
    with open(unicode_file, mode='r', encoding='utf-8') as file:
        unicode_string = file.read()

{% endhighlight %}

Similary, you can use the `file.write()` function to write a unicode string to a file and the function will internally convert your string into the proper series of bytes and write to the disk.

### General Strategy in working with Unicode

Generally when you have to work with Unicode, what you can do is accept the input from any source, it may be from the user, from a network, from a file, in any encoding, and convert all the data into unicode internally. Then you can safely work on that unicode data, because you know that all the data is in the same format. When the times comes to write or send the data, then convert it back to the original encoding.

Thank you for reading my article. Let me know if you liked my article or any other suggestions for me, in the comments section below. And please, feel free to share :)



[Unicode]: https://kishuagarwal.github.io/unicode.html