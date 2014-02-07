(ns ttt-clojure.gamestate-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.gamestate :refer :all]))

(describe "first-move?"
  (let [gamestate {:movelist [] :turn "x" :board ["-" "-" "-" "-" "-" "-" "-" "-" "-"]}]
    (it "should return true if the game is brand new || no moves have been taken"
      (should= true (first-move? gamestate)))))

