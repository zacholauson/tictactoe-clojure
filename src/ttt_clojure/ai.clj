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
    ; board size [unbeatable-depth medium-depth]
     9 [5 1]
    16 [3 1]
    25 [3 1]
       [5 1]))

(defn get-depths-based-on-difficulty [gamestate board-size-depths]
  (case (difficulty gamestate)
     :unbeatable (first board-size-depths)
         :medium (last board-size-depths)
               5))

(defn calculate-depth-limit [gamestate]
  (let [board-size-depths (get-depths-based-on-board-size (:board gamestate))
        difficulty-depth  (get-depths-based-on-difficulty gamestate board-size-depths)]
        difficulty-depth))

(defn minimax [gamestate depth alpha beta depth-limit]
  (if (or (game-over? gamestate) (= depth depth-limit)) (leaf-score gamestate depth)
      (if (even? depth) (get-max-score gamestate depth alpha beta depth-limit)
                        (get-min-score gamestate depth alpha beta depth-limit))))

(defn score-future-gamestates [gamestate]
  (into {} (for [possible-move (possible-moves gamestate)]
                [possible-move (minimax (move gamestate possible-move) 1 -100 100 (calculate-depth-limit gamestate))])))

(defn eval-best-move-from-scores [scores]
  (first (last (select-keys scores (for [[index score] scores :when (max-score? score scores)] index)))))

(defn get-calculated-move [gamestate]
  (if (first-move? gamestate) 0
      (eval-best-move-from-scores (score-future-gamestates gamestate))))

(defn get-random-move [gamestate]
  (rand-nth (possible-moves gamestate)))

(defn find-move [gamestate]
  (let [medium-or-unbeatable-difficulty? (or (= (difficulty gamestate) :unbeatable)
                                             (= (difficulty gamestate) :medium    ))]
    (if medium-or-unbeatable-difficulty?
      (get-calculated-move  gamestate)
      (get-random-move gamestate))))
