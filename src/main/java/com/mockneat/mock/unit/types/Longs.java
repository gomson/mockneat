package com.mockneat.mock.unit.types;

/**
 * Copyright 2017, Andrei N. Ciobanu

 Permission is hereby granted, free of charge, to any user obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.mockneat.mock.MockNeat;
import com.mockneat.mock.interfaces.MockUnitLong;
import com.mockneat.mock.utils.ValidationUtils;

import java.util.Random;
import java.util.function.Supplier;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Longs implements MockUnitLong {

    private MockNeat mock;
    private Random random;

    public Longs(MockNeat mock) {
        this.mock = mock;
        this.random = mock.getRandom();
    }

    @Override
    public Supplier<Long> supplier() {
        return random::nextLong;
    }

    public MockUnitLong bound(long bound) {
        isTrue(bound>=0, ValidationUtils.LOWER_BOUND_BIGGER_THAN_ZERO);
        Supplier<Long> supplier = () -> {
            long b;
            long result;
            do {
                b = (random.nextLong() << 1) >>> 1;
                result = b % bound;
            } while (b-result+bound-1 < 0L);

            return result;
        };
        return () -> supplier;
    }

    public MockUnitLong range(long lowerBound, long upperBound) {
        notNull(lowerBound, ValidationUtils.INPUT_PARAMETER_NOT_NULL, "lowerBound");
        notNull(upperBound, ValidationUtils.INPUT_PARAMETER_NOT_NULL, "upperBound");
        isTrue(lowerBound>=0, ValidationUtils.LOWER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>0, ValidationUtils.UPPER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>lowerBound, ValidationUtils.UPPER_BOUND_BIGGER_LOWER_BOUND);
        Supplier<Long> supplier = () ->
                mock.longs().bound(upperBound - lowerBound).val() + lowerBound;
        return () -> supplier;
    }

    public MockUnitLong from(long[] alphabet) {
        ValidationUtils.notEmpty(alphabet, ValidationUtils.INPUT_PARAMETER_NOT_NULL_OR_EMPTY, "alphabet");
        Supplier<Long> supp = () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
        return () -> supp;
    }

}
