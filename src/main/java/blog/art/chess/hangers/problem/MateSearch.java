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

import blog.art.chess.hangers.game.Position;
import blog.art.chess.hangers.move.Move;
import blog.art.chess.hangers.move.NullMove;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MateSearch {

  private final Position position;
  private final int nMoves;

  public MateSearch(Position position, int nMoves) {
    this.position = position;
    this.nMoves = nMoves;
  }

  public void solve() {
    long begin = System.currentTimeMillis();
    List<Move> pseudoLegalMoves = new ArrayList<>();
    if (Move.isPositionLegal(position, pseudoLegalMoves)) {
      List<Variation> variations = new ArrayList<>();
      for (Move move : pseudoLegalMoves) {
        List<Move> pseudoLegalMovesMin = new ArrayList<>();
        Position positionMin = move.make(position, pseudoLegalMovesMin);
        if (positionMin != null) {
          Variation variationMin = searchMin(positionMin, nMoves, pseudoLegalMovesMin);
          int distance = variationMin.getValue() > 0 ? nMoves - variationMin.getValue() + 1
              : Integer.MAX_VALUE;
          List<Move> moves = new ArrayList<>(variationMin.getMoves());
          moves.add(0, move);
          variations.add(new Variation(distance, moves));
          if (distance <= nMoves) {
            System.out.printf("info string %s: mate in %d%n", move.getUciCode(), distance);
          } else {
            System.out.printf("info string %s: no mate in %d%n", move.getUciCode(), nMoves);
          }
        }
      }
      long end = System.currentTimeMillis();
      if (!variations.isEmpty()) {
        variations.sort(Comparator.comparingInt(Variation::getValue));
        Variation principalVariation = variations.get(0);
        if (principalVariation.getValue() <= nMoves) {
          List<String> tokens = new ArrayList<>();
          for (Move move : principalVariation.getMoves()) {
            tokens.add(move.getUciCode());
          }
          System.out.printf("info time %d score mate %d pv %s%n", end - begin,
              principalVariation.getValue(), String.join(" ", tokens));
        } else {
          System.out.printf("info time %d%n", end - begin);
        }
        System.out.printf("bestmove %s%n", principalVariation.getMoves().get(0).getUciCode());
      } else {
        System.out.printf("info time %d%n", end - begin);
        System.out.printf("bestmove %s%n", new NullMove().getUciCode());
      }
    } else {
      System.out.println("info string Illegal position");
    }
  }

  private static Variation searchMax(Position positionMax, int nMoves,
      List<Move> pseudoLegalMovesMax) {
    int valueMax = -1;
    List<Move> movesMax = new ArrayList<>();
    for (Move moveMax : pseudoLegalMovesMax) {
      List<Move> pseudoLegalMovesMin = new ArrayList<>();
      Position positionMin = moveMax.make(positionMax, pseudoLegalMovesMin);
      if (positionMin != null) {
        Variation variationMin = searchMin(positionMin, nMoves, pseudoLegalMovesMin);
        if (variationMin.getValue() > valueMax) {
          valueMax = variationMin.getValue();
          movesMax = new ArrayList<>(variationMin.getMoves());
          movesMax.add(0, moveMax);
          if (valueMax == nMoves) {
            break;
          }
        }
      }
    }
    return new Variation(valueMax, movesMax);
  }

  private static Variation searchMin(Position positionMin, int nMoves,
      List<Move> pseudoLegalMovesMin) {
    int valueMin = 0;
    List<Move> movesMin = new ArrayList<>();
    if (nMoves == 1) {
      for (Move moveMin : pseudoLegalMovesMin) {
        if (moveMin.make(positionMin, null) != null) {
          valueMin = -1;
          break;
        }
      }
    } else {
      for (Move moveMin : pseudoLegalMovesMin) {
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        Position positionMax = moveMin.make(positionMin, pseudoLegalMovesMax);
        if (positionMax != null) {
          Variation variationMax = searchMax(positionMax, nMoves - 1, pseudoLegalMovesMax);
          if (valueMin == 0 || variationMax.getValue() < valueMin) {
            valueMin = variationMax.getValue();
            movesMin = new ArrayList<>(variationMax.getMoves());
            movesMin.add(0, moveMin);
            if (valueMin == -1) {
              break;
            }
          }
        }
      }
    }
    if (valueMin == 0) {
      valueMin = new NullMove().make(positionMin, null) != null ? -1 : nMoves;
    }
    return new Variation(valueMin, movesMin);
  }
}
