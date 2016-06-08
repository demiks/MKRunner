package demilks.picreader;

import java.util.ArrayList;

/**
 * 二值化以后的图片对象
 * 
 * @author zhangming
 *
 */
public class Pic {
	// 二值化的数组
	private ArrayList<ArrayList<Integer>> points = new ArrayList<>();
	// 值
	private String key;

	private int columnCount;
	private int rowCount;

	/**
	 * 画一个点
	 * 
	 * @param column
	 * @param row
	 */
	public void draw(int row, int column) {
		int _column = column - 1;
		int _row = row - 1;
		if (row > rowCount) {
			rowCount = row;
			// 填充到最大行数
			while (points.size() < row) {
				points.add(new ArrayList<Integer>());
			}
		}
		if (column > columnCount) {
			// 填充0
			columnCount = column;
			points.forEach((rowData) -> {
				while (rowData.size() < columnCount) {
					rowData.add(0);
				}
			});
		}
		ArrayList<Integer> _row_ = points.get(_row);
		while (_row_.size() < columnCount) {
			_row_.add(0);
		}
		_row_.set(_column, 1);
	}

	/**
	 * 移除一个点
	 * 
	 * @param column
	 * @param row
	 */
	public void wipe(int row, int column) {
		if (column < 1 || column > columnCount || row < 1 || row > rowCount) {
			return;
		}
		int _column = column - 1;
		int _row = row - 1;
		ArrayList<Integer> _row_ = points.get(_row);
		_row_.set(_column, 0);
	}

	/**
	 * 判断指定点是否着色
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	public boolean hasPoint(int row, int column) {
		if (column < 1 || column > columnCount || row < 1 || row > rowCount) {
			return false;
		}
		int _column = column - 1;
		int _row = row - 1;
		ArrayList<Integer> _row_ = points.get(_row);
		return _row_.get(_column) == 1;
	}

	/**
	 * 压缩
	 * 
	 * @return
	 */
	public Pic trim() {
		Pic pic = this;
//		pic = pic.trimUp();
		pic = pic.trimLeft();
		pic = pic.trimUp();
		return pic;
	}

	/**
	 * 往上压缩
	 */
	private Pic trimUp() {
		// 每个点是否与上一行相同列的点相同，不相同的就重新画一个
		Pic pic = new Pic();
		for (int c = 1; c <= columnCount; c++) {
			int grow = 1;
			for (int r = 1; r <= rowCount; r++) {
				if (!this.hasPoint(r - 1, c)) {
					if (this.hasPoint(r, c)) {
						pic.draw(grow++, c);
					}
				} else {
					if (!this.hasPoint(r, c)) {
						grow++;
					}
				}

			}
		}
		return pic;
	}

	/**
	 * 往左压缩
	 * 
	 * @return
	 */
	private Pic trimLeft() {
		Pic pic = new Pic();
		for (int r = 1; r <= rowCount; r++) {
			// 处理同一行的点
			int grow = 1;
			for (int c = 1; c <= columnCount; c++) {
				if (!this.hasPoint(r, c - 1)) {
					if (this.hasPoint(r, c)) {
						pic.draw(r, grow ++);
					}
				} else {
					if (!this.hasPoint(r, c)) {
						grow ++;
					}
				}
			}
		}
		return pic;
	}

	public void print() {
		if (points == null || points.isEmpty()) {
			System.out.println("pic is null");
			return;
		}
		points.forEach((rowData) -> {
			rowData.forEach(data -> {
				System.out.print(data == 1 ? "*" : " ");
			});
			System.out.println();
		});
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public static void main(String[] args) {
		Pic p = new Pic();
		p.draw(2, 3);
		p.draw(9, 7);
		p.draw(9, 6);
		p.wipe(9, 6);
		p.wipe(10, 10);
		p.print();
		System.out.println(p.hasPoint(9, 7));
	}

}
