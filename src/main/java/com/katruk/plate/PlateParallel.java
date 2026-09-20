package com.katruk.plate;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class PlateParallel implements Plate {

    private final float north;
    private final float south;
    private final float east;
    private final float west;
    private float[][] temperatures;
    private final int numProcessors = 4;

    public PlateParallel(int height, int width, float north, float east, float south, float west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.temperatures = new float[height][width];
    }

    @Override
    public void calculatedTemperatureDistribution(final float tolerance, final String fileName) {
        this.temperatures = initTemperatures(this.temperatures, this.north, this.east, this.south, this.west);
        int n = this.temperatures.length;
        int m = this.temperatures[0].length;

        float[][][] grids = new float[2][n][m];
        grids[0] = copyTemperatures(this.temperatures, n, m);
        grids[1] = copyTemperatures(this.temperatures, n, m);

        float[] localMaxDiffs = new float[numProcessors];
        final boolean[] converged = {false};
        final int[] currentIdx = {0};

        CyclicBarrier barrier = new CyclicBarrier(numProcessors, () -> {
            float maxDiff = 0;
            for (float d : localMaxDiffs) {
                if (d > maxDiff) {
                    maxDiff = d;
                }
            }
            if (maxDiff < tolerance) {
                converged[0] = true;
            }
            currentIdx[0] = 1 - currentIdx[0];
        });

        Thread[] threads = new Thread[numProcessors];
        for (int i = 0; i < numProcessors; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                int interiorHeight = n - 2;
                int rowsPerThread = interiorHeight / numProcessors;
                int startRow = 1 + id * rowsPerThread;
                int endRow = (id == numProcessors - 1) ? n - 1 : 1 + (id + 1) * rowsPerThread;

                while (!converged[0]) {
                    int oldIdx = currentIdx[0];
                    int newIdx = 1 - oldIdx;
                    float localMax = 0;

                    for (int r = startRow; r < endRow; r++) {
                        for (int c = 1; c < m - 1; c++) {
                            grids[newIdx][r][c] = (grids[oldIdx][r - 1][c] + grids[oldIdx][r + 1][c]
                                    + grids[oldIdx][r][c - 1] + grids[oldIdx][r][c + 1]) / 4.0f;
                            float diff = Math.abs(grids[newIdx][r][c] - grids[oldIdx][r][c]);
                            if (diff > localMax) {
                                localMax = diff;
                            }
                        }
                    }
                    localMaxDiffs[id] = localMax;

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.temperatures = grids[currentIdx[0]];
        saveToFile(this.temperatures, fileName);
    }

    private float[][] initTemperatures(final float[][] temperatures, final float north,
                                       final float east, final float south, final float west) {
        int height = temperatures.length;
        int width = temperatures[0].length;
        for (int j = 0; j < width; j++) {
            temperatures[0][j] = north;
        }
        for (int i = 0; i < height; i++) {
            temperatures[i][width - 1] = east;
        }
        for (int j = 0; j < width; j++) {
            temperatures[height - 1][j] = south;
        }
        for (int i = 0; i < height; i++) {
            temperatures[i][0] = west;
        }
        return temperatures;
    }

    private float[][] copyTemperatures(float[][] tempArray, int n, int m) {
        float[][] newTemperatures = new float[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tempArray[i], 0, newTemperatures[i], 0, m);
        }
        return newTemperatures;
    }

    private void saveToFile(float[][] temperatures, String fileName) {
        File jsonFile = new File(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(jsonFile, temperatures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printTemperatures() {
        int m = this.temperatures[0].length;
        for (float[] temperature : this.temperatures) {
            for (int j = 0; j < m; j++) {
                System.out.printf("%.2f  ", temperature[j]);
            }
            System.out.print("\n");
        }
    }

    @Override
    public void imageTemperatures(final String fileName, final Color color) {
        BufferedImage image = makeImage(this.temperatures, color);
        File file = new File(fileName);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage makeImage(float[][] temperatures, final Color color) {
        int height = temperatures.length;
        int width = temperatures[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int a = 255;
        int red = 0;
        int green = 0;
        int blue = 0;
        float max = getMax(temperatures);
        float min = getMin(temperatures);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                switch (color) {
                    case RED:
                        red = normalize(temperatures[y][x], max, min);
                        break;
                    case GREEN:
                        green = normalize(temperatures[y][x], max, min);
                        break;
                    case BLUE:
                        blue = normalize(temperatures[y][x], max, min);
                        break;
                }
                int pixel = (a << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }

    private int normalize(float value, float max, float min) {
        if (max == min) return 0;
        final int absoluteMax = 255;
        return (int) (((value - min) * absoluteMax) / (max - min));
    }

    private float getMin(float[][] temperatures) {
        float result = temperatures[0][0];
        int width = temperatures[0].length;
        for (float[] temperature : temperatures) {
            for (int j = 0; j < width; j++) {
                if (result > temperature[j]) {
                    result = temperature[j];
                }
            }
        }
        return result;
    }

    private float getMax(float[][] temperatures) {
        float result = temperatures[0][0];
        int width = temperatures[0].length;
        for (float[] temperature : temperatures) {
            for (int j = 0; j < width; j++) {
                if (result < temperature[j]) {
                    result = temperature[j];
                }
            }
        }
        return result;
    }

    public float[][] getTemperatures() {
        return temperatures;
    }
}
