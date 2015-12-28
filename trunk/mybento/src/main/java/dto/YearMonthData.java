package dto;

import java.io.Serializable;

public class YearMonthData implements Serializable {
	private int yearMonth;

	public YearMonthData(int yearMonth) {
		this.yearMonth = yearMonth;
	}

	public int getYearMonth() {
		return yearMonth;
	}

	public int getYear() {
		return yearMonth / 100;
	}

	public int getMonth() {
		return yearMonth % 100;
	}
}
