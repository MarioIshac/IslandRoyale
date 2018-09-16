/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package me.theeninja.islandroyale;

import com.badlogic.gdx.math.MathUtils;

/** Adapted from <a href="http://devmag.org.za/2009/04/25/perlin-noise/">http://devmag.org.za/2009/04/25/perlin-noise/</a>
 * @author badlogic */
public class PerlinNoiseGenerator {
    public static float[][] generateWhiteNoise (int width, int height) {
        float[][] noise = new float[width][height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                noise[x][y] = MathUtils.random();

        return noise;
    }

    public static float interpolate(float x0, float x1, float alpha) {
        return x0 * (1 - alpha) + alpha * x1;
    }

    public static float[][] generateSmoothNoise (float[][] baseNoise, int octave) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        float[][] smoothNoise = new float[width][height];

        int samplePeriod = 1 << octave; // calculates 2 ^ k
        float sampleFrequency = 1.0f / samplePeriod;
        for (int xTile = 0; xTile < width; xTile++) {
            int leftXTile = xTile - xTile % samplePeriod;
            int rightXTile = (leftXTile + samplePeriod) % width; // wrap around
            float horizontalBlend = (xTile - leftXTile) * sampleFrequency;

            for (int yTile = 0; yTile < height; yTile++) {
                int bottomYTile = (yTile / samplePeriod) * samplePeriod;
                int topYTile = (bottomYTile + samplePeriod) % height; // wrap around
                float verticalBlend = (yTile - bottomYTile) * sampleFrequency;
                float bottomTime = interpolate(baseNoise[leftXTile][bottomYTile], baseNoise[rightXTile][bottomYTile], horizontalBlend);
                float upperTile = interpolate(baseNoise[leftXTile][topYTile], baseNoise[rightXTile][topYTile], horizontalBlend);

                //noinspection SuspiciousNameCombination
                smoothNoise[xTile][yTile] = interpolate(bottomTime, upperTile, verticalBlend);
            }
        }

        return smoothNoise;
    }

    public static float[][] generatePerlinNoise (float[][] baseNoise, int octaveCount) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D arrays containing
        float persistance = 0.8f;

        for (int i = 0; i < octaveCount; i++)
            smoothNoise[i] = generateSmoothNoise(baseNoise, i);

        float[][] perlinNoise = new float[width][height]; // an array of floats initialised to 0

        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;

        for (int octave = octaveCount - 1; octave >= 0; octave--) {
            amplitude *= persistance;
            totalAmplitude += amplitude;

            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
        }

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                perlinNoise[i][j] /= totalAmplitude;

        return perlinNoise;
    }

    public static float[][] generatePerlinNoise (int width, int height, int octaveCount) {
        float[][] baseNoise = generateWhiteNoise(width, height);
        return generatePerlinNoise(baseNoise, octaveCount);
    }

    public static float[][] generateHeightMap(int width, int height, float min, float max, int octaveCount) {
        float[][] baseNoise = generateWhiteNoise(width, height);
        float[][] noise = generatePerlinNoise(baseNoise, octaveCount);
        float[][] heights = new float[width][height];
        float range = max - min;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                heights[x][y] = noise[x][y] * range + min;
            }
        }
        return heights;
    }
}