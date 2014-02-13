(ns ttt-clojure.ai-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.gamestate :refer :all]))

(defn playout-every-game [gamestate]
  (if (xs-turn? gamestate) (let [new-gamestate (move gamestate (find-move gamestate))]
                                (if (game-over? new-gamestate) true
                                    (map (fn [possible-move]
                                             (let [newer-gamestate (move new-gamestate possible-move)]
                                                  (cond
                                                    (win? newer-gamestate :o) [newer-gamestate false]
                                                    (win? newer-gamestate :x) true
                                                    :else (playout-every-game newer-gamestate))))
                                         (possible-moves new-gamestate))))))

(describe "leaf-score"
  (it "should return the leaf score for the given gamestate"
    (should=  10 (leaf-score {:movelist [] :board [:x :x :x :o :- :- :o :- :-]} 0))
    (should= -10 (leaf-score {:movelist [] :board [:o :o :o :x :- :- :x :- :-]} 0))
    (should=   0 (leaf-score {:movelist [] :board [:x :o :o :o :x :x :x :x :o]} 0))))

(describe "minimax"
  (it "should return 0 if its the first move"
    (should= 0 (find-move {:movelist []        :board [:- :- :- :- :- :- :- :- :-]})))
  (it "should return take the win if its available"
    (should= 0 (find-move {:movelist [1 2 3 4] :board [:- :x :x :o :o :x :- :- :o]})))
  (it "should block :o from winning if theres a chance"
    (should= 1 (find-move {:movelist [0 2 3]   :board [:o :- :o :x :- :- :x :- :-]})))
  (it "should block if a position is available that could allow o to win"
    (should= 6 (find-move {:movelist [0 4 1 2] :board [:x :x :o :- :o :- :- :- :-]})))
  (it "when playing out every possible game they should all return true for win or tie"
    (should= true (every? true? (distinct (flatten (playout-every-game {:movelist [] :board [:- :- :- :- :- :- :- :- :-]})))))))
