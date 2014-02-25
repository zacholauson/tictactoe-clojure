(ns ttt-clojure.prompters.console
  (:require [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.interface.display  :refer :all]))

(defn clear-screen []
  (print "\u001b[2J")
  (print "\u001B[0;0f"))

(defn parse-int [string]
  (cond
    (string? string) (Integer. (re-find  #"\d+" string ))
    (integer? string) string
    :else (throw (Exception. "cannot convert given argument to integer"))))

(defn parse-input [input output-type]
 (case output-type
   :int (parse-int input)
   :str (str input)))

(defn valid-move? [gamestate move]
  (= :- (get (:board gamestate) move)))

(defn prompt
  ([display prompt-message]
    (output display (str prompt-message))
    (read-line))
  ([display prompt-message validation return-type]
    (output display (str prompt-message))
    (let [user-input (read-line)]
         (let [parsed-input (parse-input user-input return-type)]
              (if (validation parsed-input)
                  parsed-input
                  (prompt prompt-message validation return-type))))))

(defn prompt-for-who-should-go-first [display]
  (clear-screen)
  (let [user-input (prompt display "Who do you want to go first? \n 1 : computer \n 2 : human ")]
       (case user-input
          "1" :computer
          "2" :human
          (prompt-for-who-should-go-first display))))

(defn prompt-for-difficulty [display]
  (clear-screen)
  (let [user-input (prompt display "What difficulty would you like? \n 1 : unbeatable \n 2 : medium \n 3 : easy ")]
       (case user-input
          "1" :unbeatable
          "2" :medium
          "3" :easy
          (prompt-for-difficulty display))))

(defn prompt-for-board-size [display]
  (clear-screen)
  (let [user-input (prompt display "What size board would you like? \n 3 : 3 x 3 \n 4 : 4 x 4 \n 5 : 5 x 5")]
       (case user-input
          "3" 3
          "4" 4
          "5" 5
          (prompt-for-board-size display))))

(deftype Console [-display]
  Prompter
  (ask-for-move [this gamestate]
    (prompt -display "Next Move: " #(valid-move? gamestate %) :int))
  (ask-for-first-player [this]
    (prompt-for-who-should-go-first -display))
  (ask-for-difficulty [this]
    (prompt-for-difficulty -display))
  (ask-for-board-size [this]
    (prompt-for-board-size -display)))

(defn new-console-prompter [display]
  (Console. display))
