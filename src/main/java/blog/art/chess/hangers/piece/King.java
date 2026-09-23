/*
 * MIT License
 *
 * Copyright (c) 2026 Ivan Denkovski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package blog.art.chess.hangers.piece;

import blog.art.chess.hangers.move.Castling;
import blog.art.chess.hangers.move.Move;
import java.util.List;
import java.util.Set;

public class King extends Leaper {

  private final boolean black;

  public King(boolean black) {
    this.black = black;
  }

  @Override
  protected boolean isBlack() {
    return black;
  }

  @Override
  protected boolean generateMoves(List<Piece> board, int origin, Set<Integer> castlingOrigins,
      Integer enPassantTarget, List<Move> moves) {
    if (!super.generateMoves(board, origin, castlingOrigins, enPassantTarget, moves)) {
      return false;
    }
    if (castlingOrigins.contains(origin)) {
      int[] castlingDirections = {-8, 8};
      for (int direction : castlingDirections) {
        int target2 = origin + direction;
        if (board.get(target2) == null) {
          int target = target2 + direction;
          if (board.get(target) == null) {
            if (direction > 0) {
              int origin2 = target + direction;
              if (castlingOrigins.contains(origin2)) {
                if (moves != null) {
                  moves.add(new Castling(origin, target, origin2, target2));
                }
              }
            } else {
              int stop = target + direction;
              if (board.get(stop) == null) {
                int origin2 = stop + direction;
                if (castlingOrigins.contains(origin2)) {
                  if (moves != null) {
                    moves.add(new Castling(origin, target, origin2, target2));
                  }
                }
              }
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  protected int[] getDirections() {
    return new int[]{-9, -8, -7, -1, 1, 7, 8, 9};
  }

  @Override
  protected int getMaxOffset() {
    return 1;
  }

  @Override
  public String getUciCode() {
    return "k";
  }
}
