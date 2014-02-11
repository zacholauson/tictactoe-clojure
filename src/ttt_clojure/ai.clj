(ns ttt-clojure.ai
  (:require [ttt-clojure.gamestate :refer :all]))

(defn leaf-score [gamestate]
  (cond
    (win? gamestate :x)  1
    (win? gamestate :o) -1
    (tied? gamestate) 0))

(defn minimax [gamestate index]
  (let [gamestate (move gamestate index)]
       (if (leaf-score gamestate) (leaf-score gamestate)
           (let [scores (map #(minimax gamestate %) (possible-moves gamestate))]
                (if (xs-turn? gamestate) (apply max (flatten scores))
                    (apply min (flatten scores)))))))

(defn find-move [gamestate]
  (if (first-move? gamestate) 0
      (let [scores (into {} (for [possible-move (possible-moves gamestate)]
                                        [possible-move (minimax gamestate possible-move)]))]
           (first (last (select-keys scores (for [[index score] scores :when (= score (apply max (vals scores)))] index)))))))



;; OLD MINIMAX
;; (defn minimax [gamestate depth]
;;   (flatten (map
;;     (fn [possible-move]
;;       (let [gamestate (move gamestate possible-move)]
;;         (if (leaf-score gamestate)
;;           {:leaf-score (leaf-score gamestate) :movelist (:movelist gamestate) :depth depth}
;;           (minimax gamestate (inc depth)))))
;;     (possible-moves gamestate))))

;; (defn find-move [gamestate]
;;   (if (first-move? gamestate) 0
;;     (let [result (apply min-key :depth (let  [gameresults (group-by :leaf-score (minimax gamestate 0))]
;;                    (let [max-score (apply max (keys gameresults))]
;;                      (get gameresults max-score))))]
;;                        (last (drop-last (:depth result) (:movelist result))))))
;;
;; (defn find-move [gamestate]
;;   (first (last (first (sort (group-by :depth (minimax gamestate 0)))))))

