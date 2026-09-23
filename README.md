# Hangers

Hangers is a mate search chess program.

## Usage

Java 8 or later is required.

```
java -jar Hangers.jar
```

Hangers uses the [Universal Chess Interface](https://chessprogramming.org/UCI) protocol with a
minimal subset of commands: `uci`, `isready`, `position fen <fenstring>`, `go mate <x>`,
`go perft <x>`, `quit`.

## Example

> Sam Loyd, The Sunny South 1885

### Input

```
position fen 5Q2/5B1k/6r1/6p1/6N1/6K1/8/8 w - - 0 1
go mate 2
```

### Output

```
info score mate 2 pv f8a8 g6f6 a8g8
bestmove f8a8
```

## Author

Ivan Denkovski is the author of Hangers.
