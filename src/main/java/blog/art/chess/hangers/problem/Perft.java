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

package blog.art.chess.hangers.problem;

import blog.art.chess.hangers.move.Move;
import blog.art.chess.hangers.position.Position;
import java.util.ArrayList;
import java.util.List;

public class Perft {

  private final Position position;
  private final int nPlies;

  public Perft(Position position, int nPlies) {
    this.position = position;
    this.nPlies = nPlies;
  }

  public void solve() {
    long begin = System.currentTimeMillis();
    List<Move> pseudoLegalMoves = new ArrayList<>();
    if (Move.isPositionLegal(position, pseudoLegalMoves)) {
      long nNodes = count(position, nPlies, pseudoLegalMoves, true);
      long end = System.currentTimeMillis();
      System.out.printf("Nodes searched: %d%n", nNodes);
      System.out.printf("info time %d%n", end - begin);
    } else {
      System.out.println("info string Illegal position");
    }
  }

  private static long count(Position position, int nPlies, List<Move> pseudoLegalMoves,
      boolean verbose) {
    if (nPlies == 0) {
      return 1;
    }
    long nNodes = 0;
    for (Move move : pseudoLegalMoves) {
      List<Move> pseudoLegalMovesNext = new ArrayList<>();
      Position positionNext = move.make(position, pseudoLegalMovesNext);
      if (positionNext != null) {
        long nChildNodes = count(positionNext, nPlies - 1, pseudoLegalMovesNext, false);
        nNodes += nChildNodes;
        if (verbose) {
          System.out.printf("%s: %d%n", move.getUciCode(), nChildNodes);
        }
      }
    }
    return nNodes;
  }
}
