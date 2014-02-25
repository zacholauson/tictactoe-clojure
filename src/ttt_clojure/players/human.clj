(ns ttt-clojure.players.human
  (:require [ttt-clojure.interface.player :refer :all]
            [ttt-clojure.display :refer :all]))

(deftype Human [-piece]
  Player
  (piece [this]
    -piece)
  (next-move [this gamestate]
    (ask-human-for-move gamestate)))

(defn new-human [piece]
  (Human. piece))

