(ns ttt-clojure.players.human
  (:require [ttt-clojure.interface.player   :refer :all]
            [ttt-clojure.interface.prompter :refer :all]))

(deftype Human [-piece -prompter]
  Player
  (piece [this]
    -piece)
  (next-move [this gamestate]
    (ask-for-move -prompter gamestate)))

(defn new-human [piece prompter]
  (Human. piece prompter))
