package com.flowpowered.nbt.stream;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.regionfile.RegionFile;
import com.flowpowered.nbt.regionfile.RegionFile.RegionChunk;

public class NBTInputStreamTest {

	/** Read a simple NBT file and compare it to a previous result */
	@Test
	public void testNBT() throws IOException, URISyntaxException {
		try (NBTInputStream in = new NBTInputStream(getClass().getResourceAsStream("/level.dat"), NBTInputStream.GZIP_COMPRESSION)) {
			Tag<?> tag = in.readTag();
			assertArrayEquals(
					Files.readAllLines(Paths.get(getClass().getResource("/level.txt").toURI())).toArray(new String[] {}),
					tag.toString().split("\r\n"));
		}
	}

	/**
	 * Test reading the NBT data in a region file
	 *
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testRegionFile() throws IOException, URISyntaxException {
		try (RegionFile file = new RegionFile(Paths.get(getClass().getResource("/r.1.3.mca").toURI()))) {
			for (RegionChunk chunk : file) {
				if (chunk.getData() != null)
					chunk.getData().readTag();
			}
		}
	}
}