(ns ttt-clojure.gamestate)

(defn turn [gamestate]
  (:turn gamestate))

(defn movelist [gamestate]
  (:movelist gamestate))

(defn board [gamestate]
  (:board gamestate))

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
                              (if (some #{"x"} line) true false)
                              (if (some #{"o"} line) true false))) (winning-lines gamestate))))

(defn game-over? [gamestate]
  (or (win? gamestate "x") (win? gamestate "o") (tied? gamestate)))

(defn first-move? [gamestate]
  (empty? (movelist gamestate)))

(defn other-turn [gamestate]
  (if (= "x" (turn gamestate)) "o" "x"))

(defn make-next-move [gamestate index]
  {:movelist (conj (movelist gamestate) index) :turn (other-turn gamestate) :board (assoc (board gamestate) index (turn gamestate))})
