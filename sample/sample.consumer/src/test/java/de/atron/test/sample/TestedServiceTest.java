/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ATRON electronic GmbH ("ATRON") CONFIDENTIAL                               *
 * Unpublished Copyright (c) 2008-2020 ATRON electronic GmbH, All Rights      *
 * Reserved.                                                                  *
 *                                                                            *
 * NOTICE: All information contained herein is, and remains the property of   *
 * ATRON. The intellectual and technical concepts contained herein are        *
 * proprietary to ATRON and may be covered by U.S. and Foreign Patents,       *
 * patents in process, and are protected by trade secret or copyright law.    *
 * Dissemination of this information or reproduction of this material is      *
 * strictly forbidden unless prior written permission is obtained             *
 * from ATRON. Access to the source code contained herein is hereby forbidden *
 * to anyone except current ATRON employees, managers or contractors who have *
 * executed. Confidentiality and Non-disclosure agreements explicitly         *
 * covering such access.                                                      *
 *                                                                            *
 *                                                                            *
 * The copyright notice above does not evidence any actual or intended        *
 * publication or disclosure of this source code, which includes information  *
 * that is confidential and/or proprietary, and is a trade secret, of ATRON.  *
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR       *
 * PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS   *
 * WRITTEN CONSENT OF ATRON IS STRICTLY PROHIBITED, AND IN VIOLATION OF       *
 * APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF   *
 * THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY   *
 * RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO            *
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN    *
 * PART.                                                                      *
 *                                                                            *
 * $$Id: $$                                                                   *
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package de.atron.test.sample;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Uwe Plonus
 */
public class TestedServiceTest {

    @Mock(name = "mocked")
    private MockedInterface mockedInterface;

    @InjectMocks
    private TestedService objectToTest;

    @Before
    public void setUp() {
        this.objectToTest = new TestedService();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldDividePositiveNumbers() {
        // given
        double dividend = 10.0d;
        double divisor = 2.0d;
        Mockito.when(this.mockedInterface.divide(dividend, divisor)).thenReturn(5.0d);

        // when
        double result = this.objectToTest.positiveDivide(dividend, divisor);

        // then
        Assertions.assertThat(result).isEqualTo(5.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNegativeDividend() {
        // given
        double dividend = -10.0d;
        double divisor = 2.0d;

        // when
        double result = this.objectToTest.positiveDivide(dividend, divisor);

        // then
        // Exception thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNegativeDivisor() {
        // given
        double dividend = 10.0d;
        double divisor = -2.0d;

        // when
        double result = this.objectToTest.positiveDivide(dividend, divisor);

        // then
        // Exception thrown
    }

}
