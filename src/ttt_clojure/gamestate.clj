(ns ttt-clojure.gamestate
  (:require [ttt-clojure.helper :refer :all]))

(defn first-move? [gamestate]
  (empty? (:movelist gamestate)))

