/**
 * MIT License
 *
 * Copyright (c) 2019 John Nahlen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package SourceStatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SourceCodeStatisticEngine {
	private SourceCodeStatisticConfiguration sourceCodeStatisticConfiguration;
	
	public SourceCodeStatisticEngine(SourceCodeStatisticConfiguration _sourceCodeStatisticConfiguration) throws Exception {
		sourceCodeStatisticConfiguration = _sourceCodeStatisticConfiguration;
		if (sourceCodeStatisticConfiguration != null) {
			Collections.sort(sourceCodeStatisticConfiguration.getExcludePaths());
			Collections.sort(sourceCodeStatisticConfiguration.getExcludeSuffixes());
		} else {
			throw new Exception("Invalid Configuration Provided.");
		}
	}
	
	public List<Map.Entry<String,SourceCodeStatistic>> run(File path) throws IOException {
		Map<String,SourceCodeStatistic> map = process(path);
		//
		// Indicate which method of sorting to use
		//
		List<Map.Entry<String, SourceCodeStatistic>> mapEntries = new ArrayList<>(map.entrySet());
		mapEntries.sort(new Comparator<Map.Entry<String, SourceCodeStatistic>>() {
			@Override
			public int compare(Map.Entry<String, SourceCodeStatistic> o1, Map.Entry<String, SourceCodeStatistic> o2) {
				if (sourceCodeStatisticConfiguration.getSortType() == SourceCodeStatisticConfiguration.Sort.FILENAME) {
					return o1.getKey().compareTo(o2.getKey());
				} else if (sourceCodeStatisticConfiguration.getSortType() == SourceCodeStatisticConfiguration.Sort.NUM_LINES) {
					Integer i1 = o1.getValue().numLines;
					Integer i2 = o2.getValue().numLines;
					return i2.compareTo(i1);
				}
				return 0;
			}
		});
		return mapEntries;
	}
	
	private Map<String,SourceCodeStatistic> process(File path) throws IOException {
		Map<String,SourceCodeStatistic> map = new HashMap<>();
		
		if (!path.isDirectory()) {
			return map;
		}
		
		File[] files = path.listFiles();
		if (files != null) {
			for(File file : files) {
				if (Collections.binarySearch(sourceCodeStatisticConfiguration.getExcludePaths(),file.getName()) >= 0) {
					continue;
				}
				
				if (file.isDirectory()) {
					Map<String,SourceCodeStatistic> recursed = process(file);
					if (recursed != null) {
						map.putAll(recursed);
					}
					continue;
				}
				
				boolean exclude = false;
				for(String suffix : sourceCodeStatisticConfiguration.getExcludeSuffixes()) {
					if (file.getName().endsWith(suffix)) {
						exclude = true;
						break;
					}
				}
				if (exclude) {
					continue;
				}
				
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				
				SourceCodeStatistic sourceCodeStatistic = new SourceCodeStatistic();
				
				int numLines = 0;
				int numEmptyLines = 0;
				int numChars = 0;
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					line = line.trim();
					if (line.length() == 0) {
						numEmptyLines++;
						continue;
					}
					numLines++;
					numChars += line.length();
				}
				bufferedReader.close();
				
				sourceCodeStatistic.numLines = numLines;
				sourceCodeStatistic.numEmptyLines = numEmptyLines;
				sourceCodeStatistic.numChars = numChars;
				map.put(file.getAbsolutePath(),sourceCodeStatistic);
			}
		}
		return map;
	}
}
