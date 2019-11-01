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

import java.io.*;
import java.util.*;

/**
 *
 */
public class Main {
	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//
		// Make sure at least one argument (the path) is specified.
		//
		if (args.length == 0) {
			System.err.println("Path not specified.");
			System.exit(-1);
		}
		
		
		File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			System.err.println("Path is not directory.");
			System.exit(-1);
		}
		// TODO Do we need to test if the directory is readable?
		
		//
		// Create configuration
		//
		
		// Create list of excluded files / folders
		List<String> excludes = new ArrayList<>();
		// .git, node_modules, etc
		
		// Create list of suffixes
		List<String> excludeSuffixes = new ArrayList<>();
		// .jpg, .png, etc
		
		SourceCodeStatisticConfiguration config = new SourceCodeStatisticConfiguration();
		config.setExcludePaths(excludes);
		config.setExcludeSuffixes(excludeSuffixes);
		
		//
		// Create engine and pass configuration
		//
		
		SourceCodeStatisticEngine engine = new SourceCodeStatisticEngine(config);
		List<Map.Entry<String, SourceCodeStatistic>> list = engine.run(dir);
		List<SourceCodeStatistic> sourceCodeStatistics = new ArrayList<>(list.size());
		
		System.out.println(String.format("%-100s\t%10s\t%10s","File Name","Lines","Num Chars"));
		for(Map.Entry<String,SourceCodeStatistic> mapEntry : list) {
			SourceCodeStatistic sourceCodeStatistic = mapEntry.getValue();
			sourceCodeStatistics.add(sourceCodeStatistic);
			System.out.println(String.format("%-100s\t%10d\t%10d", mapEntry.getKey(), sourceCodeStatistic.numLines, sourceCodeStatistic.numChars));
		}
		
		SourceCodeStatistic totalStatistics = SourceCodeStatisticUtils.aggregate(sourceCodeStatistics.toArray(new SourceCodeStatistic[0]));
		System.out.println();
		System.out.println(String.format("Total non-empty lines: %d",totalStatistics.numLines));
		System.out.println(String.format("Total characters: %d",totalStatistics.numChars));
	}
}
