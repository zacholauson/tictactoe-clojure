(ns ttt-clojure.ai
  (:require [ttt-clojure.gamestate :refer :all]))

(declare minimax)

(defn leaf-score [gamestate depth]
  (cond
    (win? gamestate (:computer gamestate)) (+ 10 depth)
    (win? gamestate (human-mark gamestate)) (+ -10 depth)
    :else 0))

(defn max-score? [score scores]
  (= score (apply max (vals scores))))

(defn within-range? [beta gamestate-score]
  (< gamestate-score beta))

(defn playout-child-gamestates [gamestate depth alpha beta depth-limit]
  (map #(minimax (move gamestate %) (inc depth) alpha beta depth-limit) (possible-moves gamestate)))

(defn get-max-score [gamestate depth alpha beta depth-limit]
  (apply max (cons alpha (filter #(within-range? beta %) (playout-child-gamestates gamestate depth alpha beta depth-limit)))))

(defn get-min-score [gamestate depth alpha beta depth-limit]
  (apply min (cons beta  (filter #(within-range? beta %) (playout-child-gamestates gamestate depth alpha beta depth-limit)))))

(defn get-depths-based-on-board-size [board]
  (case (count board)
     9 [5 2]
    16 [3 1]
    25 [3 1]
       [5 2]))

(defn get-depths-based-on-difficulty [gamestate board-size-depths]
  (case (difficulty gamestate)
     :unbeatable (first board-size-depths)
         :medium (last board-size-depths)
               5))

(defn calculate-depth-limit [gamestate]
  (get-depths-based-on-difficulty
    gamestate
    (get-depths-based-on-board-size (:board gamestate))))

(defn reached-depth-limit? [depth depth-limit]
  (>= depth depth-limit))

(defn reached-the-end-of-the-tree? [gamestate depth depth-limit]
  (or (game-over? gamestate) (reached-depth-limit? depth depth-limit)))

(def minimax
  (memoize
    (fn [gamestate depth alpha beta depth-limit]
      (if (reached-the-end-of-the-tree? gamestate depth depth-limit) (leaf-score gamestate depth)
          (if (even? depth) (get-max-score gamestate depth alpha beta depth-limit)
                            (get-min-score gamestate depth alpha beta depth-limit))))))

(defn score-future-gamestates [gamestate]
  (into {} (for [possible-move (possible-moves gamestate)]
                [possible-move (minimax (move gamestate possible-move) 1 -100 100 (calculate-depth-limit gamestate))])))

(defn eval-best-move-from-scores [scores]
  (first (last (select-keys scores (for [[index score] scores :when (max-score? score scores)] index)))))

(defn get-calculated-move [gamestate]
  (if (first-move? gamestate) 0
      (-> gamestate score-future-gamestates eval-best-move-from-scores)))

(defmulti find-move difficulty)

(defmethod find-move :unbeatable [gamestate]
  (get-calculated-move gamestate))

(defmethod find-move :medium [gamestate]
  (get-calculated-move gamestate))

(defmethod find-move :easy [gamestate]
  (-> gamestate possible-moves rand-nth))

(defmethod find-move :default [gamestate]
  (get-calculated-move gamestate))
