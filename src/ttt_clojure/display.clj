(ns ttt-clojure.display
  (:require [clojure.math.numeric-tower :as math]))

(defn clear-screen []
  (print "\u001b[2J")
  (print "\u001B[0;0f"))

(defn parse-int [string]
  (cond
    (string? string) (Integer. (re-find  #"\d+" string ))
    (integer? string) string
    :else (throw (Exception. "cannot convert given argument to integer"))))

(defn index-board [board]
  (map-indexed #(if (= :- %2) (format "%2s" %1) (format "%2s" (name %2))) board))

(defn parse-input [input output-type]
 (case output-type
   :int (parse-int input)
   :str (str input)))

(defn print-board [gamestate]
  (clear-screen)
  (let [board (:board gamestate)
        rows (partition (math/sqrt (count board)) (index-board board))]
    (loop [print-lines (map #(interpose " |" %) rows)]
      (apply println (first print-lines))
      (if (not (empty? (rest print-lines))) (recur (rest print-lines))))))

(defn prompt
  ([prompt-message]
    (println (str prompt-message))
    (read-line))
  ([prompt-message validation return-type]
    (println (str prompt-message))
    (let [user-input (read-line)]
         (let [parsed-input (parse-input user-input return-type)]
              (if (validation parsed-input)
                  parsed-input
                  (prompt prompt-message validation return-type))))))

(defn valid-move? [gamestate move]
  (= :- (get (:board gamestate) move)))

(defn ask-human-for-move [gamestate]
  (prompt "Next Move: " #(valid-move? gamestate %) :int))

(defn ask-for-who-should-go-first []
  (clear-screen)
  (let [user-input (prompt "Who do you want to go first? \n 1 : computer \n 2 : human ")]
       (case user-input
          "1" :computer
          "2" :human
          (ask-for-who-should-go-first))))

(defn ask-for-difficulty []
  (clear-screen)
  (let [user-input (prompt "What difficulty would you like? \n 1 : unbeatable \n 2 : medium \n 3 : easy ")]
       (case user-input
          "1" :unbeatable
          "2" :medium
          "3" :easy
          (ask-for-difficulty))))

(defn ask-for-board-size []
  (clear-screen)
  (let [user-input (prompt "What size board would you like? \n 3 : 3 x 3 \n 4 : 4 x 4 \n 5 : 5 x 5")]
       (case user-input
          "3" 3
          "4" 4
          "5" 5
          (ask-for-board-size))))
