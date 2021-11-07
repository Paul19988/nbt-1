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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.flowpowered.nbt.regionfile.Chunk;
import com.flowpowered.nbt.regionfile.RegionFile;

public class RegionFileTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/**
	 * Test reading the NBT data in a region file
	 *
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testRead() throws IOException, URISyntaxException {
		try (RegionFile file = new RegionFile(Paths.get(getClass().getResource("/r.1.3.mca").toURI()));) {
			for (int i : file.listChunks()) {
				Chunk chunk = file.loadChunk(i);
				if (chunk != null)
					chunk.readTag();
			}
		}
	}

	@Test
	public void testCreateNew() throws IOException {
		File file = folder.newFile();
		file.delete();
		RegionFile.createNew(file.toPath()).close();
		file.delete();
		RegionFile rf = RegionFile.open(folder.newFolder().toPath().resolve("test").resolve("test.mca"));
		rf.writeChunks(new HashMap<>());
		assertEquals(4096 * 2, Files.size(rf.getPath()));
		rf.close();
	}
}
