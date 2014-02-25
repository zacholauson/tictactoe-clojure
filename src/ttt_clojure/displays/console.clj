(ns ttt-clojure.displays.console
  (:require [ttt-clojure.interface.display :refer :all]
            [clojure.math.numeric-tower    :as math]))

(defn clear-screen []
  (print "\u001b[2J")
  (print "\u001B[0;0f"))

(defn index-board [board]
  (map-indexed #(if (= :- %2) (format "%2s" %1) (format "%2s" (name %2))) board))

(defn output-board [board]
  (clear-screen)
  (let [rows (partition (math/sqrt (count board)) (index-board board))]
    (loop [print-lines (map #(interpose " |" %) rows)]
      (apply println (first print-lines))
      (if (not (empty? (rest print-lines))) (recur (rest print-lines))))))

(deftype Console []
  Display
  (output [this output-string]
    (println output-string))
  (print-board [this board]
    (output-board board))
  (declare-winner [this winning-statement]
    (println winning-statement)))

(defn new-display-console []
  (Console.))
