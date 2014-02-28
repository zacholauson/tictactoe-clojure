(ns ttt-clojure.displays.web
  (:require [ttt-clojure.interface.display :refer :all]
            [clojure.math.numeric-tower    :as math]
            [clojure.data.json             :as json]))

(defn row-size [board]
  (math/sqrt (count board)))

(defn index-board [board]
  (map-indexed #(if (= :- %2) (format "%2s" %1) (format "%2s" (name %2))) board))

(defn output-board [board]
  board)

(deftype Web []
  Display
  (output [this output-string]
    output-string)
  (print-board [this board]
    (output-board board))
  (declare-winner [this winning-statement]
    winning-statement))

(defn new-display-web []
  (Web.))
