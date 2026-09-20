package com.katruk.util;

public final class CompareUtil {

    private CompareUtil() {}

    public static double calculateStdDevOfDifferences(float[][] grid1, float[][] grid2) {
        int n = grid1.length;
        int m = grid1[0].length;
        int count = n * m;
        double[] differences = new double[count];
        double sum = 0;
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double diff = grid1[i][j] - grid2[i][j];
                differences[k++] = diff;
                sum += diff;
            }
        }
        double mean = sum / count;
        double sumSqDiff = 0;
        for (double diff : differences) {
            sumSqDiff += Math.pow(diff - mean, 2);
        }
        return Math.sqrt(sumSqDiff / count);
    }
}
