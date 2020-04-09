/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gpv.util;

import static gpv.util.Coordinate.makeCoordinate;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

/**
 * Description
 * @version Apr 8, 2020
 */
class CoordinateTests
{

	@ParameterizedTest
	@MethodSource("distanceProvider")
	void distanceToTests(int expected, int x1, int y1, int x2, int y2)
	{
		assertEquals(expected, makeCoordinate(x1, y1).distanceTo(makeCoordinate(x2, y2)));
	}
	
	static Stream<Arguments> distanceProvider()
	{
		return Stream.of(
				Arguments.of(1, 1, 5, 2, 5),
				Arguments.of(2, 2, 5, 4, 5),
				Arguments.of(2, 3, 4, 1, 4),
				Arguments.of(3, 6, 3, 3, 6)
		);
	}

}
