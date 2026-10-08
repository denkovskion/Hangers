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

package blog.art.chess.hangers;

import blog.art.chess.hangers.game.Position;
import blog.art.chess.hangers.parser.Parser;
import blog.art.chess.hangers.problem.MateSearch;
import blog.art.chess.hangers.problem.Perft;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    try {
      Position position = new Parser().parseFen(
          "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      for (String line; (line = reader.readLine()) != null; ) {
        try {
          Scanner scanner = new Scanner(line);
          if (scanner.hasNext()) {
            String command = scanner.next("uci|isready|position|go|quit");
            if (command.equals("uci")) {
              scanner.skip("\\s*$");
              System.out.printf("id name %s %s%n", getName(), getVersion());
              System.out.printf("id author %s%n", getAuthor());
              System.out.println("uciok");
            } else if (command.equals("isready")) {
              scanner.skip("\\s*$");
              System.out.println("readyok");
            } else if (command.equals("position")) {
              scanner.next("fen");
              scanner.skip("\\s*");
              String parameter = scanner.nextLine();
              Position newPosition = new Parser().parseFen(parameter);
              if (newPosition != null) {
                position = newPosition;
              }
            } else if (command.equals("go")) {
              String subcommand = scanner.next("perft|mate");
              if (subcommand.equals("perft")) {
                String parameter = scanner.next("0|[1-9]\\d*");
                int nPlies = Integer.parseInt(parameter);
                scanner.skip("\\s*$");
                new Perft(position, nPlies).solve();
              } else if (subcommand.equals("mate")) {
                String parameter = scanner.next("[1-9]\\d*");
                int nMoves = Integer.parseInt(parameter);
                scanner.skip("\\s*$");
                new MateSearch(position, nMoves).solve();
              }
            } else if (command.equals("quit")) {
              scanner.skip("\\s*$");
              System.exit(0);
            }
          }
        } catch (NoSuchElementException e) {
          System.out.println("info string Ignored line");
        }
      }
    } catch (IOException e) {
      System.exit(1);
    }
  }

  private static String getName() {
    return "Hangers";
  }

  private static String getVersion() {
    Package pkg = Main.class.getPackage();
    if (pkg != null) {
      String version = pkg.getImplementationVersion();
      if (version != null) {
        return version;
      }
      return "(development)";
    }
    return "(unknown)";
  }

  private static String getAuthor() {
    return "Ivan Denkovski";
  }
}
