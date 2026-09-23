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

import blog.art.chess.hangers.move.Capture;
import blog.art.chess.hangers.move.DoubleStep;
import blog.art.chess.hangers.move.EnPassant;
import blog.art.chess.hangers.move.Move;
import blog.art.chess.hangers.move.Promotion;
import blog.art.chess.hangers.move.PromotionCapture;
import blog.art.chess.hangers.move.QuietMove;
import java.util.List;
import java.util.Set;

public class Pawn extends Piece {

  private final boolean black;

  public Pawn(boolean black) {
    this.black = black;
  }

  @Override
  protected boolean isBlack() {
    return black;
  }

  @Override
  protected boolean generateMoves(List<Piece> board, int origin, Set<Integer> castlingOrigins,
      Integer enPassantTarget, List<Move> moves) {
    int[] captureDirections = black ? new int[]{-9, 7} : new int[]{-7, 9};
    int maxOffset = 1;
    for (int direction : captureDirections) {
      int target = origin + direction;
      if (target >= 0 && target < 64 && Math.abs(target / 8 - origin / 8) <= maxOffset
          && Math.abs(target % 8 - origin % 8) <= maxOffset) {
        Piece other = board.get(target);
        if (other != null) {
          if (other.isBlack() != black) {
            if (other instanceof King) {
              return false;
            }
            if (origin % 8 == (black ? 1 : 6)) {
              Piece[] box = new Piece[]{new Queen(black), new Rook(black), new Bishop(black),
                  new Knight(black)};
              for (Piece promoted : box) {
                if (moves != null) {
                  moves.add(new PromotionCapture(origin, target, promoted));
                }
              }
            } else {
              if (moves != null) {
                moves.add(new Capture(origin, target));
              }
            }
          }
        } else {
          if (enPassantTarget != null) {
            if (target == enPassantTarget) {
              int stop = (target / 8) * 8 + origin % 8;
              if (moves != null) {
                moves.add(new EnPassant(origin, target, stop));
              }
            }
          }
        }
      }
    }
    int direction = black ? -1 : 1;
    int target = origin + direction;
    if (target >= 0 && target < 64 && Math.abs(target / 8 - origin / 8) <= 1
        && Math.abs(target % 8 - origin % 8) <= maxOffset) {
      if (board.get(target) == null) {
        if (origin % 8 == (black ? 1 : 6)) {
          Piece[] box = new Piece[]{new Queen(black), new Rook(black), new Bishop(black),
              new Knight(black)};
          for (Piece promoted : box) {
            if (moves != null) {
              moves.add(new Promotion(origin, target, promoted));
            }
          }
        } else {
          if (moves != null) {
            moves.add(new QuietMove(origin, target));
          }
          if (origin % 8 == (black ? 6 : 1)) {
            int target2 = target + direction;
            if (board.get(target2) == null) {
              if (moves != null) {
                moves.add(new DoubleStep(origin, target2, target));
              }
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public String getUciCode() {
    return "p";
  }
}
