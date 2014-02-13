(ns ttt-clojure.ai
  (:require [ttt-clojure.gamestate :refer :all]))

(declare minimax)

(defn leaf-score [gamestate depth]
  (cond
    (win? gamestate :x) (+ 10 depth)
    (win? gamestate :o) (+ -10 depth)
    :else 0))

(defn minimax [gamestate depth alpha beta]
  (if (or (game-over? gamestate) (= depth 5)) (leaf-score gamestate depth)
    (if (even? depth)
      (apply max (cons alpha
        (filter #(< % beta)
          (map (fn [possible-move]
                 (minimax (move gamestate possible-move) (inc depth) alpha beta))
               (possible-moves gamestate)))))

      (apply min (cons beta
        (filter #(< % beta)
          (map (fn [possible-move]
                 (minimax (move gamestate possible-move) (inc depth) alpha beta))
               (possible-moves gamestate))))))))

(defn find-move [gamestate]
  (if (first-move? gamestate) 0
    (let [scores (into {} (for [possible-move (possible-moves gamestate)]
                  [possible-move (minimax (move gamestate possible-move) 1 -100 100)]))]
         (first (last (select-keys scores (for [[index score] scores :when (= score (apply max (vals scores)))] index)))))))
