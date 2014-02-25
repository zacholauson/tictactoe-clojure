(ns ttt-clojure.players.computer-spec
  (:require [speclj.core :refer :all]
            [ttt-clojure.interface.player :refer :all]
            [ttt-clojure.players.computer :refer [new-computer]]
            [ttt-clojure.players.human    :refer [new-human]]))

(describe "#piece"
  (it "should return the players piece"
    (let [computer (new-computer :x)]
      (should= :x (piece computer)))
    (let [computer (new-computer :o)]
      (should= :o (piece computer)))))

(describe "#next-move"
  (it "should return the next best move for the given gamestate"
    (let [computer (new-computer :x)
          human    (new-human :o nil)
          gamestate {:board   [:- :- :- :- :- :- :- :- :-]
                     :players [computer human]
                     :computer :x
                     :options {:difficulty :unbeatable}}]
      (should= 0 (next-move computer gamestate)))
    (let [computer (new-computer :x)
          human    (new-human :o nil)
          gamestate {:board   [:x :- :- :- :o :- :- :- :-]
                     :players [computer human]
                     :computer :x
                     :options {:difficulty :unbeatable}}]
      (should= 1 (next-move computer gamestate)))))
