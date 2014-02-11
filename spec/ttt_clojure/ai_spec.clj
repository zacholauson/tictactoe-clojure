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
                                                    (win? newer-gamestate :o) false
                                                    (win? newer-gamestate :x) true
                                                    :else (playout-every-game newer-gamestate))))
                                         (possible-moves new-gamestate))))))

(describe "leaf-score"
  (it "should return the leaf score for the given gamestate"
    (should=  1 (leaf-score {:movelist [] :board [:x :x :x :o :- :- :o :- :-]}))
    (should= -1 (leaf-score {:movelist [] :board [:o :o :o :x :- :- :x :- :-]}))
    (should=  0 (leaf-score {:movelist [] :board [:x :o :o :o :x :x :x :x :o]}))))

(describe "minimax"
  (it "should return the minimax score for the given position"
    (should= 0 (find-move {:movelist []        :board [:- :- :- :- :- :- :- :- :-]})))
  (it "should return take the win if its available"
    (should= 0 (find-move {:movelist [1 2 3 4] :board [:- :x :x :o :o :x :- :- :o]})))
  (it "should return 0 if thats best move"
    (should= 3 (find-move {:movelist [0 1]     :board [:x :o :- :- :- :- :- :- :-]})))
  (it "should block if a position is available that could allow o to win"
    (should= 1 (find-move {:movelist [0 2 3]   :board [:o :- :o :x :- :- :x :- :-]})))
  (it "when playing out every possible game they should all return true for win or tie"
    (should= true (every? true? (remove nil? (flatten (playout-every-game {:movelist [] :board [:- :- :- :- :- :- :- :- :-]})))))))
