(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate :refer :all]
            [ttt-clojure.ai :refer :all]
            [ttt-clojure.display :refer :all]))

(def empty-space :-)

(defn get-next-move [gamestate]
  (if (computers-turn? gamestate)
      (find-move gamestate)
      (ask-human-for-move gamestate)))

(defn ttt [gamestate]
  (if (not (computers-turn? gamestate)) (print-board gamestate))
  (cond
    (win?  gamestate :x) (prn "x won")
    (win?  gamestate :o) (prn "o won")
    (tied? gamestate   ) (prn "tied!")
    :else (ttt (move gamestate (get-next-move gamestate)))))

(defn -main []
  (let [who-goes-first (ask-for-who-should-go-first)
        difficulty-setting (ask-for-difficulty)
        board-size (ask-for-board-size)]
       (ttt {:board (vec (repeat (* board-size board-size) empty-space))
             :computer (if (= who-goes-first :computer) :x :o)
             :options {:difficulty difficulty-setting}})))
