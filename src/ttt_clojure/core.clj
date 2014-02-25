(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate        :refer :all]
            [ttt-clojure.ai               :refer [find-move]]
            [ttt-clojure.display          :refer :all]
            [ttt-clojure.interface.player :refer :all]
            [ttt-clojure.players.computer :refer [new-computer]]
            [ttt-clojure.players.human    :refer [new-human]]))

(defn get-next-move [gamestate]
  (next-move (first (:players gamestate)) gamestate))

(defn ttt [gamestate]
  (print-board gamestate)
  (cond
    (win?  gamestate :x) (prn "x won")
    (win?  gamestate :o) (prn "o won")
    (tied? gamestate   ) (prn "tied!")
    :else (ttt (move gamestate (get-next-move gamestate)))))

(defn -main []
  (let [who-goes-first     (ask-for-who-should-go-first)
        difficulty-setting (ask-for-difficulty)
        computer           (new-computer (if (= who-goes-first :computer) :x :o))
        human              (new-human    (if (= who-goes-first :computer) :o :x))
        board-size         (ask-for-board-size)]
       (ttt {:board        (create-board board-size)
             :players      (if (= who-goes-first :computer) [computer human] [human computer])
             :computer     (if (= who-goes-first :computer) :x :o)
             :options      {:difficulty difficulty-setting}})))
