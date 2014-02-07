(ns ttt-clojure.helper-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.helper :refer :all] ))

(describe "winning-lines"
  (it "should return a vector of all winning lines with whatever piece is in each position"
    (should= [["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"] ["-" "-" "-"]]
             (winning-lines {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}))
    (should= [["x" "x" "o"] ["o" "o" "-"] ["-" "-" "-"] ["x" "o" "-"] ["x" "o" "-"] ["o" "-" "-"] ["x" "o" "-"] ["o" "o" "-"]]
             (winning-lines {:movelist [] :turn "x" :board ["x" "x" "o" "o" "o" "-" "-" "-" "-"]}))))

(describe "win?"
  (context "x won"
    (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "x" "x" "x"]}]
      (it "should return true if 'x' has won"
        (should= true (win? gamestate "x"))))))
  (context "o won"
    (let [gamestate {:movelist [] :turn "x" :board ["-" "o" "-" "-" "o" "-" "x" "o" "x"]}]
      (it "should return true if 'o' has won"
        (should= true (win? gamestate "o")))))

(describe "tied?"
  (let [gamestate {:movelist [] :turn "x" :board ["x" "o" "o" "o" "x" "x" "x" "x" "o"]}]
    (it "should return true when no one has won and no more spaces are available"
        (should= true (tied? gamestate)))))

(describe "game-over?"
  (context "x won"
    (let [gamestate {:movelist [] :turn "x" :board ["x" "x" "x" "o" "-" "-" "o" "-" "-"]}]
      (it "should return true when x has won"
        (should= true (game-over? gamestate)))))
  (context "o won"
    (let [gamestate {:movelist [] :turn "x" :board ["o" "o" "o" "x" "-" "-" "x" "-" "-"]}]
      (it "should return true when o has won"
        (should= true (game-over? gamestate)))))
  (context "tied game"
    (let [gamestate {:movelist [] :turn "x" :board ["x" "o" "o" "o" "x" "x" "x" "x" "o"]}]
      (it "should return true when the game has tied"
        (should= true (game-over? gamestate)))))
  (context "game not over"
    (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
      (it "should return false when the game is not over"
        (should= false (game-over? gamestate))))))

(describe "possible-moves"
  (it "should return a list of indexes for the possible moves"
    (should= [0 1 2 3 4 5 6 7 8] (possible-moves {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}))
    (should= [4 5 7 8] (possible-moves {:movelist [] :turn "x" :board ["o" "o" "o" "x" "-" "-" "x" "-" "-"]}))))

(describe "other-turn"
  (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return 'o' when its 'x's turn"
      (should= "o" (other-turn gamestate))))
  (let [gamestate {:movelist [] :turn "o" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return 'x' when its 'o's turn"
      (should= "x" (other-turn gamestate)))))

(describe "add-play-to-movelist"
  (it "should return an updated movelist with the current players last move"
    (should= [0] (add-play-to-movelist {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]} 0))))

(describe "add-play-to-board"
  (it "should return an updated board with the current players turn on the board"
    (should= ["x" "-" "-" "-" "-" "-" "-" "-" "-"] (add-play-to-board {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]} 0))))

(describe "make-next-move"
  (let [gamestate {:movelist [] :turn "o" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should take the next move index and put the current players marker on the board and swap to the other players turn"
      (should= {:movelist [0] :turn "x" :board ["o" "-" "-" "-" "-" "-" "-" "-" "-"]} (make-next-move gamestate 0)))))
