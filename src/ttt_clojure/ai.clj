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

(defn playout-child-gamestates [gamestate depth alpha beta]
  (map #(minimax (move gamestate %) (inc depth) alpha beta) (possible-moves gamestate)))

(defn get-max-score [gamestate depth alpha beta]
  (apply max (cons alpha (filter #(within-range? beta %) (playout-child-gamestates gamestate depth alpha beta)))))

(defn get-min-score [gamestate depth alpha beta]
  (apply min (cons beta  (filter #(within-range? beta %) (playout-child-gamestates gamestate depth alpha beta)))))

(defn minimax [gamestate depth alpha beta]
  (let [difficulty-depth (case (difficulty gamestate)
                            :unbeatable 5
                            :medium 1
                            5)]
    (if (or (game-over? gamestate) (= depth difficulty-depth)) (leaf-score gamestate depth)
        (if (even? depth) (get-max-score gamestate depth alpha beta)
                          (get-min-score gamestate depth alpha beta)))))

(defn score-future-gamestates [gamestate]
  (into {} (for [possible-move (possible-moves gamestate)]
                [possible-move (minimax (move gamestate possible-move) 1 -100 100)])))

(defn eval-best-move-from-scores [scores]
  (first (last (select-keys scores (for [[index score] scores :when (max-score? score scores)] index)))))

(defn calculate-move [gamestate]
  (if (first-move? gamestate) 0
      (eval-best-move-from-scores (score-future-gamestates gamestate))))

(defn get-random-move [gamestate]
  (rand-nth (possible-moves gamestate)))

(defn find-move [gamestate]
  (let [medium-or-unbeatable-difficulty? (or (= (difficulty gamestate) :unbeatable)
                                             (= (difficulty gamestate) :medium    ))]
    (if medium-or-unbeatable-difficulty?
      (calculate-move  gamestate)
      (get-random-move gamestate))))
