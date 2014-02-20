(ns ttt-clojure.gamestate)

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

(def three-by-three-winning-positions
  [[0 1 2] [3 4 5] [6 7 8] [0 3 6] [1 4 7] [2 5 8] [0 4 8] [2 4 6]])

(def four-by-four-winning-positions
  [[0 1 2 3] [4 5 6 7] [8 9 10 11] [12 13 14 15] [0 4 8 12] [1 5 9 13] [2 6 10 14] [3 7 11 15] [0 5 10 15] [3 6 9 12]])

(defn winning-positions [gamestate]
  (cond
    (= (count (:board gamestate)) 9) three-by-three-winning-positions
    (= (count (:board gamestate)) 16) four-by-four-winning-positions
    :else three-by-three-winning-positions))

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
