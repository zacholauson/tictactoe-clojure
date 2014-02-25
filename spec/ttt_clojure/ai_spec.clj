(ns ttt-clojure.ai-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.interface.player :refer :all]
            [ttt-clojure.players.human :refer [new-human]]
            [ttt-clojure.players.computer :refer [new-computer]]))

(defn computer-win-or-tie [gamestate]
  (or (win? gamestate (:computer gamestate)) (tied? gamestate)))

(defn playout-every-game [gamestate]
  (if (computers-turn? gamestate)
    (let [new-gamestate (move gamestate (find-move gamestate))
          computer-won-or-tied (computer-win-or-tie new-gamestate)]
      (if computer-won-or-tied true
          (map (fn [possible-move]
               (let [newer-gamestate (move new-gamestate possible-move)]
                 (cond
                   (tied? newer-gamestate) true
                   (win? newer-gamestate (:computer newer-gamestate)) true
                   (win? newer-gamestate (human-mark newer-gamestate)) false
                   :else (playout-every-game newer-gamestate))))
             (possible-moves new-gamestate))))))

(describe "#leaf-score"
  (context "3x3 board"
    (it "should return the correct leaf score for the given gamestate"
      (should=  10 (leaf-score {:board [:x :x :x :o :- :- :o :- :-] :computer :x} 0))
      (should= -10 (leaf-score {:board [:o :o :o :x :- :- :x :- :-] :computer :x} 0))
      (should=   0 (leaf-score {:board [:x :o :o :o :x :x :x :x :o] :computer :x} 0))
      (should=  10 (leaf-score {:board [:o :o :o :x :- :- :x :- :x] :computer :o} 0))
      (should= -10 (leaf-score {:board [:x :x :x :o :- :- :o :- :-] :computer :o} 0))
      (should=   0 (leaf-score {:board [:x :o :o :o :x :x :x :x :o] :computer :o} 0))))
  (context "4x4 board"
    (it "should return the correct leaf score for the given gamestate"
      (should=  10 (leaf-score {:board [:x :x :x :x :o :o :o :- :o :- :- :- :- :- :- :-] :computer :x} 0))
      (should= -10 (leaf-score {:board [:o :o :o :o :x :x :x :- :x :- :- :- :- :- :- :-] :computer :x} 0))
      (should=   0 (leaf-score {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x} 0))
      (should=  10 (leaf-score {:board [:o :o :o :o :x :x :x :- :x :- :- :- :- :- :- :-] :computer :o} 0))
      (should= -10 (leaf-score {:board [:x :x :x :x :o :o :o :- :o :- :- :- :- :- :- :-] :computer :o} 0))
      (should=   0 (leaf-score {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :o} 0))))
  (context "5x5 board"
    (it "should return the correct leaf score for the given gamestate"
      (should=  10 (leaf-score {:board [:x :x :x :x :x :o :o :o :o :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x} 0))
      (should= -10 (leaf-score {:board [:o :o :o :o :o :x :x :x :x :- :x :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x} 0))
      (should=   0 (leaf-score {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x} 0))
      (should=  10 (leaf-score {:board [:o :o :o :o :o :x :x :x :x :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :o} 0))
      (should= -10 (leaf-score {:board [:x :x :x :x :x :o :o :o :o :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :o} 0))
      (should=   0 (leaf-score {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :o} 0)))))

(describe "#calculate-depth-limit"
  (context "3x3 board"
    (it "should calculate the correct depth limit for the given gamestate"
      (should= 5 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :unbeatable}}))
      (should= 2 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :medium}}))))
  (context "4x4 board"
    (it "should calculate the correct depth limit for the given gamestate"
      (should= 3 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :unbeatable}}))
      (should= 1 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :medium}}))))
  (context "5x5 board"
    (it "should calculate the correct depth limit for the given gamestate"
      (should= 3 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :unbeatable}}))
      (should= 1 (calculate-depth-limit {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-] :computer :x :options {:difficulty :medium}})))))

(describe "#minimax"
  (tags :acceptance)
  (context "3x3 board"
    (context "computer goes first"
      (let [computer (new-computer :x)
            human    (new-human :o)]
        (it "should return 0 if its the first move"
          (should= 0 (find-move {:board [:- :- :- :- :- :- :- :- :-]
                                 :players [computer human]
                                 :computer :x
                                 :options {:difficulty :unbeatable}})))
        (it "should return take the win if its available"
          (should= 0 (find-move {:board [:- :x :x :o :o :x :- :- :o]
                                 :players [computer human]
                                 :computer :x
                                 :options {:difficulty :unbeatable}})))
        (it "should block :o from winning if theres a chance"
          (should= 1 (find-move {:board [:o :- :o :x :- :- :x :- :-]
                                 :players [computer human]
                                 :computer :x
                                 :options {:difficulty :unbeatable}})))
        (it "when playing out every possible game they should all return true for win or tie when computer goes first"
          (should= true (every? true? (distinct (flatten (playout-every-game {:board [:- :- :- :- :- :- :- :- :-]
                                                                              :players [computer human]
                                                                              :computer :x
                                                                              :options {:difficulty :unbeatable}}))))))
        (it "should return a greater number of trues than falses when playing at medium difficulty"
          (let [game-results (flatten (playout-every-game {:board [:- :- :- :- :- :- :- :- :-]
                                                           :players [computer human]
                                                           :computer :x
                                                           :options {:difficulty :medium}}))]

            (should= true (> (count (filter true? game-results)) (count (filter false? game-results))))))))

    (context "human goes first"
      (it "when playing out every possible game they should all return true for win or tie when human goes first"
        (should= true (every? true? (distinct (flatten (playout-every-game {:board [:- :- :- :- :- :- :- :- :-]
                                                                            :computer :o
                                                                            :options {:difficulty :unbeatable}}))))))))

  (context "4x4 board"
    (let [computer (new-computer :x)
          human    (new-human :o)]
      (it "should return 0 if its the first move"
        (should= 0 (find-move {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-]
                               :players [computer human]
                               :computer :x
                               :options {:difficulty :unbeatable}})))
      (it "should take the win if its available"
        (should= 2 (find-move {:board [:x :x :- :x :o :o :o :- :- :- :- :- :- :- :- :-]
                               :players [computer human]
                               :computer :x
                               :options {:difficulty :unbeatable}})))))

  (context "5x5 board"
    (let [computer (new-computer :x)
          human    (new-human :o)]
      (it "should return 0 if its the first move"
        (should= 0 (find-move {:board [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-]
                               :players [computer human]
                               :computer :x
                               :options {:difficulty :unbeatable}})))
      (it "should take the win if its available"
        (should= 2 (find-move {:board [:x :x :- :x :x :o :o :o :o :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-]
                               :players [computer human]
                               :computer :x
                               :options {:difficulty :unbeatable}}))))))
