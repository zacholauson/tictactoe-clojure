(ns ttt-clojure.gamestate
  (:require [ttt-clojure.helper :refer :all]))

(defn first-move? [gamestate]
  (empty? (:movelist gamestate)))

(defn xs-turn? [gamestate]
  (if (<= (count (filter true? (map #(= :x %) (:board gamestate)))) (count (filter true? (map #(= :o %) (:board gamestate))))) true false))

(defn os-turn? [gamestate]
  (if (> (count (filter true? (map #(= :x %) (:board gamestate)))) (count (filter true? (map #(= :o %) (:board gamestate))))) true false))

(defn turn [gamestate]
  (cond
    (xs-turn? gamestate) :x
    (os-turn? gamestate) :o))

(def winning-positions
  [[0 1 2] [3 4 5] [6 7 8]
   [0 3 6] [1 4 7] [2 5 8]
   [0 4 8] [2 4 6]])

(defn winning-lines [gamestate]
  (map #(map (fn [line-piece] (nth (:board gamestate) line-piece)) %) winning-positions))

(defn win? [gamestate piece]
  (some #(true? %) (map (fn [line] (every? #{piece} line)) (winning-lines gamestate))))

(defn tied? [gamestate]
  (every? #(true? %) (map (fn [line]
                            (and
                              (if (some #{:x} line) true false)
                              (if (some #{:o} line) true false))) (winning-lines gamestate))))

(defn game-over? [gamestate]
  (or (win? gamestate :x) (win? gamestate :o) (tied? gamestate)))

(defn space-free? [space]
  (if (= '(:-) (rest space)) true false))

(defn filter-possible-move [space]
  (if (space-free? space) (first space) nil))

(defn possible-moves [gamestate]
  (let [board (:board gamestate)]
    (vec (remove nil? (map #(filter-possible-move %) (map-indexed vector board))))))

(defn other-turn [gamestate]
  (if (= :x (turn gamestate)) :o :x))

(defn add-play-to-movelist [gamestate index]
  (conj (:movelist gamestate) index))

(defn add-play-to-board [gamestate index]
  (do
    (assoc (:board gamestate) index (turn gamestate))))

(defn move [gamestate index]
  {:movelist (add-play-to-movelist gamestate index)
   :board (add-play-to-board gamestate index)})
