(ns ttt-clojure.gamestate-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.gamestate :refer :all]))

(describe "first-move?"
  (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return true if the game is brand new || no moves have been taken"
      (should= true (first-move? gamestate)))))

(describe "other-turn"
  (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return 'o' when its 'x's turn"
      (should= "o" (other-turn gamestate))))
  (let [gamestate {:movelist [] :turn "o" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return 'x' when its 'o's turn"
      (should= "x" (other-turn gamestate)))))

(describe "make-next-move"
  (let [gamestate {:movelist [] :turn "o" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should take the next move index and put the current players marker on the board and swap to the other players turn"
      (should= {:movelist [0] :turn "x" :board ["o" "-" "-" "-" "-" "-" "-" "-" "-"]} (make-next-move gamestate 0)))))
