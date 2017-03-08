---
layout: post
title:  "Python 2 and Unicode"
date:   2017-03-05 10:56:08 +0530
tags: unicode ascii character-set python
comments: true
meta:
    keywords: "python2 python-2 python-3 python3 encoding decoding unicode utf-8 character-set character-encoding utf-16 ascii bom byte-order-mark u+fffe u+feff endianness big-endian little-endian what-is-utf8 what-is-utf-16 what-is-unicode"
---

If you don't know what is Unicode, UTF-8 or UTF-16 or if need to refresh your memory on these topics, then before reading this article, check this one [What everyone should know about Unicode][Unicode].


I have divided this article in two parts, one for Python 2 and one for Python 3, since there are some differences in how each handles string and unicode in general. You can either read both of them or read just for the language you are interested in. This article is for Python 2. For Python 3, check out this [article]({{ site.url}}/unicode-in-python3.html).

Python 2 has two built-in types for handling strings. One is the `str` type and the other one is `unicode`. All the string literals that you normally create in your python program like

{% highlight python %}
    str_type_string = 'Some random string'
{% endhighlight %}

are the `str` type string literals. These are also called the byte strings as they are merely series of bytes. For example, if you do

{% highlight python %}
    random_byte_string = 'abc\x23\x45\x80\x81'
    print random_byte_string
    print 'Number of bytes =',len(random_byte_string)
 {% endhighlight %}

 you would get the output as follows-

{% highlight python %}
'abc#E\x80\x81'
Number of bytes = 7
{% endhighlight %}

As you can see from the output above, length of this string is just the number of bytes in the string. One thing to note in the above example is that, the last two bytes are non ASCII characters since they are greater than `\x7f` but they are also counted as valid bytes. You can use any byte in the range from `\x00` to `\xff` in the byte string.

Now to enter unicode characters, you have several options-
- Directly writing them as the unicode literal string by using `u` prefix like

{% highlight python %}
    unicode_string_literal = u'omega\u03a9'
{% endhighlight %}

This would give you a `unicode` type string.

- Using the unicode function to convert the normal byte string into unicode string.

{% highlight python %}
    str_type_string = 'abcd'
    unicode_string = unicode(str_type_string)
{% endhighlight %}

By default, unicode function uses the python default encoding scheme which is usually `ascii`. You can get the python default encoding scheme of your system by 

{% highlight python %}
    import sys
    print sys.getdefaultencoding()
{% endhighlight %}

You should never change this default encoding since many other programs and modules rely on this default behaviour and may break if you change it to something else. You should instead pass your desired encoding as the argument to the unicode function.

{% highlight python %}
    str_type_string = 'omega\xce\xa9'
    unicode_string = unicode(str_type_string, encoding='utf8')
{% endhighlight %}

In the example above, the encoding argument specifies the encoding of the input string given as argument to the unicode function. Here, I am passing 'utf8' as I am using `\xce\xa9` which is the utf-8 encoding for the Ω character.

- Third way is use to the `str` decode function. You can call the decode function on the `str` string object and pass in the encoding of the string being called upon to get the unicode string.

{% highlight python %}
    str_type_string = 'omega\xce\xa9'
    unicode_string = str_type_string.decode('utf8')
{% endhighlight %}


Finally to convert the unicode string back into normal `str` or byte string, use the decode counterpart function encode.

{% highlight python %}
    unicode_string = u'\u03a9'
    encoded_byte_string = unicode_string.encode('utf16')
{% endhighlight %}

Just pass in the encoding and it will give you the encoded byte string. On my machine it gave the ouput 

{% highlight python %}
 '\xff\xfe\xa9\x03'
{% endhighlight %}

`\xfe\xff` is the BOM character followed by the utf-16 encoding of the Ω character `\x03\xa9`, but in the reverse order since my machine is a little-endian machine. You may get different result on your machine depending upon your machine endianess.


### Working with unicode in files

Now suppose, you want to read in a file which is encoded using UTF-8 or UTF-16. You can use the python `open()` function, but it doesn't understand about encodings. It will just read the bytes of the file and dump it back to you. So, either you could write your own decoder for decoding those bytes to unicode or instead you can use nice `open()` function provided by the `codecs` module.

{% highlight python %}
    import codecs
    unicode_file = 'unicode_test.txt'
    with open(unicode_file, 'r') as file:
        normal_byte_string = file.read()

    with codecs.open(unicode_file, 'r', encoding='utf8') as file:
        unicode_string = file.read()
{% endhighlight %}

Similary, you can use the `file.write()` function to write a unicode string to a file and the `codecs` module will internally convert your string into the proper series of bytes and write to the disk.

### General Strategy in working with Unicode

Generally when you have to work with Unicode, what you can do is accept the input from any source, it may be from the user, from a network, from a file, in any encoding, and convert all the data into unicode internally. Then you can safely work on that unicode data, because you know that all the data is in the same format. When the times comes to write or send the data, then convert it back to the original encoding.

Thank you for reading my article. Let me know if you liked my article or any other suggestions for me, in the comments section below. And please, feel free to share :)



[Unicode]: https://kishuagarwal.github.io/unicode.html