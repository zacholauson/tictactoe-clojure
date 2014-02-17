(ns ttt-clojure.ai-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.gamestate :refer :all]))

(defn computer-win-or-tie [gamestate]
  (or (win? gamestate (:computer gamestate)) (tied? gamestate)))

(defn playout-every-game [gamestate]
  (if (computers-turn? gamestate)
    (let [new-gamestate (move gamestate (find-move gamestate))
          computer-won-or-tied (computer-win-or-tie new-gamestate)]
      (if computer-won-or-tied
        true
        (map (fn [possible-move]
               (let [newer-gamestate (move new-gamestate possible-move)]
                 (cond
                   (win? newer-gamestate (:computer newer-gamestate)) true
                   (win? newer-gamestate (human-mark newer-gamestate)) false
                   :else (playout-every-game newer-gamestate))))
             (possible-moves new-gamestate))))))

(describe "leaf-score"
  (it "should return the leaf score for the given gamestate"
    (should=  10 (leaf-score {:board [:x :x :x :o :- :- :o :- :-] :computer :x} 0))
    (should= -10 (leaf-score {:board [:o :o :o :x :- :- :x :- :-] :computer :x} 0))
    (should=   0 (leaf-score {:board [:x :o :o :o :x :x :x :x :o] :computer :x} 0))
    (should=  10 (leaf-score {:board [:o :o :o :x :- :- :x :- :x] :computer :o} 0))
    (should= -10 (leaf-score {:board [:x :x :x :o :- :- :o :- :-] :computer :o} 0))
    (should=   0 (leaf-score {:board [:x :o :o :o :x :x :x :x :o] :computer :o} 0))))

(describe "minimax"
  (it "should return 0 if its the first move"
    (should= 0 (find-move {:board [:- :- :- :- :- :- :- :- :-]
                           :computer :x
                           :options {:difficulty :unbeatable}})))
  (it "should return take the win if its available"
    (should= 0 (find-move {:board [:- :x :x :o :o :x :- :- :o]
                           :computer :x
                           :options {:difficulty :unbeatable}})))
  (it "should block :o from winning if theres a chance"
    (should= 1 (find-move {:board [:o :- :o :x :- :- :x :- :-]
                           :computer :x
                           :options {:difficulty :unbeatable}})))
  (it "when playing out every possible game they should all return true for win or tie when computer goes first"
    (should= true (every? true? (distinct (flatten (playout-every-game {:board [:- :- :- :- :- :- :- :- :-]
                                                                        :computer :x
                                                                        :options {:difficulty :unbeatable}}))))))

  (it "when playing out every possible game they should all return true for win or tie when human goes first"
    (should= true (every? true? (distinct (flatten (playout-every-game {:board [:- :- :- :- :- :- :- :- :-]
                                                                        :computer :o
                                                                        :options {:difficulty :unbeatable}}))))))

  (it "should return a greater number of trues than falses when playing at medium difficulty"
    (let [game-results (flatten (playout-every-game                    {:board [:- :- :- :- :- :- :- :- :-]
                                                                        :computer :x
                                                                        :options {:difficulty :medium}}))]

      (should= true ( > (count (filter true? game-results)) (count (filter false? game-results)))))))
