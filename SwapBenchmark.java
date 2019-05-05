package premature.optimization;

import java.util.Random;

public class SwapBenchmark {
    static Random random = new Random();

    public static void main(String[] args) {
        for (var rep = 1; rep < 11; rep++)
            for (var digits = 6; digits < 10; digits += 1) {
                var size = (int) Double.parseDouble("1E" + digits);
                var x = new int[size];
                for (var i = 0; i < x.length; i++) x[i] = random.nextInt();
                for (var swapper : swappers.values()) {
                    final var first = x[0];
                    var begin = System.currentTimeMillis();
                    swapper.swap(x);
                    var last = x[size - 1];
                    if (last != first) throw new Error("swap elevator fail");
                    var l = System.currentTimeMillis() - begin;
                    System.err.println(swapper + ": " + size + ":  " + l + " @" + (double) size / (double) l + "/ms");

                }
            }
    }


    enum swappers {
        xor() {
            void swap(int[] x) {
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    x[i] ^= x[j];
                    x[j] ^= x[i];
                    x[i] ^= x[j];
                }
            }
        }, r64 {
            @Override
            void swap(int[] x) {
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    final var x1 = x[i];
                    final var x2 = x[j];
                    var t = ((long) x1 & 0xffff_ffffL) << 32 | (x2 & 0xffff_ffffL);
                    x[i] = (int) (t & 0xffff_ffffL);
                    x[j] = (int) (t >>> 32);
                    assert x1 == x[j];
                    assert x2 == x[i];
                }
            }
        }, tmp {
            @Override
            void swap(int[] x) {
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
                    final var x1 = x[j];
                    final var t = x[i];
                    x[i] = x1;
                    x[j] = t;
                }
            }
        }, xtm {
            @Override
            void swap(int[] x) {
                for (var i = 0; i < x.length - 1; i++) {
                    final var j = i + 1;
//                    int v2, x2 =v2= x[j];
//                    int v1, x1 =v1= x[i];
                    var x2 = x[j];
                    var x1 = x[i];
                    x1 ^= x2;
                    x2 ^= x1;
                    x1 ^= x2;
                    x[i] = x1;
                    x[j] = x2;
//                    assert v1 == x[j];
//                    assert v2 == x[i];
                }
            }
        };

        abstract void swap(int[] x);
    }
}
