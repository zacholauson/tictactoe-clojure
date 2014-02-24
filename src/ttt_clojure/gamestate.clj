(ns ttt-clojure.gamestate
  (:require [clojure.math.numeric-tower :as math]))

(defn first-move? [gamestate]
  (every? #(= :- %) (:board gamestate)))

(defn count-of [gamestate piece]
  (count (filter #(= piece %) (:board gamestate))))

(defn xs-turn? [gamestate]
  (<= (count-of gamestate :x)
      (count-of gamestate :o)))

(defn os-turn? [gamestate]
  (> (count-of gamestate :x)
     (count-of gamestate :o)))

(defn turn [gamestate]
  (cond
    (xs-turn? gamestate) :x
    (os-turn? gamestate) :o))

(defn computers-turn? [gamestate]
  (= (turn gamestate) (:computer gamestate)))

(defn step [step-num range-vec]
  (loop [range-vec range-vec
         return-col []]
    (if (> (count range-vec) 0)
      (let [return-col (conj return-col (first range-vec))]
        (recur (drop step-num range-vec) return-col))
       return-col)))

(defn right-diag [board-size]
  (let [range-vec (range board-size)
        step-num (- (math/sqrt board-size) 1)]
    (loop [range-vec range-vec
           return-col []]
      (if (< (+ (first range-vec) (+ step-num 1)) board-size)
        (let [return-col (conj return-col (+ (first range-vec) step-num))]
          (recur (drop step-num range-vec) return-col))
        return-col))))

(defn rows [board-size]
  (partition (math/sqrt board-size) (range board-size)))

(defn columns [board-size]
  (apply mapv vector (rows board-size)))

(defn calculate-winning-positions [gamestate]
  (let [board-size  (count (:board gamestate))
        row-size    (math/sqrt board-size)]
       (->> (right-diag board-size)
            (concat (step (+ row-size 1) (range board-size)))
            (concat (columns board-size))
            (concat (rows board-size))
            (flatten)
            (partition row-size))))

(defn winning-positions [gamestate]
  (calculate-winning-positions gamestate))

(defn winning-lines [gamestate]
  (map #(map (fn [line-piece] (nth (:board gamestate) line-piece)) %) (winning-positions gamestate)))

(defn win? [gamestate piece]
  (some true? (map (fn [line] (every? #{piece} line)) (winning-lines gamestate))))

(defn has-piece? [collection piece]
  (if (some #{piece} collection) true false))

(defn tied? [gamestate]
  (every? true? (map (fn [line]
                         (and (has-piece? line :x) (has-piece? line :o)))
                     (winning-lines gamestate))))

(defn game-over? [gamestate]
  (or (win? gamestate :x)
      (win? gamestate :o)
      (tied? gamestate)))

(defn space-free? [space]
  (= '(:-) (rest space)))

(defn filter-possible-move [space]
  (if (space-free? space) (first space)))

(defn possible-moves [gamestate]
  (vec (remove nil? (map filter-possible-move (map-indexed vector (:board gamestate))))))

(defn other-turn [gamestate]
  (if (= :x (turn gamestate)) :o :x))

(defn human-mark [gamestate]
  (if (= :x (:computer gamestate)) :o :x))

(defn add-play-to-board [gamestate index]
  (assoc (:board gamestate) index (turn gamestate)))

(defn difficulty [gamestate]
  (:difficulty (:options gamestate)))

(defn move [gamestate index]
  {:board    (add-play-to-board gamestate index)
   :computer (:computer gamestate)
   :options  (:options gamestate)})
