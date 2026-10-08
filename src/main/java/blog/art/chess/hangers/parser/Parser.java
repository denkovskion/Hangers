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

package blog.art.chess.hangers.parser;

import blog.art.chess.hangers.game.Position;
import blog.art.chess.hangers.piece.Bishop;
import blog.art.chess.hangers.piece.King;
import blog.art.chess.hangers.piece.Knight;
import blog.art.chess.hangers.piece.Pawn;
import blog.art.chess.hangers.piece.Piece;
import blog.art.chess.hangers.piece.Queen;
import blog.art.chess.hangers.piece.Rook;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;

public class Parser {

  public Position parseFen(String string) {
    try {
      Scanner fields = new Scanner(string);
      List<Piece> board = new ArrayList<>(Collections.nCopies(64, null));
      Scanner characters = new Scanner(fields.next()).useDelimiter("");
      for (int rank = 8; rank >= 1; rank--) {
        for (int file = 1; file <= 8; file++) {
          if (characters.hasNext("[" + "12345678".substring(0, 8 - (file - 1)) + "]")) {
            file += characters.nextInt();
            if (file > 8) {
              break;
            }
          }
          String letter = characters.next("[KQRBNPkqrbnp]");
          int square = (file - 1) * 8 + rank - 1;
          switch (letter) {
            case "K":
              board.set(square, new King(false));
              break;
            case "Q":
              board.set(square, new Queen(false));
              break;
            case "R":
              board.set(square, new Rook(false));
              break;
            case "B":
              board.set(square, new Bishop(false));
              break;
            case "N":
              board.set(square, new Knight(false));
              break;
            case "P":
              board.set(square, new Pawn(false));
              break;
            case "k":
              board.set(square, new King(true));
              break;
            case "q":
              board.set(square, new Queen(true));
              break;
            case "r":
              board.set(square, new Rook(true));
              break;
            case "b":
              board.set(square, new Bishop(true));
              break;
            case "n":
              board.set(square, new Knight(true));
              break;
            case "p":
              board.set(square, new Pawn(true));
              break;
          }
        }
        characters.skip(rank > 1 ? "/" : "$");
      }
      boolean blackToMove = false;
      if (fields.hasNext("w")) {
        fields.next();
      } else {
        fields.next("b");
        blackToMove = true;
      }
      Set<Integer> castlingOrigins = new HashSet<>();
      if (fields.hasNext("-")) {
        fields.next();
      } else {
        String[] letters = fields.next("\\bK?Q?k?q?").split("");
        for (String letter : letters) {
          switch (letter) {
            case "K":
            case "Q":
              castlingOrigins.add(32);
              break;
            case "k":
            case "q":
              castlingOrigins.add(39);
              break;
          }
          switch (letter) {
            case "K":
              castlingOrigins.add(56);
              break;
            case "Q":
              castlingOrigins.add(0);
              break;
            case "k":
              castlingOrigins.add(63);
              break;
            case "q":
              castlingOrigins.add(7);
              break;
          }
        }
      }
      Integer enPassantTarget = null;
      if (fields.hasNext("-")) {
        fields.next();
      } else {
        fields.next("([a-h])([36])");
        MatchResult result = fields.match();
        int file = 1 + result.group(1).charAt(0) - 'a';
        int rank = 1 + result.group(2).charAt(0) - '1';
        enPassantTarget = (file - 1) * 8 + rank - 1;
      }
      fields.next("0|[1-9]\\d*");
      fields.next("[1-9]\\d*");
      fields.skip("\\s*$");
      return Position.newInstance(board, blackToMove, castlingOrigins, enPassantTarget);
    } catch (IllegalArgumentException ex) {
      System.out.printf("info string %s%n", ex.getMessage());
    } catch (NoSuchElementException e) {
      System.out.println("info string Invalid FEN");
    }
    return null;
  }
}
