package premature.optimization;

import java.util.Random;

public class SwapBenchmark {
    static Random random = new Random();

    public static void main(String[] args) {
        for (var rep = 1; rep < 11; rep++)
            for (var digits =6; digits < 10; digits += 1) {
                var size = (int) Double.parseDouble("1E" + digits);
                var x = new int[size];
                for (var i = 0; i < x.length; i++) x[i] = random.nextInt();
                for (var swapper : swappers.values()) {
                    var last = 0;
                    final var first = x[0];
                    var begin = System.currentTimeMillis();
                    last = swapper.Swap(x);
                    if (last != first) throw new Error("swap elevator fail");
                    var l = System.currentTimeMillis() - begin;
                    System.err.println(swapper+ ": " + size + ":  " + l + " @" + (double) size / (double) l + "/ms");

                } 
            }
    }


    enum swappers {
        xorSwap() {
            int Swap(int[] x) {
                var last = 0;
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    x[i] = x[i] ^ x[j];
                    x[j] = x[j] ^ x[i];
                    x[i] = x[i] ^ x[j];
                    last = x[j];
                }
                return last;
            }
        }, r64Swap {
            @Override
            int Swap(int[] x) {

                var last = 0;
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    var x1 = x[i];
                    var x2 = x[j];
                    var t = ((long) x1) << 32 | (x2 & 0xffff_ffffL);
                    x[i] = (int) (t & 0xffff_ffffL);
                    x[j] = (int) (t >> 32);
                    last = x[j];
                }
                return last;
            }
        }, tmpSwap {
            @Override
            int Swap(int[] x) {
                int t;
                var last = 0;
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    t = x[i];
                    x[i] = x[j];
                    x[j] = t;
                    last = x[j];
                }
                return last;
            }
        }, xorTmp {
            @Override
            int Swap(int[] x) {
                var last = 0;
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    var x1 = x[i];
                    var x2 = x[j];
                    x1 = x1 ^ x2;
                    x2 = x1 ^ x2;
                    x1 = x1 ^ x2;
                    x[i] = x1;
                    x[j] = x2;
                    last = x[j];
                }
                return last;
            }
        };
        abstract int Swap(int[] x);
    }
}
