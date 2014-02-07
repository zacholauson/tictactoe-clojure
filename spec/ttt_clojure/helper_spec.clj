(ns ttt-clojure.helper-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.helper :refer [winning-lines
                                        possible-moves
                                        win?
                                        tied?
                                        game-over?]]))

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
