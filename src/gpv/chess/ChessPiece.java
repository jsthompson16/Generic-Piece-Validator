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

import gpv.Piece;
import gpv.util.*;
import static gpv.util.Coordinate.makeCoordinate;

/**
 * The chess piece is a piece with some special properties that are used for
 * determining whether a piece can move. It implements the Piece interface
 * and adds properties and methods that are necessary for the chess-specific
 * behavior.
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor>
{
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved;	// true if this piece has moved
	
	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
	 * @param descriptor
	 */
	public ChessPiece(ChessPieceDescriptor descriptor)
	{
		this.descriptor = descriptor;
		hasMoved = false;
	}

	/*
	 * @see gpv.Piece#getDescriptor()
	 */
	@Override
	public ChessPieceDescriptor getDescriptor()
	{
		return descriptor;
	}
	
	/**
	 * @return the color
	 */
	public PlayerColor getColor()
	{
		return descriptor.getColor();
	}

	/**
	 * @return the name
	 */
	public PieceName getName()
	{
		return descriptor.getName();
	}

	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b)
	{
		int fromColumn = from.getColumn();
		int toColumn = to.getColumn();
		int fromRow = from.getRow();
		int toRow = to.getRow();
		
		// Ensure Coordinate to is valid
		if (toColumn < 1 || toColumn > 8 || toRow < 1 || toRow > 8)
		{
			return false;
		}
		
		// Not allowed to stand still on a move
		if (fromColumn == toColumn && fromRow == toRow)
		{
			return false;
		}

		// Check if trying to move to space already occupied by piece of same color
		ChessPiece piece = (ChessPiece) b.getPieceAt(from);
		if (b.isSpaceOccupied(to) && ((ChessPiece) b.getPieceAt(to)).getColor() == piece.getColor()) 
		{
			return false;
		}
		
		switch (piece.getName()) {
			case BISHOP:
				return checkValidMoveBishop(from, to, b);
			case KING:
				return checkValidMoveKing(from, to);
			case KNIGHT:
				return checkValidMoveKnight(from, to, b);
			case PAWN:
				return checkValidMovePawn(piece, from, to, b);
			case QUEEN:
				return checkValidMoveQueen(from, to, b);
			case ROOK:
				return checkValidMoveRook(from, to, b);
			default:
				break;
			
		}
		
		return false;
	}

	/**
	 * Determine if a rook's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @param b - the board
	 * @return true if the move can be made
	 */
	private boolean checkValidMoveRook(Coordinate from, Coordinate to, Board b)
	{
		int fromColumn = from.getColumn();
		int fromRow = from.getRow();
		Coordinate tempCoord;

		// Attempting to move vertically
		if (fromColumn == to.getColumn()) 
		{	
			// Moving up
			for (int i = fromRow; i < to.getRow(); i++) 
			{
				tempCoord = makeCoordinate(i, fromColumn);

				// Checks if any of the spaces are occupied
				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from)) 
				{
					return false;
				}
			}

			// Moving down
			for (int i = fromRow; i > to.getRow(); i--) 
			{
				tempCoord = makeCoordinate(i, fromColumn);

				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from)) 
				{
					return false;
				}
			}
			return true;
		}

		// Attempting to move horizontally
		if (fromRow == to.getRow()) 
		{	
			// Moving right
			for (int i = fromColumn; i < to.getColumn(); i++) 
			{
				tempCoord = makeCoordinate(fromRow, i);

				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from)) 
				{
					return false;
				}
			}

			// Moving left
			for (int i = fromColumn; i > to.getColumn(); i--) 
			{
				tempCoord = makeCoordinate(fromRow, i);

				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from)) 
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Determine if a queen's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @param b - the board
	 * @return true if the move can be made
	 */
	private boolean checkValidMoveQueen(Coordinate from, Coordinate to, Board b)
	{
		return checkValidMoveBishop(from, to, b) || checkValidMoveRook(from, to, b);
	}

	/**
	 * Determine if a pawn's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @param b - the board
	 * @return true if the move can be made
	 */
	private boolean checkValidMovePawn(ChessPiece piece, Coordinate from, Coordinate to, Board b)
	{
		int fromColumn = from.getColumn();
		int fromRow = from.getRow();
		int toColumn = to.getColumn();
		int toRow = to.getRow();
		
		int columnDiff = Math.abs(fromColumn - toColumn);
		int rowDiff = Math.abs(fromRow - toRow);
		double hypotenuse = getHypotenuse(columnDiff, rowDiff);
		Coordinate nextSpace;
		Coordinate twoSpacesAway;
		
		// Pawn can't move if it reaches the end of the board
		if (fromRow == 1 || fromRow == 8) 
		{
			return false;
		}

		// Pawn can't move more than 2 spaces forward or 1 space diagonally
		if (rowDiff > 2 || (hypotenuse > Math.sqrt(2) && fromColumn != toColumn)) 
		{
			return false;
		}

		// Row numbers are different based on pawn color
		switch (piece.getColor()) 
		{
			case WHITE:
				// Cannot move backwards or sideways
				if ((fromRow > toRow || toColumn != fromColumn) && fromRow >= toRow) 
				{
					return false;
				}

				// Get adjacent spaces on the board
				nextSpace = makeCoordinate(fromColumn, fromRow + 1);
				twoSpacesAway = makeCoordinate(fromColumn, fromRow + 2);

				// Pawns are able to move forward two spaces if they are on their starting position
				if (!piece.hasMoved && rowDiff == 2 && !b.isSpaceOccupied(nextSpace)
					&& !b.isSpaceOccupied(twoSpacesAway) && fromColumn == toColumn) 
				{
					return true;
				}
				
				// Otherwise, they move only one space
				if (fromRow >= 2 && rowDiff == 1 && !b.isSpaceOccupied(nextSpace)	&& fromColumn == toColumn) 
				{
					return true;
				}
				
				// Able to capture an opponent piece that is diagonally left or right forward one space
				if (b.isSpaceOccupied(to) && toRow == fromRow + 1 && (toColumn == fromColumn + 1 || toColumn == fromColumn - 1)) 
				{
					return true;
				}
				break;
			case BLACK:
				// Cannot move backwards or sideways
				if ((fromRow < toRow || toColumn != fromColumn) && fromRow <= toRow) 
				{
					return false;
				}

				// Get adjacent spaces on the board
				nextSpace = makeCoordinate(fromColumn, fromRow - 1);
				twoSpacesAway = makeCoordinate(fromColumn, fromRow - 2);

				// Pawns are able to move forward two spaces if they are on their starting position
				if (!piece.hasMoved && rowDiff == 2 && !b.isSpaceOccupied(nextSpace)
					&& !b.isSpaceOccupied(twoSpacesAway) && fromColumn == toColumn) 
				{
					return true;
				}
				
				// Otherwise, they move only one space
				if (fromRow >= 2 && rowDiff == 1 && !b.isSpaceOccupied(nextSpace)	&& fromColumn == toColumn) 
				{
					return true;
				}
				
				// Able to capture an opponent piece that is diagonally left or right forward one space
				if (b.isSpaceOccupied(to) && toRow == fromRow - 1 && (toColumn == fromColumn + 1 || toColumn == fromColumn - 1)) 
				{
					return true;
				}
				break;

		}
		return false;
	}

	/**
	 * Determine if a knight's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @param b - the board
	 * @return true if the move can be made
	 */
	private boolean checkValidMoveKnight(Coordinate from, Coordinate to, Board b)
	{
		int fromColumn = from.getColumn();
		int fromRow = from.getRow();
		int toColumn = to.getColumn();
		int toRow = to.getRow();
		
		int columnDiff = Math.abs(fromColumn - toColumn);
		int rowDiff = Math.abs(fromRow - toRow);
		
		// Check if the proposed movement is equivalent to a horse movement, aka, an L-shape
		if (getHypotenuse(columnDiff, rowDiff) == Math.sqrt(2 * 2 + 1))
		{
			return true;
		}
		return false;
	}

	/**
	 * Get the hypotenuse of a movement triangle using the Pythagorean Theorem
	 * @param columnDiff - the difference between the columns of the two points on the board
	 * @param rowDiff - the difference between the rows of the two points on the board
	 * @return the hypotenuse of a movement triangle
	 */
	private double getHypotenuse(int columnDiff, int rowDiff)
	{
		double cSquared = (columnDiff * columnDiff) + (rowDiff * rowDiff);
		return Math.sqrt(cSquared);
	}

	/**
	 * Determine if a bishop's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @param b - the board
	 * @return true if the move can be made
	 */
	private boolean checkValidMoveBishop(Coordinate from, Coordinate to, Board b)
	{
		int fromColumn = from.getColumn();
		int fromRow = from.getRow();
		int toColumn = to.getColumn();
		int toRow = to.getRow();
		int currentRow = fromRow;
		int columnDiff = Math.abs(fromColumn - toColumn);
		int rowDiff = Math.abs(fromRow - toRow);
		
		// If attempting to move diagonally
		if (fromColumn != toColumn && fromRow != toRow && columnDiff == rowDiff)
		{
			// check both diagonal paths for anything blocking the way
			
			// Diagonal to the right
			for (int i = fromColumn; i < to.getColumn(); i++)
			{
				Coordinate tempCoord = makeCoordinate(currentRow, i);
				
				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from))
				{
					return false;
				}
				
				if (toRow > fromRow)
				{
					currentRow++;
				}
				
				if (toRow < fromRow)
				{
					currentRow--;
				}
				
			}
			
			// Diagonally to the left
			for (int i = fromColumn; i > to.getColumn(); i--)
			{
				Coordinate tempCoord = makeCoordinate(currentRow, i);
				
				if (b.isSpaceOccupied(tempCoord) && !tempCoord.equals(from))
				{
					return false;
				}
				
				if (toRow > fromRow)
				{
					currentRow++;
				}
				
				if (toRow < fromRow)
				{
					currentRow--;
				}
			}
			// Able to return true if made through both for loops, otherwise the move wasn't possible
			return true;
		}
		
		return false;
	}

	/**
	 * Determine if a king's move is valid
	 * @param from - the starting coordinate
	 * @param to - the ending coordinate
	 * @return true if the move can be made
	 */
	private boolean checkValidMoveKing(Coordinate from, Coordinate to)
	{
		int fromColumn = from.getColumn();
		int fromRow = from.getRow();
		int toColumn = to.getColumn();
		int toRow = to.getRow();
		
		int columnDiff = Math.abs(fromColumn - toColumn);
		int rowDiff = Math.abs(fromRow - toRow);
		
		if (columnDiff == 1 || rowDiff == 1)
		{
			return true;
		}
		return false;
	}

	/**
	 * @return the hasMoved
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Once it moves, you can't change it.
	 * @param hasMoved the hasMoved to set
	 */
	public void setHasMoved()
	{
		hasMoved = true;
	}
}
