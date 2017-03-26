package com.jojos.report.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Creates statistics out of lists of numbers
 *
 * @author karanikasg@gmail.com
 */
public class Statistics {
	private static final Statistics NONE = new Statistics(0, 0, 0, 0, 0, 0, 0, 0, 0) {
		@Override
		public String toString() {
			return "NOT MEASURED";
		}
	};
	private static final Comparator<Number> DOUBLE_COMPARATOR = Comparator.comparingDouble(Number::doubleValue);
	private final double min;
	private final double max;
	private final double avg;
	private final double std;
	private final double median;
	private final double percentile99;
	private final double percentile95;
	private final double percentile90;
	private final int observations;

	private Statistics(double min,
                       double max,
                       double avg,
                       double std,
                       double median,
                       double percentile99,
                       double percentile95,
                       double percentile90,
                       int observations) {
		// NOTE: if more values are added, add them to toString() and machineReadable() as well!
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.std = std;
		this.median = median;
		this.percentile99 = percentile99;
		this.percentile95 = percentile95;
		this.percentile90 = percentile90;
		this.observations = observations;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getAvg() {
		return avg;
	}

	public double getStd() {
		return std;
	}

    public double getMedian() {
        return median;
    }

    public double get99thPercentile() {
		return percentile99;
	}

	public double get95thPercentile() {
		return percentile95;
	}

	public double get90thPercentile() {
		return percentile90;
	}

	public int getObservations() {
		return observations;
	}

	/**
	 * Calculate statistics for the list of numbers. The numbers will be treated like doubles, 
	 * so avoid sticking longs in here with values that are larger than ints...
	 * 
	 * @param values the collection of numbers
	 * @return statistics regarding these numbers
	 */
	public static Statistics calculate(Collection<? extends Number> values) {
		if (values.isEmpty()) {
			return NONE;
		}
		// copy here, as a queue isn't sortable
		List<Number> numbers = new ArrayList<>(values);
		// perform statistics on the time
		double min = Double.MAX_VALUE;
		double max = 0;
		double sumOfSquares = 0;
		double total = 0;
		double percentile99 = 0;
		int percentile99Index = (int) Math.ceil(0.99 * numbers.size()) - 1;
		double percentile95 = 0;
		int percentile95Index = (int) Math.ceil(0.95 * numbers.size()) - 1;
		double percentile90 = 0;
		int percentile90Index = (int) Math.ceil(0.9 * numbers.size()) - 1;
		numbers.sort(DOUBLE_COMPARATOR);

		double median;
        int middle = numbers.size() / 2;
		if (numbers.size() % 2 == 0) {
		    median = (numbers.get(middle - 1).doubleValue() + numbers.get(middle).doubleValue()) / 2d;
        } else {
		    median = numbers.get(middle).doubleValue();
        }

		for (int i = 0; i < numbers.size(); i++) {
			Number number = numbers.get(i);
			double value = number.doubleValue();
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
			total += value;
			sumOfSquares += value * value;
			if (i == percentile99Index) {
				percentile99 = value;
			}
			if (i == percentile95Index) {
				percentile95 = value;
			}
			if (i == percentile90Index) {
				percentile90 = value;
			}
		}
		double average = total / (double) numbers.size();
		double meanOfSquares = sumOfSquares / (double) numbers.size();
		double standardDeviation = Math.sqrt(meanOfSquares - (average * average));

		return new Statistics(min, max, average, standardDeviation, median, percentile99, percentile95, percentile90, numbers.size());
	}

	/**
	 * Creates a |-separated string with key|value pairs. The string neither starts nor ends with a |, 
	 * to avoid having to do trimming when joined with other values.
	 * 
	 * @return the values in a computer-readable format
	 */
	public String machineReadable() {
		return String.format(Locale.US,
			"Min|%.2f|Max|%.2f|99%%|%.2f|95%%|%.2f|90%%|%.2f|Average|%.2f|Standard deviation|%.2f|Median|%.2f|Observations|%d", min, max,
			percentile99, percentile95, percentile90, avg, std, median, observations);
	}

	@Override
	public String toString() {
		return String
			.format(
				Locale.US,
				"Statistics: Min: %8.2f, Max: %8.2f, 99%%: %8.2f, 95%%: %8.2f, 90%%: %8.2f, Average: %8.2f, Standard deviation: %8.2f, Median: %8.2f, Observations: %8d",
				min, max, percentile99, percentile95, percentile90, avg, std, median, observations);
	}
}
