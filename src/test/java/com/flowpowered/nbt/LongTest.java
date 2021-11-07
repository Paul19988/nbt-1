/*
 * This file is part of Flow NBT, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2011 Flow Powered <https://flowpowered.com/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.nbt;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Random;

import org.junit.Test;

import com.flowpowered.nbt.regionfile.Chunk;

public class LongTest {

	@Test
	public void test() {
		for (int BITS = 4; BITS <= 12; BITS++) {
			Random random = new Random(1234);

			// Fill a byte buffer with random data
			ByteBuffer buffer = ByteBuffer.wrap(new byte[BITS * 64 * 8]);
			buffer.order(ByteOrder.BIG_ENDIAN);
			random.nextBytes(buffer.array());

			// Convert it to a long buffer
			LongBuffer data = buffer.asLongBuffer();
			data.rewind();
			long[] longData = new long[BITS * 64];
			data.get(longData);

			// Control data: Convert all bytes to a binary string and zero-pad them, append them to a large bit string
			// Slicing the bit string into equal length substrings will give the control data.
			// Add a double reverse because Mojang does silly stuff sometimes.
			StringBuffer number = new StringBuffer();
			for (long l : longData)
				number.append(convertLong(Long.reverse(l)));
			for (int i = 0; i < BITS * 64; i++) {
				// Compare conversion with control data
				assertEquals(Long.parseLong(new StringBuilder(number.substring(i * BITS, i * BITS + BITS)).reverse().toString(), 2),
						Chunk.extractFromLong(longData, i, BITS));
			}
		}
	}

	/** Convert long to binary string and zero pad it */
	private static String convertLong(long l) {
		String s = Long.toBinaryString(l);
		// Fancy way of zero padding :)
		s = "0000000000000000000000000000000000000000000000000000000000000000".substring(s.length()) + s;
		return s;
	}
}
