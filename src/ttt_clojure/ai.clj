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
  (let [difficulty (:difficulty (:options gamestate))
        difficulty-depth (cond
                           (= difficulty :unbeatable) 5
                           (= difficulty :medium) 1
                           :else 5)]
    (if (or (game-over? gamestate) (= depth difficulty-depth)) (leaf-score gamestate depth)
        (if (even? depth) (get-max-score gamestate depth alpha beta)
                          (get-min-score gamestate depth alpha beta)))))

(defn score-future-gamestates [gamestate]
  (into {} (for [possible-move (possible-moves gamestate)]
                [possible-move (minimax (move gamestate possible-move) 1 -100 100)])))

(defn eval-best-move-from-scores [scores]
  (first (last (select-keys scores (for [[index score] scores :when (max-score? score scores)] index)))))

(defn find-move [gamestate]
  (if (first-move? gamestate) 0
      (eval-best-move-from-scores (score-future-gamestates gamestate))))
