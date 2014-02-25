(ns ttt-clojure.players.computer
  (:require [ttt-clojure.interface.player :refer :all])
  (:use     [ttt-clojure.ai :as ai :only [find-move]]))

(deftype Computer [-piece]
  Player
  (piece [this]
    -piece)
  (next-move [this gamestate]
    (ai/find-move gamestate)))

(defn new-computer [piece]
  (Computer. piece))
