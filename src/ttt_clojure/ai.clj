(ns ttt-clojure.ai
  (:require [ttt-clojure.gamestate :refer :all]))

(declare minimax)

(defn leaf-score [gamestate]
  (cond
    (win? gamestate :x)  1
    (win? gamestate :o) -1
    (tied? gamestate) 0))

(defn max-score [scores]
  (apply max (flatten scores)))

(defn min-score [scores]
  (apply min (flatten scores)))

(defn playout-gamestates [gamestate]
  (map #(minimax gamestate %) (possible-moves gamestate)))

(defn return-score-for-gamestate [gamestate gamestate-scores]
  (if (xs-turn? gamestate)
      (max-score gamestate-scores)
      (min-score gamestate-scores)))

(defn score-gamestates [gamestate]
  (return-score-for-gamestate gamestate (playout-gamestates gamestate)))

(defn minimax [gamestate index]
  (let [gamestate (move gamestate index)]
    (if (game-over? gamestate) (leaf-score gamestate) (score-gamestates gamestate))))

(defn map-scores-to-moves [gamestate]
  (into {} (for [possible-move (possible-moves gamestate)]
                [possible-move (minimax gamestate possible-move)])))

(defn max-score? [score scores]
  (= score (apply max (vals scores))))

(defn pluck-best-score [scores]
  (first (last (select-keys scores
                            (for [[index score] scores :when (max-score? score scores)]
                                   index)))))

(defn find-move [gamestate]
  (if (first-move? gamestate) 0
    (pluck-best-score (map-scores-to-moves gamestate))))
