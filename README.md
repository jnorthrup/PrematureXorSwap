# PrematureXorSwap
XOR swap is ~still~ ~almost~ not really the fastest swap option. 

## background
I recently noticed an opportunity to tweak some tree code and insert an xorswap.  I was proud of my old school skillz.

the actual tweak happens to be in java, and swaps between 2 arrays.

## but wait...
to my surprised i noticed a blog post refuting this trick, indicating I'm from a previous era of instruction counting dinosaurs.

[this post](http://codingwiththomas.blogspot.com/2012/10/java-xor-swap-performance.html) lacked a github link and repeatability, even if you chase down the mentioned measurement (calipers) cited.  it prints zero's.  and if you debug it, without calling the blogged code.  this code also never actually used the product of the experiment, so it might've missed the point or taken advantage of hotspot being smart in one area and less aggressive in another.  

it doesn't need annotations or a test framework, or inheritance, so I set about seeing how things may have changed in 7 years from java 7 to java 12.

# results

the new code runs in main.  it tests 


 * hbb_4way	
  4 forward moving get/put heapbytebuffers particular to this benchmark

 * hbb_cpos	
  heap bytebuffer calling position instead of mark

 * hbb_mark	
  heapbytebuffer using mark and rewind naively

 * hbb_racc	
  heapbytebuffer random access get and put.

 * r64shift	
  creates a long, and pushes to it

 * tmp1swap	
  creates a tmp, swaps two array items

 * tmp2swap	
  creates 2 tmps, swaps two array items


 * xor_swap	
  xor swap

 * xor_tmps	
  we get some kind of optimization from creating two temps and swapping them and writing them, ony my cpu slightly tweaked by   the read and write order from the array.


some steady-state jvm results (lower is gooder)

```


---- for 100000000
tmp2swap: 100000000:  64 @1562500.0/ms
tmp1swap: 100000000:  69 @1449275.3623188406/ms
hbb_racc: 100000000:  69 @1449275.3623188406/ms
xor_tmps: 100000000:  75 @1333333.3333333333/ms
r64shift: 100000000:  166 @602409.6385542168/ms
xor_swap: 100000000:  172 @581395.3488372093/ms
hbb_4way: 100000000:  324 @308641.97530864197/ms
hbb_cpos: 100000000:  386 @259067.35751295337/ms
hbb_mark: 100000000:  459 @217864.92374727668/ms

---- for 1000000000
hbb_racc: 1000000000:  636 @1572327.0440251573/ms
tmp1swap: 1000000000:  637 @1569858.7127158556/ms
tmp2swap: 1000000000:  694 @1440922.190201729/ms
xor_tmps: 1000000000:  780 @1282051.282051282/ms
r64shift: 1000000000:  1741 @574382.5387708214/ms
xor_swap: 1000000000:  1807 @553403.4311012728/ms
hbb_4way: 1000000000:  3201 @312402.37425804435/ms
hbb_cpos: 1000000000:  3922 @254971.95308516064/ms
hbb_mark: 1000000000:  4395 @227531.28555176337/ms


```
