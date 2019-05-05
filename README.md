# PrematureXorSwap
XOR swap is ~still~ almost the fastest swap option. 

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
   * the supposed faster optimizable option proven by java 1.7.  discrete loop optimizers make this one faster in ways that don't occur inline with other loops in same method.  (jdk 12, as of commit time)
 
 R64:
   * can we make a 64 bit tmp run faster than tmp ?  nope, but it edges out xor swap.

 xor tmp swap:
   * is assignment truly faster than xor? well, we try XOR on two temps!  ~FTW!!!!~
   * as we blow out the one-big-method benchmark into multiple simple loop methods we see the tmpSwap takes the lead away from xor tmp.
   

some steady-state jvm results (lower is gooder)

```
xorSwap: 1000000:  2 @500000.0/ms
r64Swap: 1000000:  2 @500000.0/ms
tmpSwap: 1000000:  1 @1000000.0/ms
xorTmp: 1000000:  0 @Infinity/ms
xorSwap: 10000000:  18 @555555.5555555555/ms
r64Swap: 10000000:  17 @588235.2941176471/ms
tmpSwap: 10000000:  8 @1250000.0/ms
xorTmp: 10000000:  8 @1250000.0/ms
xorSwap: 100000000:  182 @549450.5494505494/ms
r64Swap: 100000000:  165 @606060.6060606061/ms
tmpSwap: 100000000:  71 @1408450.704225352/ms
xorTmp: 100000000:  81 @1234567.9012345679/ms
xorSwap: 1000000000:  1819 @549752.6113249038/ms
r64Swap: 1000000000:  1640 @609756.0975609756/ms
tmpSwap: 1000000000:  689 @1451378.809869376/ms
xorTmp: 1000000000:  816 @1225490.1960784313/ms
```
