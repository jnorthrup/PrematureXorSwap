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
tmp2swap: 100000000:  63 @1587301.5873015872/ms
tmp1swap: 100000000:  66 @1515151.5151515151/ms
hbb_racc: 100000000:  67 @1492537.3134328357/ms
xor_tmps: 100000000:  75 @1333333.3333333333/ms
r64shift: 100000000:  166 @602409.6385542168/ms
xor_swap: 100000000:  174 @574712.643678161/ms
hbb_4way: 100000000:  325 @307692.3076923077/ms
hbb_cpos: 100000000:  380 @263157.8947368421/ms
hbb_mark: 100000000:  428 @233644.8598130841/ms

---- for 1000000000
tmp1swap: 1000000000:  664 @1506024.096385542/ms
tmp2swap: 1000000000:  676 @1479289.9408284023/ms
hbb_racc: 1000000000:  872 @1146788.990825688/ms
xor_tmps: 1000000000:  889 @1124859.392575928/ms
r64shift: 1000000000:  1756 @569476.0820045559/ms
xor_swap: 1000000000:  1812 @551876.3796909492/ms
hbb_4way: 1000000000:  3412 @293083.23563892144/ms
hbb_cpos: 1000000000:  4038 @247647.35017335316/ms
hbb_mark: 1000000000:  4618 @216543.95842356/ms


```
