# PrematureXorSwap
XOR swap is still the fastest tmp option. 

## background
I recently noticed an opportunity to tweak some tree code and insert an xorswap.  I was proud of my old school skillz.

the actual tweak happens to be in java, and swaps between 2 arrays.

## but wait...
to my surprised i noticed a blog post refuting this trick, indicating I'm from a previous era of instruction counting dinosaurs.

[this post](http://codingwiththomas.blogspot.com/2012/10/java-xor-swap-performance.html) lacked a github link and repeatability, even if you chase down the mentioned measurement (calipers) cited.  it prints zero's.  and if you debug it, without calling the blogged code.  this code also never actually used the product of the experiment, so it might've missed the point or taken advantage of hotspot being smart in one area and less aggressive in another.  

it doesn't need annotations or a test framework, or inheritance, so I set about seeing how things may have changed in 7 years from java 7 to java 12.

# results

the new code runs in main.  it tests 

 xor:  
   * the c code xor swap from days of yow
 
 tmpswap: 
   * the supposed faster optimizable option proven by java 1.7
 
 R64:
   * can we make a 64 bit tmp run faster than tmp ?  nope, but it edges out xor swap.

 xor tmp swap:
   * is assignment truly faster than xor? well, we try XOR on two temps!  FTW!!!!
   

some steady-state jvm results (lower is gooder)

```
xorswap: 1000000:  2 @500000.0/ms
xtmswap: 1000000:  1 @1000000.0/ms
r64swap: 1000000:  2 @500000.0/ms
tmpswap: 1000000:  1 @1000000.0/ms
xorswap: 10000000:  18 @555555.5555555555/ms
xtmswap: 10000000:  8 @1250000.0/ms
r64swap: 10000000:  19 @526315.7894736842/ms
tmpswap: 10000000:  9 @1111111.111111111/ms
xorswap: 100000000:  177 @564971.7514124294/ms
xtmswap: 100000000:  84 @1190476.1904761905/ms
r64swap: 100000000:  186 @537634.4086021505/ms
tmpswap: 100000000:  95 @1052631.5789473683/ms
xorswap: 1000000000:  1784 @560538.1165919283/ms
xtmswap: 1000000000:  828 @1207729.4685990338/ms
r64swap: 1000000000:  1693 @590667.4542232723/ms
tmpswap: 1000000000:  914 @1094091.9037199125/ms
```
