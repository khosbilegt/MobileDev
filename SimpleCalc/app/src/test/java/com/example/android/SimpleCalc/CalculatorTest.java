/*
 * Copyright 2018, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.SimpleCalc;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

/**
 * JUnit4 unit tests for the calculator logic. These are local unit tests; no device needed
 */
@RunWith(JUnit4.class)
@SmallTest
public class CalculatorTest {

    private Calculator mCalculator;

    /**
     * Set up the environment for testing
     */
    @Before
    public void setUp() {
        mCalculator = new Calculator();
    }

    /**
     * Test for simple addition
     */
    @Test
    public void addTwoNumbers() {
        double resultAdd = mCalculator.add(1d, 1d);
        assertThat(resultAdd, is(equalTo(2d)));
    }

    @Test
    public void addTwoNumbersNegative() {
        double resultAdd = mCalculator.add(-1d, 2d);
        assertThat(resultAdd, is(equalTo(1d)));
    }

    @Test
    public void addTwoNumbersFloats() {
        double resultAdd = mCalculator.add(1.111f, 1.111d);
        assertThat(resultAdd, is(closeTo(2.222d, 0.01)));
    }

    @Test
    public void subTwoNumbers() {
        double resultSub = mCalculator.sub(2d, 2d);
        assertThat(resultSub, is(equalTo(0d)));
    }

    @Test
    public void subWorksWithNegativeResults() {
        double resultSub = mCalculator.sub(2d, 3d);
        assertThat(resultSub, is(equalTo(-1d)));
    }

    @Test
    public void mulTwoNumbers() {
        double resultMult = mCalculator.mul(2d, 3d);
        assertThat(resultMult, is(equalTo(6d)));
    }

    @Test
    public void mulTwoNumbersZero() {
        double resultMult = mCalculator.mul(2d, 0d);
        assertThat(resultMult, is(equalTo(0d)));
    }

    @Test
    public void divTwoNumbers() {
        double resultDiv = mCalculator.div(6d, 2d);
        assertThat(resultDiv, is(equalTo(3d)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void divByZeroThrows() {
        double resultDiv = mCalculator.div(6d, 0d);
        assertThat(resultDiv, is(equalTo(1d)));
    }

    @Test
    public void powPositive() {
        double resultPow = mCalculator.pow(2d, 2d);
        assertThat(resultPow, is(equalTo(4d)));
    }

    @Test
    public void powNegFirst() {
        double resultPow = mCalculator.pow(-2d, 2d);
        assertThat(resultPow, is(equalTo(4d)));
    }

    @Test
    public void powNegSecond() {
        double resultPow = mCalculator.pow(4d, -2d);
        assertThat(resultPow, is(equalTo(0.0625)));
    }

    @Test
    public void powZeroPos() {
        double resultPow = mCalculator.pow(0d, 2d);
        assertThat(resultPow, is(equalTo(0d)));
    }

    @Test
    public void powZero() {
        double resultPow = mCalculator.pow(5d, 0d);
        assertThat(resultPow, is(equalTo(1d)));
    }

    @Test
    public void powZeroNeg() {
        double resultPow = mCalculator.pow(0d, -1d);
        assertThat(resultPow, is(equalTo(Double.POSITIVE_INFINITY)));
    }

    @Test
    public void powZeroNegNeg() {
        double resultPow = mCalculator.pow(-0d, -1d);
        assertThat(resultPow, is(equalTo(-Double.POSITIVE_INFINITY)));
    }

}