/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import static gpv.chess.ChessPieceDescriptor.*;
import static org.junit.Assert.*;
import static gpv.util.Coordinate.makeCoordinate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import gpv.util.Board;

/**
 * Tests to ensure that pieces are created correctly and that all pieces
 * get created.
 * @version Feb 23, 2020
 */
class ChessPieceTests
{
	private static ChessPieceFactory factory = null;
	private Board board;
	
	@BeforeAll
	public static void setupBeforeTests()
	{
		factory = new ChessPieceFactory();
	}
	
	@BeforeEach
	public void setupTest()
	{
		board = new Board(8, 8);
	}
	
	@Test
	void makePiece()
	{
		ChessPiece pawn = factory.makePiece(WHITEPAWN);
		assertNotNull(pawn);
	}
	
	/**
	 * This type of test loops through each value in the Enum and
	 * one by one feeds it as an argument to the test method.
	 * It's worth looking at the different types of parameterized
	 * tests in JUnit: 
	 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
	 * @param d the Enum value
	 */
	@ParameterizedTest
	@EnumSource(ChessPieceDescriptor.class)
	void makeOneOfEach(ChessPieceDescriptor d)
	{
		ChessPiece p = factory.makePiece(d);
		assertNotNull(p);
		assertEquals(d.getColor(), p.getColor());
		assertEquals(d.getName(), p.getName());
	}

	@Test
	void placeOnePiece()
	{
		ChessPiece p = factory.makePiece(BLACKPAWN);
		board.putPieceAt(p, makeCoordinate(2, 2));
		assertEquals(p, board.getPieceAt(makeCoordinate(2, 2)));
	}

	@Test
	void placeTwoPieces()
	{
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		assertEquals(bn, board.getPieceAt(makeCoordinate(3, 5)));
		assertEquals(wb, board.getPieceAt(makeCoordinate(2, 6)));
	}
	
	@Test
	void checkForPieceHasMoved()
	{
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		assertFalse(bq.hasMoved());
		bq.setHasMoved();
		assertTrue(bq.hasMoved());
	}
	
	@Test
	void PieceCannotStayStill() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(2, 5), board));
	}
	
	@Test
	void PieceCannotMoveToInvalidSpace1() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(0, 5), board));
	}
	
	@Test
	void PieceCannotMoveToInvalidSpace2() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(1, 0), board));
	}
	@Test
	void PieceCannotMoveToInvalidSpace3() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(10, 5), board));
	}
	@Test
	void PieceCannotMoveToInvalidSpace4() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(1, 10), board));
	}
	
	@Test
	void PieceCannotMoveToOccupiedSpaceofSameColor() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece wp2 = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		board.putPieceAt(wp2, makeCoordinate(3,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(3, 5), board));
	}
	
	@Test
	void KingMoveOneSpace() 
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(3, 6), board));
	}
	
	@Test
	void KingCannotMoveTwoSpaces() 
	{
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(2,5));
		assertTrue(wk.canMove(makeCoordinate(2,5), makeCoordinate(4, 6), board));
	}
	
	@Test
	void PawnMoveForward()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(3, 5), board));
	}
	
	@Test
	void PawnMoveForwardTwoSpaces()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(4, 5), board));
	}
	
	@Test
	void PawnTryMoveTwoSpacesWhenAlreadyMoved()
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		wp.setHasMoved();
		board.putPieceAt(wp, makeCoordinate(2,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(4, 5), board));
	}
	
	@Test
	void PawnCannotMoveForwardTwoSpaces() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece wp2 = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		board.putPieceAt(wp2, makeCoordinate(4,5));
		assertFalse(wp.canMove(makeCoordinate(2,5), makeCoordinate(4, 5), board));
	}
	
	@Test
	void PawnCapture() 
	{
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(wp, makeCoordinate(2,5));
		board.putPieceAt(bp, makeCoordinate(3,6));
		assertTrue(wp.canMove(makeCoordinate(2,5), makeCoordinate(3, 6), board));
	}
	
	@Test
	void RookMoveUp() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(4,4));
		assertTrue(wr.canMove(makeCoordinate(4,4), makeCoordinate(7, 4), board));
	}
	
	@Test
	void RookMoveRight() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(4,4));
		assertTrue(wr.canMove(makeCoordinate(4,4), makeCoordinate(4, 7), board));
	}
	
	@Test
	void RookMoveLeft() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(4,4));
		assertTrue(wr.canMove(makeCoordinate(4,4), makeCoordinate(4, 1), board));
	}
	
	@Test
	void RookMoveDown() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		board.putPieceAt(wr, makeCoordinate(4,4));
		assertTrue(wr.canMove(makeCoordinate(4,4), makeCoordinate(1, 4), board));
	}
	
	@Test
	void RookMoveUpBlocked() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(6,4));
		assertFalse(wr.canMove(makeCoordinate(4,4), makeCoordinate(7, 4), board));
	}
	
	@Test
	void RookMoveRightBlocked() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(4,6));
		assertFalse(wr.canMove(makeCoordinate(4,4), makeCoordinate(4, 7), board));
	}
	
	@Test
	void RookMoveLeftBlocked() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(4,2));
		assertFalse(wr.canMove(makeCoordinate(4,4), makeCoordinate(4, 1), board));
	}
	
	@Test
	void RookMoveDownBlocked() 
	{
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wr, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(2,4));
		assertFalse(wr.canMove(makeCoordinate(4,4), makeCoordinate(1, 4), board));
	}
	
	@Test
	void BishopMoveUpAndRight() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4,4));
		assertTrue(wb.canMove(makeCoordinate(4,4), makeCoordinate(7, 7), board));
	}
	
	@Test
	void BishopMoveUpAndLeft() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4,4));
		assertTrue(wb.canMove(makeCoordinate(4,4), makeCoordinate(7, 1), board));
	}
	
	@Test
	void BishopMoveDownAndRight() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4,4));
		assertTrue(wb.canMove(makeCoordinate(4,4), makeCoordinate(1, 7), board));
	}
	
	@Test
	void BishopMoveDownAndLeft() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(wb, makeCoordinate(4,4));
		assertTrue(wb.canMove(makeCoordinate(4,4), makeCoordinate(1, 1), board));
	}
	
	@Test
	void BishopMoveUpAndRightBlocked() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(6,6));
		assertFalse(wb.canMove(makeCoordinate(4,4), makeCoordinate(7, 7), board));
	}
	
	@Test
	void BishopMoveUpAndLeftBlocked() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(6,2));
		assertFalse(wb.canMove(makeCoordinate(4,4), makeCoordinate(7, 1), board));
	}
	
	@Test
	void BishopMoveDownAndRightBlocked() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(2,6));
		assertFalse(wb.canMove(makeCoordinate(4,4), makeCoordinate(1, 7), board));
	}
	
	@Test
	void BishopMoveDownAndLeftBlocked() 
	{
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		board.putPieceAt(wb, makeCoordinate(4,4));
		board.putPieceAt(wp, makeCoordinate(2,2));
		assertFalse(wb.canMove(makeCoordinate(4,4), makeCoordinate(1, 1), board));
	}
	
	@Test
	void KnightMoveUpAndRight() 
	{
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wk, makeCoordinate(4,4));
		assertTrue(wk.canMove(makeCoordinate(4,4), makeCoordinate(5, 6), board));
	}
	
	@Test
	void KnightMoveUpAndLeft() 
	{
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wk, makeCoordinate(4,4));
		assertTrue(wk.canMove(makeCoordinate(4,4), makeCoordinate(6, 3), board));
	}
	
	@Test
	void KnightMoveDownAndLeft() 
	{
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wk, makeCoordinate(4,4));
		assertTrue(wk.canMove(makeCoordinate(4,4), makeCoordinate(3, 2), board));
	}
	
	@Test
	void KnightMoveDownAndRight() 
	{
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		board.putPieceAt(wk, makeCoordinate(4,4));
		assertTrue(wk.canMove(makeCoordinate(4,4), makeCoordinate(2, 5), board));
	}
	
	
}
