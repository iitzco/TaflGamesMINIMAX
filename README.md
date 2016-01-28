# Minimax implementation with alpha-beta pruning of the [Tafl Game](https://en.wikipedia.org/wiki/Tafl_games)

#### Desktop game using Java and Swing for GUI. 
#### AI implemented with [Minimax algorithm](https://en.wikipedia.org/wiki/Minimax) optimized with alpha-beta pruning

To play, download or clone the repository. Then, change directory and execute

```bash
ant
```

The file *game.jar* must have been generated.

Now, execute

```bash
java -jar game.jar -file file (-maxtime n | -depth n) -visual [-prune]
```

Where:
* *file* is the path to the initial board. See the *boards* folder for examples. (Note: the first line of the file contains 1 or 2 depending on the starting turn, '0' is an empty cell, 'N' is an enemy, 'G' is one king's guard, 'R' is the King.). **Feel free to create your own starting board!**
* *n* can be related to -maxtime or to -depth. Maxtime stops the Minimax processing once it reaches *n* seconds. Depth means that the Minimax algorithm will stop once it reaches *n* levels in the gaming tree.
* *-prune* is optional. It activates alpha-beta pruning.

For example,
```bash
java -jar game.jar –file boards/15x15.txt -maxtime 4 -visual -prune
```

##### This was the final project of Data Structure and Algorithms course, at [ITBA](http://itba.edu.ar/).

###### Made By:
* [Ivan Itzcovich](https://github.com/iitzco)
* [Kevin Kraus](https://github.com/kevinkraus92)
* Lucas Carmona




