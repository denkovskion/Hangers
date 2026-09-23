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

package blog.art.chess.hangers.move;

import blog.art.chess.hangers.piece.Piece;
import blog.art.chess.hangers.position.Position;
import java.util.List;
import java.util.Set;

public class Castling extends Move {

  private final int origin;
  private final int target;
  private final int origin2;
  private final int target2;

  public Castling(int origin, int target, int origin2, int target2) {
    this.origin = origin;
    this.target = target;
    this.origin2 = origin2;
    this.target2 = target2;
  }

  @Override
  protected boolean preMake(Position position) {
    return new NullMove().make(position, null) != null
        && new QuietMove(origin, target2).make(position, null) != null;
  }

  @Override
  protected void updateBoard(List<Piece> board) {
    board.set(target, board.set(origin, null));
    board.set(target2, board.set(origin2, null));
  }

  @Override
  protected void updateCastlingOrigins(Set<Integer> castlingOrigins) {
    castlingOrigins.remove(origin);
    castlingOrigins.remove(origin2);
  }

  @Override
  protected Integer getEnPassantTarget() {
    return null;
  }

  @Override
  public String getUciCode() {
    return Piece.toUciCode(origin) + Piece.toUciCode(target);
  }
}
