(ns ttt-clojure.gamestate
  (:require [ttt-clojure.helper :as helper]))

(defn first-move? [gamestate]
  (empty? (:movelist gamestate)))

(defn other-turn [gamestate]
  (if (= "x" (:turn gamestate)) "o" "x"))

(defn make-next-move [gamestate index]
  {:movelist (conj (:movelist gamestate) index)
   :turn (other-turn gamestate)
   :board (assoc (:board gamestate) index (:turn gamestate))})
