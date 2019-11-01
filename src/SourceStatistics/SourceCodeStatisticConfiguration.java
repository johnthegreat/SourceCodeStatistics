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

import java.util.List;

public class SourceCodeStatisticConfiguration {
	private Sort sortType = Sort.FILENAME;
	private List<String> excludePaths;
	private List<String> excludeSuffixes;
	
	public SourceCodeStatisticConfiguration() {
	
	}
	
	public void setSortType(Sort sortType) {
		this.sortType = sortType;
	}
	
	public void setExcludePaths(List<String> excludePaths) {
		this.excludePaths = excludePaths;
	}
	
	public void setExcludeSuffixes(List<String> excludeSuffixes) {
		this.excludeSuffixes = excludeSuffixes;
	}
	
	public Sort getSortType() {
		return sortType;
	}
	
	public List<String> getExcludePaths() {
		return excludePaths;
	}
	
	public List<String> getExcludeSuffixes() {
		return excludeSuffixes;
	}
	
	public enum Sort {
		FILENAME, NUM_LINES;
	}
}