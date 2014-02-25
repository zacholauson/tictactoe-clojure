(ns ttt-clojure.interface.player)

(defprotocol Player
  (piece [this])
  (next-move [this gamestate]))
